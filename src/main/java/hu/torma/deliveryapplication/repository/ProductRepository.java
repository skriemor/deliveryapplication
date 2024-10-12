package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    @Query(value = "select p from Product p join fetch p.firstUnit join fetch p.secondUnit")
    List<Product> findAllFetchAll();
}