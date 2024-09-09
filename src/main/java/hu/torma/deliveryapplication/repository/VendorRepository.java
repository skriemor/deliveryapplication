package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, String> {
    Vendor findVendorByVendorName(String s);

    @Query(value = "select v from Vendor v left join fetch v.mediator m")
    List<Vendor> findAllWithMediatorFetch();


}