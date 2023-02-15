package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.PurchasedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct, Integer> {
}