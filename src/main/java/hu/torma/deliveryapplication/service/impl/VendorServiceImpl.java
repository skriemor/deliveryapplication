package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.VendorDTO;
import hu.torma.deliveryapplication.repository.VendorRepository;
import hu.torma.deliveryapplication.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    VendorRepository repo;

    @Override
    public List<VendorDTO> getAllVendors() {
        return new ArrayList<VendorDTO>(
                repo.findAll().stream().map(
                        vendor -> vendor.toDTO(true)
                ).toList()
        );
    }

    @Override
    public VendorDTO getVendor(VendorDTO vendorDTO) {
        return repo.findById(vendorDTO.getTaxId()).map(vendor -> vendor.toDTO(true)).orElse(null);
    }

    @Override
    @Transactional
    public VendorDTO saveVendor(VendorDTO vendorDTO) {
        return repo.save(vendorDTO.toEntity(true)).toDTO(true);
    }

    @Override
    @Transactional
    public void deleteVendor(VendorDTO vendorDTO) {
        repo.deleteById(vendorDTO.getTaxId());
    }

    @Override
    public VendorDTO getVendorById(String s) {
        return repo.findById(s).map(vendor -> vendor.toDTO(true)).orElse(null);
    }
}
