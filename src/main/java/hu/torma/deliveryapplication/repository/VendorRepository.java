package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, String> {
    Vendor findVendorByVendorName(String s);
}