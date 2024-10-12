package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Tuple;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findAllByReceiptDateAfter(Date date);

    List<Purchase> findAllByReceiptDateBefore(Date date);

    List<Purchase> findAllByReceiptDateBetween(Date startDate, Date endDate);

    @Query(value = """
                SELECT DISTINCT p
                FROM Purchase p
                JOIN FETCH p.vendor v
                LEFT JOIN FETCH p.productList pl
                LEFT JOIN FETCH pl.product prod
                where (
                    ?1 is null or ?1 = v.taxId
                )
                and (
                    (p.receiptDate is null and ?2 is null and ?3 is null)
                        or (?2 is not null and ?3 is not null and p.receiptDate between ?2  and ?3)
                        or (?2 is not null and ?3 is null and  p.receiptDate >= ?2 )
                        or (?3 is not null and ?2 is null and  p.receiptDate <  ?3 )
                        or (?2 is null and  ?3 is null)
                )
                and (
                    ?4 is NULL or (?4 is false and p.remainingPrice = 0) or (?4 is true and p.remainingPrice <> 0)
                )
            """)
    List<Purchase> applyFilterChainAndReturnPurchases(String taxId, Date startDate, Date endDate, Boolean unPaidOnly);


    @Query(value = """
            select distinct p
            from Purchase p
            left join fetch p.vendor v
            left join fetch p.productList pList
            where (v.mediator.id = ?3 OR ?3 is null)
            and (
             (?1 is not null and ?2 is not null and p.receiptDate between ?1  and ?2)
                            or (?1 is not null and ?2 is null and  p.receiptDate >= ?1 )
                            or (?2 is not null and ?1 is null and  p.receiptDate <  ?2 )
                            or (?1 is null and  ?2 is null)
            )
            order by p.receiptDate asc NULLS LAST
            """)
    List<Purchase> getPurchasesByMediatorAndDate(Date startDate, Date endDate, String mediatorId);

    @Query(name = "supply_products_with_quantity", nativeQuery = true)
    List<ProductWithQuantity> getProductsWithQuantitiesByDates(
            @Param("date1") Date date1,
            @Param("date2") Date date2
    );

    @Query(nativeQuery = true, value = """
            SELECT pp.unit_price
            FROM (
            SELECT MAX(p.id) id, p.vendor_name name
            FROM purchase p
            where p.vendor_name like ?1
            group by name
            ) query2
            inner join purchased_product pp on pp.purchase_id = query2.id
            limit 6
            """)
    List<Integer> getLastPurchasePricesByVendorTaxId(
            @Param(value = "vendortaxid") String vendorid
    );

    @Query(nativeQuery = true, value = """
            select sum(r.price), GROUP_CONCAT(cp.new_serial SEPARATOR ' ')
            from records r
            inner join completed_purchase cp on cp.id = r.completed_id
            where r.purchase_id = ?1
            group by r.purchase_id
            """)
    Tuple getConcatedSerialsAndMaskedPricesById(@Param("id") Integer id);

    @Query(value = "SELECT p FROM Purchase p JOIN FETCH p.productList")
    List<Purchase> getAllAndFetchPPs();

    @Query(value = "SELECT p FROM Purchase p LEFT JOIN FETCH p.productList pl LEFT JOIN FETCH pl.product LEFT JOIN FETCH p.vendor v LEFT JOIN FETCH p.site s WHERE p.id = :id")
    Optional<Purchase> findAndFetchPPsById(@Param("id") Integer id);

    @Query(value = "select p from Purchase p join fetch p.vendor v where p.id = :id")
    Optional<Purchase> findPurchaseFetchAllById(@Param("id") Integer id);


    @Query(value = "select p from Purchase p join fetch p.vendor v")
    List<Purchase> findAllAndFetchVendors();
}