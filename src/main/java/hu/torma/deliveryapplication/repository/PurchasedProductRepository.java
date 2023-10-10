package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.DTO.DisplayUnit;
import hu.torma.deliveryapplication.entity.PurchasedProduct;
import hu.torma.deliveryapplication.primefaces.sumutils.SaleSumPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct, Integer> {
    @Query(name = "get_real_storage", nativeQuery = true)
    List<DisplayUnit> getRealStorage();

    @Query(name = "get_real_storage_date", nativeQuery = true)
    List<DisplayUnit> getRealStorageWithDates(
            @Param("date1") Date date1,
            @Param("date2") Date date2
    );
    @Query(name = "get_fictional_storage_date", nativeQuery = true)
    SaleSumPojo getFictionalStorageWithDates(
            @Param("date1") Date date1,
            @Param("date2") Date date2
    );
    @Query(name = "get_fictional_storage", nativeQuery = true)
    SaleSumPojo getFictionalStorage();
}