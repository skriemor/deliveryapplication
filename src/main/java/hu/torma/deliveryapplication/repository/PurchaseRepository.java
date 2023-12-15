package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.DTO.DisplayUnit;
import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.entity.Sale;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findAllByReceiptDateAfter(Date date);

    List<Purchase> findAllByReceiptDateBefore(Date date);

    List<Purchase> findAllByReceiptDateBetween(Date startDate, Date endDate);

    @Query(nativeQuery = true, value = """
                SELECT p.*
                FROM purchase p
                join vendor v on v.tax_id = p.vendor_name
                where (
                    ?1 is null or LOWER(p.vendor_name) like LOWER(CONCAT('%', ?1 , '%'))
                )
                and (
                    (p.receipt_date is null and ?2 is null and ?3 is null)
                        or (?2 is not null and ?3 is not null and p.receipt_date between ?2  and ?3)
                        or (?2 is not null and ?3 is null and  p.receipt_date >= ?2 )
                        or (?3 is not null and ?2 is null and  p.receipt_date <  ?3 )
                        or (?2 is null and  ?3 is null)
                )
                and (
                    ?4 is NULL or (?4 is false and p.remaining_price = 0) or (?4 is true and p.remaining_price <> 0)
                )
            """)
    List<Purchase> applyFilterChainAndReturnPurchases(String name, Date startDate, Date endDate, Boolean unPaidOnly);


    @Query(nativeQuery = true, value = """
            select p.*
            from purchase p
            join vendor v on v.tax_id = p.vendor_name
            where (v.mediator_id = ?3 OR ?3 is null)
            and (
             (?1 is not null and ?2 is not null and p.receipt_date between ?1  and ?2)
                            or (?1 is not null and ?2 is null and  p.receipt_date >= ?1 )
                            or (?2 is not null and ?1 is null and  p.receipt_date <  ?2 )
                            or (?1 is null and  ?2 is null)
            )
            order by p.receipt_date asc NULLS LAST 
            """)
    List<Purchase> getPurchasesByMediatorAndDate(Date startDate, Date endDate, String mediatorId);

    @Query(name = "supply_products_with_quantity", nativeQuery = true)
    List<ProductWithQuantity> getProductsWithQuantitiesByDates(
            @Param("date1") Date date1,
            @Param("date2") Date date2
    );
}