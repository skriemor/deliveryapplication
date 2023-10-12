package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findAllByReceiptDateAfter(Date date);
    List<Purchase> findAllByReceiptDateBefore(Date date);

    List<Purchase> findAllByReceiptDateBetween(Date startDate, Date endDate);

    @Query(nativeQuery = true,value = """
        SELECT p.*
        FROM purchase p
        join vendor v on v.tax_id = p.vendor_name
        where (
            ?1 is null or LOWER(v.vendor_name) like LOWER(CONCAT('%', ?1 , '%'))
        )
        and (
            p.receipt_date is null
                or (?2 is not null and ?3 is not null and p.receipt_date between ?2  and ?3)
                or (?2 is not null and ?3 is null and  p.receipt_date >= ?2 )
                or (?3 is not null and ?2 is null and  p.receipt_date <  ?3 )
                or (?2 is null and  ?3 is null)
        )
        and (
            ?4 is false or p.remaining_price <> 0
        )
    """)
    List<Purchase> applyFilterChainAndReturnPurchases(String name,  Date startDate, Date endDate, Boolean unPaidOnly);



}