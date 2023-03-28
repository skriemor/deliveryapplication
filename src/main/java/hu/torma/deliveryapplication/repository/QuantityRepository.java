package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.entity.Quantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuantityRepository extends JpaRepository<Quantity, Integer> {
    @Query("SELECT q.amount FROM Quantity q WHERE q.product.id = :productName")
    Double getAmountOfProduct(String productName);

    @Query("SELECT q FROM Quantity q WHERE q.product.id = :productName")
    Quantity getQuantityByProductName(String productName);

    @Query("SELECT CASE WHEN count(q) > 0 then true else false end FROM Quantity q where q.product.id = :productName")
    Boolean existsByProductName(String productName);
}