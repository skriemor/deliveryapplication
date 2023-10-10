package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;

import java.util.List;

public interface PurchasedProductService {
    List<PurchasedProductDTO> getAllPurchasedProducts();

    PurchasedProductDTO getPurchasedProduct(PurchasedProductDTO PurchasedProductDTO);

    PurchasedProductDTO savePurchasedProduct(PurchasedProductDTO PurchasedProductDTO);

    void deletePurchasedProduct(PurchasedProductDTO PurchasedProductDTO);

}
