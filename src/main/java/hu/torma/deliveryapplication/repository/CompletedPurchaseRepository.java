package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.CompletedPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CompletedPurchaseRepository extends JpaRepository<CompletedPurchase, Integer> {
    List<CompletedPurchase> findAllByReceiptDateAfter(Date date);
    List<CompletedPurchase> findAllByReceiptDateBefore(Date date);

    List<CompletedPurchase> findAllByReceiptDateBetween(Date startDate, Date endDate);

    @Query(nativeQuery = true,value = """
        SELECT cp.*
        FROM COMPLETED_PURCHASE cp
        join vendor v on cp.vendor_name = v.tax_id
        where (?1 is null or LOWER(v.vendor_name) like LOWER(CONCAT('%', ?1 , '%')))
        and (
            cp.receipt_date is null
                or (?2 is not null and ?3 is not null and cp.receipt_date between ?2  and ?3)
                or (?2 is not null and ?3 is null and  cp.receipt_date >= ?2 )
                or (?3 is not null and ?2 is null and  cp.receipt_date <  ?3 )
                or (?2 is null and  ?3 is null)
        )
        and (
            ?4 is null or cp.new_serial like %?4%
        )
        and (
            ?5 is false or cp.payment_date is null
        )
        and (
            ?6 is null or cp.payment_method like %?6%
        )
    """)
    List<CompletedPurchase> applyFilterChainAndReturnResults(String name, Date startDate, Date endDate, String numSerial,boolean notPaidOnly, String paymentMethod);
}