package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;

import java.util.Date;
import java.util.List;

public interface PurchaseService {
    List<PurchaseDTO> getAllPurchases();

    PurchaseDTO getPurchase(PurchaseDTO PurchaseDTO);

    PurchaseDTO savePurchase(PurchaseDTO PurchaseDTO);

    void deletePurchase(PurchaseDTO PurchaseDTO);

    PurchaseDTO addProductToPurchase(PurchaseDTO PurchaseDTO, PurchasedProductDTO PurchasedProductDTO);

    PurchaseDTO getPurchaseById(Integer id);

    List<PurchaseDTO> getPsByStartingDate(Date startDate);

    List<PurchaseDTO> getPsByEndingDate(Date endDate);

    List<PurchaseDTO> getPsByBothDates(Date startDate, Date endDate);
}
