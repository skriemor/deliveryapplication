package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}