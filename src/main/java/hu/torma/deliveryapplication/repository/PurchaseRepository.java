package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
}