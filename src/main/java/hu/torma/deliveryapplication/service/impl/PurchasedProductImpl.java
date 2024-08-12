package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.entity.PurchasedProduct;
import hu.torma.deliveryapplication.repository.PurchasedProductRepository;
import hu.torma.deliveryapplication.service.PurchasedProductService;
import org.modelmapper.ModelMapper;
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
                        c -> mapper.map(c, PurchasedProductDTO.class)
                ).toList()
        );
    }

    @Override
    public PurchasedProductDTO getPurchasedProduct(PurchasedProductDTO PurchasedProductDTO) {
        return mapper.map(repo.findById(PurchasedProductDTO.getId()), PurchasedProductDTO.class);
    }

    @Override
    @Transactional
    public PurchasedProductDTO savePurchasedProduct(PurchasedProductDTO PurchasedProductDTO) {
        return mapper.map(repo.save(mapper.map(PurchasedProductDTO, PurchasedProduct.class)), PurchasedProductDTO.class);
    }

    @Override
    @Transactional
    public void deletePurchasedProduct(PurchasedProductDTO PurchasedProductDTO) {
        repo.deleteById(PurchasedProductDTO.getId());
    }



}
