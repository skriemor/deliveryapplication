package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.CompletedPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletedPurchaseRepository extends JpaRepository<CompletedPurchase, Integer> {
}