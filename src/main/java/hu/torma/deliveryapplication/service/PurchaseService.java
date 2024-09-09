package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;

import javax.persistence.Tuple;
import java.util.Date;
import java.util.List;

public interface PurchaseService {
    PurchaseDTO getRecordlessPurchaseById(Integer id);
    List<PurchaseDTO> getAllPurchases();

    PurchaseDTO getPurchase(PurchaseDTO PurchaseDTO);

    PurchaseDTO savePurchase(PurchaseDTO PurchaseDTO);
    Purchase savePurchase(Purchase purchase);

    void deletePurchase(PurchaseDTO PurchaseDTO);

    PurchaseDTO addProductToPurchase(PurchaseDTO PurchaseDTO, PurchasedProductDTO PurchasedProductDTO);

    PurchaseDTO getPurchaseById(Integer id);

    Purchase getPurchaseEntityById(Integer id);

    PurchaseDTO getPurchaseForSelectionById(Integer id);
    List<PurchaseDTO> getAllPurchasesForSelection();

    List<PurchaseDTO> getPsByStartingDate(Date startDate);

    List<PurchaseDTO> getPsByEndingDate(Date endDate);

    List<PurchaseDTO> getPsByBothDates(Date startDate, Date endDate);

    List<PurchaseDTO> getPurchasesByMediatorIdAndDates(Date startDate, Date endDate, String mediatorId);
    List<PurchaseDTO> applyFilterChainAndReturnPurchases(String name, Date startDate, Date endDate, Boolean unPaidOnly);

    List<Integer> getPricesOnLastPurchase(String vendorId);

    List<ProductWithQuantity> getPurchasesByDates(Date date1, Date date2);

    Tuple getConcatedSerialsAndMaskedPricesById(Integer id);

    List<PurchaseDTO> getAllPurchasesAndFetchPPs();

    List<PurchaseDTO> getAllPurchasesForListing();

    PurchaseDTO getPurchaseAndFetchPPsById(Integer id);
}
