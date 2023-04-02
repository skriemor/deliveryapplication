package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, String> {
}