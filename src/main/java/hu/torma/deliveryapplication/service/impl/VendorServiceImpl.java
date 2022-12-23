package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.VendorDTO;
import hu.torma.deliveryapplication.entity.Vendor;
import hu.torma.deliveryapplication.repository.VendorRepository;
import hu.torma.deliveryapplication.service.VendorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    VendorRepository repo;
    ModelMapper mapper = new ModelMapper();

    @Override
    public List<VendorDTO> getAllVendors() {
        return new ArrayList<VendorDTO>(
                repo.findAll().stream().map(
                        c -> mapper.map(c, VendorDTO.class)
                ).toList()
        );
    }

    @Override
    public VendorDTO getVendor(VendorDTO vendorDTO) {
        return mapper.map(repo.findById(vendorDTO.getTaxId()), VendorDTO.class);
    }

    @Override
    @Transactional
    public VendorDTO saveVendor(VendorDTO vendorDTO) {
        return mapper.map(repo.save(mapper.map(vendorDTO, Vendor.class)), VendorDTO.class);
    }

    @Override
    @Transactional
    public void deleteVendor(VendorDTO vendorDTO) {
        repo.deleteById(vendorDTO.getTaxId());
    }
}
