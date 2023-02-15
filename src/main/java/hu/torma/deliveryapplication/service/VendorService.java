package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.VendorDTO;

import java.util.List;

public interface VendorService {
    List<VendorDTO> getAllVendors();

    VendorDTO getVendor(VendorDTO vendorDTO);

    VendorDTO saveVendor(VendorDTO vendorDTO);

    void deleteVendor(VendorDTO vendorDTO);

    VendorDTO getVendorById(String s);

}
