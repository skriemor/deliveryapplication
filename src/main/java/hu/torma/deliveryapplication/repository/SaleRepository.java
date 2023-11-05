package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Sale;
import hu.torma.deliveryapplication.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    List<Sale> findAllByReceiptDateAfter(Date date);
    List<Sale> findAllByReceiptDateBefore(Date date);
    List<Sale> findAllByReceiptDateBetween(Date startDate, Date endDate);
    @Query(nativeQuery = true,value = """
        SELECT s.*
        FROM sale s
        join BUYER b on b.ID = s.BUYER_NAME
        where (
            ?1 is null or LOWER(s.BUYER_NAME) like LOWER(CONCAT('%', ?1 , '%'))
        )
        and (
           ?2 is null or LOWER(s.CURRENCY) like LOWER(CONCAT('%', ?2, '%'))
        )
        and (
            s.receipt_date is null
                or (?3 is not null and ?4 is not null and s.receipt_date between ?3  and ?4)
                or (?3 is not null and ?4 is null and  s.receipt_date >= ?3 )
                or (?4 is not null and ?3 is null and  s.receipt_date <  ?4 )
                or (?3 is null and  ?4 is null)
        )
        and (
            ?5 is null or (?5 is false and s.COMPLETION_DATE is not NULL) or (s.COMPLETION_DATE is null and ?5 is true)
        )
         and (
            ?6 is null or ?6 like '' or b.PAPER = ?6
        )
        and (
            ?7 is null or s.LETAI = ?7
        )
        and (
            ?8 is null or s.GLOBALGAP = ?8
        )
       
    """)
    List<Sale> applyFilterChainAndReturnSales(String name, String currency, Date startDate, Date endDate, Boolean unPaidOnly, String paper, Boolean letaiOnly, Boolean globalGapOnly);
}