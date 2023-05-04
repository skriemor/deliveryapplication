package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.CompletedPurchaseDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;

import java.util.List;

public interface CompletedPurchaseService {
    List<CompletedPurchaseDTO> getAllCompletedPurchases();

    CompletedPurchaseDTO getCompletedPurchase(CompletedPurchaseDTO CompletedPurchaseDTO);

    CompletedPurchaseDTO saveCompletedPurchase(CompletedPurchaseDTO CompletedPurchaseDTO);

    void deleteCompletedPurchase(CompletedPurchaseDTO CompletedPurchaseDTO);

}
