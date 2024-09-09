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

    List<CompletedPurchaseDTO> getCompletedPurchasesForListing();
    List<CompletedPurchaseDTO> getAllCompletedPurchasesWithRecords();

    CompletedPurchaseDTO getCompletedPurchase(CompletedPurchaseDTO CompletedPurchaseDTO);

    @Transactional
    CompletedPurchase saveCompletedPurchase(CompletedPurchase completedPurchase);

    Date getEarliestPurchaseDate(Integer id);

    void deleteCompletedPurchase(CompletedPurchaseDTO CompletedPurchaseDTO);
    void deleteCompletedPurchaseById(Integer id);

    List<CompletedPurchaseDTO> getCPsByStartingDate(Date startDate);

    List<CompletedPurchaseDTO> getCPsByEndingDate(Date endDate);

    List<CompletedPurchaseDTO> getCPsByBothDates(Date startDate, Date endDate);

    List<CompletedPurchaseDTO> getFilteredListOfCPs(String name, Date startDate, Date endDate, String numSerial1,String numSerial2,Boolean notPaidOnly, String paymentMethod);

    List<CompletedPurchaseDTO> getCompletedPurchasesByMediatorIdAndDates(Date startDate, Date endDate, String mediatorId);

    List<ProductWithQuantity> getCpsByDatesAsProductWithQuantities(Date date1, Date date2);
}
