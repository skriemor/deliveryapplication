package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.BuyerDTO;
import hu.torma.deliveryapplication.entity.Buyer;
import hu.torma.deliveryapplication.repository.BuyerRepository;
import hu.torma.deliveryapplication.service.BuyerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BuyerServiceImpl implements BuyerService {
    @Autowired
    BuyerRepository repo;
    ModelMapper mapper = new ModelMapper();

    @Override
    public List<BuyerDTO> getAllBuyers() {
        return new ArrayList<BuyerDTO>(
                repo.findAll().stream().map(
                        c -> mapper.map(c, BuyerDTO.class)
                ).toList()
        );
    }

    @Override
    public BuyerDTO getBuyer(BuyerDTO vendorDTO) {
        return mapper.map(repo.findById(vendorDTO.getAccountNum()), BuyerDTO.class);
    }

    @Override
    @Transactional
    public BuyerDTO saveBuyer(BuyerDTO vendorDTO) {
        return mapper.map(repo.save(mapper.map(vendorDTO, Buyer.class)), BuyerDTO.class);
    }

    @Override
    @Transactional
    public void deleteBuyer(BuyerDTO vendorDTO) {
        repo.deleteById(vendorDTO.getAccountNum());
    }

    @Override
    public BuyerDTO getBuyerById(String s) {
        return mapper.map(repo.findById(s), BuyerDTO.class);
    }
}
