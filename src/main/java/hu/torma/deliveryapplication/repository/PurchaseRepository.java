package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findAllByReceiptDateAfter(Date date);
    List<Purchase> findAllByReceiptDateBefore(Date date);

    List<Purchase> findAllByReceiptDateBetween(Date startDate, Date endDate);
}