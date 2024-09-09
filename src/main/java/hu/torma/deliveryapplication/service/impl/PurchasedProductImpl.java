package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.repository.PurchasedProductRepository;
import hu.torma.deliveryapplication.service.PurchasedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchasedProductImpl implements PurchasedProductService {
    @Autowired
    PurchasedProductRepository repo;

    @Override
    public List<PurchasedProductDTO> getAllPurchasedProducts() {
        return new ArrayList<PurchasedProductDTO>(
                repo.findAll().stream().map(
                        purchasedProduct -> purchasedProduct.toDTO(true, true, true)
                ).toList()
        );
    }

    @Override
    public PurchasedProductDTO getPurchasedProduct(PurchasedProductDTO PurchasedProductDTO) {
        return repo.findById(PurchasedProductDTO.getId()).map(purchasedProduct -> purchasedProduct.toDTO(true, true, true)).orElse(null);
    }

    @Override
    @Transactional
    public PurchasedProductDTO savePurchasedProduct(PurchasedProductDTO purchasedProductDTO) {
        return repo.save(purchasedProductDTO.toEntity(true, true, true)).toDTO(true, true, true);
    }

    @Override
    @Transactional
    public void deletePurchasedProduct(PurchasedProductDTO PurchasedProductDTO) {
        repo.deleteById(PurchasedProductDTO.getId());
    }



}
