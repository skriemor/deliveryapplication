package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Sale;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    List<Sale> findAllByReceiptDateAfter(Date date);
    List<Sale> findAllByReceiptDateBefore(Date date);
    List<Sale> findAllByReceiptDateBetween(Date startDate, Date endDate);

    @Query("""
    SELECT DISTINCT s
    FROM Sale s
    LEFT JOIN FETCH s.buyer b
    LEFT JOIN FETCH s.productList pl
    LEFT JOIN FETCH pl.product prod
    WHERE (
        :bankAccountNum IS NULL OR b.accountNum = :bankAccountNum
    )
    AND (
        :currency IS NULL OR LOWER(s.currency) LIKE LOWER(CONCAT('%', :currency, '%'))
    )
    AND (
        (s.receiptDate IS NULL AND :startDate IS NULL AND :endDate IS NULL)
        OR (:startDate IS NOT NULL AND :endDate IS NOT NULL AND s.receiptDate BETWEEN :startDate AND :endDate)
        OR (:startDate IS NOT NULL AND :endDate IS NULL AND s.receiptDate >= :startDate)
        OR (:endDate IS NOT NULL AND :startDate IS NULL AND s.receiptDate < :endDate)
        OR (:startDate IS NULL AND :endDate IS NULL)
    )
    AND (
        :unPaidOnly IS NULL OR (:unPaidOnly = false AND s.completionDate IS NOT NULL) OR (s.completionDate IS NULL AND :unPaidOnly = true)
    )
    AND (
        :paper IS NULL OR :paper = '' OR b.paper = :paper
    )
    AND (
        :letaiOnly IS NULL OR s.letai = :letaiOnly
    )
    AND (
        :globalGapOnly IS NULL OR s.globalgap = :globalGapOnly
    )
""")
    List<Sale> applyFilterChainAndReturnSales(@Param("bankAccountNum") String bankAccountNum,
                                              @Param("currency") String currency,
                                              @Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate,
                                              @Param("unPaidOnly") Boolean unPaidOnly,
                                              @Param("paper") String paper,
                                              @Param("letaiOnly") Boolean letaiOnly,
                                              @Param("globalGapOnly") Boolean globalGapOnly);

    @Query(name = "supply_products_with_quantity_sale", nativeQuery = true)
    List<ProductWithQuantity> getProductsWithQuantitiesByDates(
            @Param("date1") Date date1,
            @Param("date2") Date date2
    );

    @Query(name = "supply_official_products_with_quantity_sale", nativeQuery = true)
    List<ProductWithQuantity> getOfficialProductsWithQuantitiesByDates(
            @Param("date1") Date date1,
            @Param("date2") Date date2
    );

    @Query(value = "select s from Sale s left join fetch s.productList pl left join fetch pl.product where s.id = :id ")
    Optional<Sale> getSaleById(@Param("id") int id);
}