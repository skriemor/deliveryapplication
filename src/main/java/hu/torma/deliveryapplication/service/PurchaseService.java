package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;

import javax.persistence.Tuple;
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

    List<PurchaseDTO> getPurchasesByMediatorIdAndDates(Date startDate, Date endDate, String mediatorId);
    List<PurchaseDTO> applyFilterChainAndReturnPurchases(String name, Date startDate, Date endDate, Boolean unPaidOnly);

    List<Integer> getPricesOnLastPurchase(String vendorId);

    List<ProductWithQuantity> getPurchasesByDates(Date date1, Date date2);

    Tuple getConcatedSerialsAndMaskedPricesById(Integer id);
}
