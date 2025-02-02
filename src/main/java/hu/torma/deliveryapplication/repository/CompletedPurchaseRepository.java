package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.CompletedPurchase;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CompletedPurchaseRepository extends JpaRepository<CompletedPurchase, Integer> {
    List<CompletedPurchase> findAllByReceiptDateAfter(Date date);
    List<CompletedPurchase> findAllByReceiptDateBefore(Date date);

    List<CompletedPurchase> findAllByReceiptDateBetween(Date startDate, Date endDate);

    @Query("""
    SELECT DISTINCT cp
    FROM CompletedPurchase cp
    LEFT JOIN FETCH cp.vendor v
    LEFT JOIN FETCH cp.records recs
    WHERE (:taxId IS NULL OR v.taxId = :taxId)
    AND (
        (cp.receiptDate IS NULL AND :startDate IS NULL AND :endDate IS NULL)
        OR (:startDate IS NOT NULL AND :endDate IS NOT NULL AND cp.receiptDate BETWEEN :startDate AND :endDate)
        OR (:startDate IS NOT NULL AND :endDate IS NULL AND cp.receiptDate >= :startDate)
        OR (:endDate IS NOT NULL AND :startDate IS NULL AND cp.receiptDate < :endDate)
        OR (:startDate IS NULL AND :endDate IS NULL)
    )
    AND (
        cp.newSerial IS NULL
        OR (:serialBegin IS NOT NULL AND :serialEnd IS NOT NULL AND cp.newSerial BETWEEN :serialBegin AND :serialEnd)
        OR (:serialBegin IS NOT NULL AND :serialEnd IS NULL AND cp.newSerial >= :serialBegin)
        OR (:serialEnd IS NOT NULL AND :serialBegin IS NULL AND cp.newSerial < :serialEnd)
        OR (:serialBegin IS NULL AND :serialEnd IS NULL)
    )
    AND (
        :notPaidOnly IS NULL OR (:notPaidOnly = false AND cp.paymentDate IS NOT NULL) OR (:notPaidOnly = true AND cp.paymentDate IS NULL)
    )
    AND (
        :paymentMethod IS NULL OR cp.paymentMethod LIKE CONCAT('%', :paymentMethod, '%')
    )
""")
    List<CompletedPurchase> applyFilterChainAndReturnResults(
            @Param("taxId") String taxId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("serialBegin") String serialBegin,
            @Param("serialEnd") String serialEnd,
            @Param("notPaidOnly") Boolean notPaidOnly,
            @Param("paymentMethod") String paymentMethod
    );


    @Query(value = """
        select DISTINCT cp
        from  CompletedPurchase cp
        LEFT JOIN FETCH cp.vendor v
        LEFT JOIN FETCH cp.records r
        where (v.mediator.id = ?3 OR ?3 is null)
        and (
            (?1 is not null and ?2 is not null and cp.receiptDate between ?1  and ?2)
                            or (?1 is not null and ?2 is null and  cp.receiptDate >= ?1 )
                            or (?2 is not null and ?1 is null and  cp.receiptDate <  ?2 )
                            or (?1 is null and  ?2 is null)
        )
        order by cp.receiptDate asc NULLS LAST
    """)
    List<CompletedPurchase> getCompletedPurchasesByMediatorAndDate(Date startDate, Date endDate, String mediatorId);

    @Query(nativeQuery = true, value = """
            select p.receipt_date
                                                 
                                      from completed_purchase cp
                                      join records r on r.completed_id = cp.id
                                      join purchase p on p.id = r.purchase_id
                                      
                                      where cp.id = ?1
                                      
                                      order by p.receipt_date asc
                                      
                                      limit 1
            """)
    Optional<Date> getEarliestPurchaseDate(Integer id);

    @Query(value = "SELECT cp FROM CompletedPurchase cp JOIN FETCH cp.records")
    List<CompletedPurchase> findAllAndFetchRecords();

    @Query(value = "SELECT cp FROM CompletedPurchase cp JOIN FETCH cp.records records JOIN FETCH records.purchase p")
    List<CompletedPurchase> findAllAndFetchRecordsAndPurchases();

    @Query(value = "SELECT cp FROM CompletedPurchase cp LEFT JOIN FETCH cp.records recs LEFT JOIN FETCH recs.completedPurchase LEFT JOIN FETCH recs.purchase purchase LEFT JOIN FETCH cp.vendor vendor LEFT JOIN FETCH cp.site site WHERE cp.id = :idParam ")
    CompletedPurchase findAndFetchRecordsById(@Param("idParam") Integer id);

    @Query(value = "select distinct cp from CompletedPurchase cp left join fetch cp.site left join fetch cp.vendor v left join fetch cp.records r left join fetch r.purchase p")
    List<CompletedPurchase> findAllForCpListing();


    @Query(name = "supply_completed_purchases_with_dates", nativeQuery = true)
    List<ProductWithQuantity> getCpsByDatesAsProductWithQuantities(
            @Param("date1") Date date1,
            @Param("date2") Date date2
    );

}