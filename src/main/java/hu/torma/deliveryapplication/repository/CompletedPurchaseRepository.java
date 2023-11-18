package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.CompletedPurchase;
import hu.torma.deliveryapplication.entity.Purchase;
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
        where (?1 is null or LOWER(cp.vendor_name) like LOWER(CONCAT('%', ?1 , '%')))
        and (
            cp.receipt_date is null
                or (?2 is not null and ?3 is not null and cp.receipt_date between ?2  and ?3)
                or (?2 is not null and ?3 is null and  cp.receipt_date >= ?2 )
                or (?3 is not null and ?2 is null and  cp.receipt_date <  ?3 )
                or (?2 is null and  ?3 is null)
        )
        and (
        cp.new_serial is null
                or (?4 is not null and ?5 is not null and cp.new_serial between ?4  and ?5)
                or (?4 is not null and ?5 is null and  cp.new_serial >= ?4 )
                or (?5 is not null and ?4 is null and  cp.new_serial <  ?5 )
                or (?4 is null and  ?5 is null)
        )
        and (
            ?6 is null or (?6 is false and cp.payment_date is not null) or (?6 is true and cp.payment_date is null)
        )
        and (
            ?7 is null or cp.payment_method like %?7%
        )
    """)
    List<CompletedPurchase> applyFilterChainAndReturnResults(String name, Date startDate, Date endDate, String numSerial1,String numSerial2,Boolean notPaidOnly, String paymentMethod);


    @Query(nativeQuery = true, value = """
select cp.*
from completed_purchase cp
join vendor v on v.tax_id = cp.vendor_name
where (v.mediator_id = ?3 OR ?3 is null)
and (
(?1 is not null and ?2 is not null and cp.receipt_date between ?1  and ?2)
                or (?1 is not null and ?2 is null and  cp.receipt_date >= ?1 )
                or (?2 is not null and ?1 is null and  cp.receipt_date <  ?2 )
                or (?1 is null and  ?2 is null)
)
order by cp.receipt_date asc NULLS LAST 
""")
    List<CompletedPurchase> getCompletedPurchasesByMediatorAndDate(Date startDate, Date endDate, String mediatorId);

}