package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;

import java.util.List;

public interface PurchaseService {
    List<PurchaseDTO> getAllPurchases();

    PurchaseDTO getPurchase(PurchaseDTO PurchaseDTO);

    PurchaseDTO savePurchase(PurchaseDTO PurchaseDTO);

    void deletePurchase(PurchaseDTO PurchaseDTO);

    PurchaseDTO addProductToPurchase(PurchaseDTO PurchaseDTO, PurchasedProductDTO PurchasedProductDTO);

    PurchaseDTO getPurchaseById(Integer id);
}
