package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.BuyerDTO;
import hu.torma.deliveryapplication.entity.Buyer;
import hu.torma.deliveryapplication.repository.BuyerRepository;
import hu.torma.deliveryapplication.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BuyerServiceImpl implements BuyerService {
    @Autowired
    BuyerRepository repo;

    @Override
    public List<BuyerDTO> getAllBuyers() {
        return new ArrayList<BuyerDTO>(
                repo.findAll().stream().map(
                        Buyer::toDTO
                ).toList()
        );
    }

    @Override
    public BuyerDTO getBuyer(BuyerDTO vendorDTO) {
        return repo.findById(vendorDTO.getAccountNum()).map(Buyer::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public BuyerDTO saveBuyer(BuyerDTO vendorDTO) {
        return repo.save(vendorDTO.toEntity()).toDTO();
    }

    @Override
    @Transactional
    public void deleteBuyer(BuyerDTO vendorDTO) {
        repo.deleteById(vendorDTO.getAccountNum());
    }

    @Override
    public BuyerDTO getBuyerById(String s) {
        return repo.findById(s).map(Buyer::toDTO).orElse(null);
    }
}
