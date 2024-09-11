package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;

import javax.persistence.Tuple;
import java.util.Date;
import java.util.List;

public interface PurchaseService {
    List<PurchaseDTO> getAllPurchases();

    void savePurchase(PurchaseDTO PurchaseDTO);
    Purchase savePurchase(Purchase purchase);

    void deletePurchase(PurchaseDTO PurchaseDTO);

    PurchaseDTO getPurchaseById(Integer id);

    Purchase getPurchaseEntityById(Integer id);

    PurchaseDTO getPurchaseForSelectionById(Integer id);
    List<PurchaseDTO> getAllPurchasesForSelection();

    List<PurchaseDTO> getAllForListing();


    List<PurchaseDTO> getPurchasesByMediatorIdAndDates(Date startDate, Date endDate, String mediatorId);
    List<PurchaseDTO> applyFilterChainAndReturnPurchases(String name, Date startDate, Date endDate, Boolean unPaidOnly);

    List<Integer> getPricesOnLastPurchase(String vendorId);

    List<ProductWithQuantity> getPurchasesByDates(Date date1, Date date2);

    Tuple getConcatedSerialsAndMaskedPricesById(Integer id);

    PurchaseDTO getPurchaseAndFetchPPsById(Integer id);

    Purchase getPurchaseWithPurchasedProductsById(Integer id);
}
