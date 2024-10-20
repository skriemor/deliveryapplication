package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.CompletedPurchaseDTO;
import hu.torma.deliveryapplication.entity.CompletedPurchase;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface CompletedPurchaseService {
    List<CompletedPurchaseDTO> getAllCompletedPurchases();

    CompletedPurchaseDTO getCompletedPurchaseById(Integer id);

    @Transactional
    CompletedPurchase saveCompletedPurchase(CompletedPurchase completedPurchase);

    void deleteCompletedPurchaseById(Integer id);

    List<CompletedPurchaseDTO> getFilteredListOfCPs(String name, Date startDate, Date endDate, String numSerial1, String numSerial2, Boolean notPaidOnly, String paymentMethod);

    List<CompletedPurchaseDTO> getCompletedPurchasesByMediatorIdAndDates(Date startDate, Date endDate, String mediatorId);

    List<ProductWithQuantity> getCpsByDatesAsProductWithQuantities(Date date1, Date date2);

}
