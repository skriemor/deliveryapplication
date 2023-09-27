package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.CompletedPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CompletedPurchaseRepository extends JpaRepository<CompletedPurchase, Integer> {
    List<CompletedPurchase> findAllByReceiptDateAfter(Date date);
    List<CompletedPurchase> findAllByReceiptDateBefore(Date date);

    List<CompletedPurchase> findAllByReceiptDateBetween(Date startDate, Date endDate);
}