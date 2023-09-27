package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Sale;
import hu.torma.deliveryapplication.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
    List<Sale> findAllByReceiptDateAfter(Date date);
    List<Sale> findAllByReceiptDateBefore(Date date);

    List<Sale> findAllByReceiptDateBetween(Date startDate, Date endDate);
}