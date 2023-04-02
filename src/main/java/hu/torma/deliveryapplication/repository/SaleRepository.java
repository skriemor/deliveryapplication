package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
}