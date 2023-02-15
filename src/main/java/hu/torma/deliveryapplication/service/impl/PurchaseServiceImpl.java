package hu.torma.deliveryapplication.service.impl;


import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.repository.PurchaseRepository;
import hu.torma.deliveryapplication.service.PurchaseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    PurchaseRepository repo;
    ModelMapper mapper = new ModelMapper();

    @Override
    public List<PurchaseDTO> getAllPurchases() {
        return new ArrayList<PurchaseDTO>(
                repo.findAll().stream().map(
                        c -> mapper.map(c, PurchaseDTO.class)
                ).toList()
        );
    }

    @Override
    public PurchaseDTO getPurchase(PurchaseDTO PurchaseDTO) {
        return mapper.map(repo.findById(PurchaseDTO.getId()), PurchaseDTO.class);
    }

    @Override
    @Transactional
    public PurchaseDTO savePurchase(PurchaseDTO PurchaseDTO) {
        return mapper.map(repo.save(mapper.map(PurchaseDTO, Purchase.class)), PurchaseDTO.class);
    }

    @Override
    @Transactional
    public void deletePurchase(PurchaseDTO PurchaseDTO) {
        repo.deleteById(PurchaseDTO.getId());
    }

    @Override
    public PurchaseDTO addProductToPurchase(PurchaseDTO PurchaseDTO, PurchasedProductDTO PurchasedProductDTO) {
        PurchaseDTO.getProductList().add(PurchasedProductDTO);
        return PurchaseDTO;
    }
}
