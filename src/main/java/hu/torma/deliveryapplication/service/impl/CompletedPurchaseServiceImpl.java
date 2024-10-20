package hu.torma.deliveryapplication.service.impl;


import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.entity.CompletedPurchase;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import hu.torma.deliveryapplication.repository.CompletedPurchaseRepository;
import hu.torma.deliveryapplication.service.CompletedPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CompletedPurchaseServiceImpl implements CompletedPurchaseService {
    Logger logger = Logger.getLogger("PRODUCTLIST");
    @Autowired
    CompletedPurchaseRepository repo;

    @Override
    public List<CompletedPurchaseDTO> getAllCompletedPurchases() {
        return new ArrayList<CompletedPurchaseDTO>(
                repo.findAllForCpListing().stream().map(
                        cp -> cp.toDTO(true)
                ).toList()
        );
    }

    @Override
    public CompletedPurchaseDTO getCompletedPurchaseById(Integer id) {
        CompletedPurchase cp = repo.findAndFetchRecordsById(id);
        return cp.toDTO(true);
    }

    @Transactional
    @Override
    public CompletedPurchase saveCompletedPurchase(CompletedPurchase completedPurchase) {
        if (completedPurchase.getRecords() != null) {
            for (var c : completedPurchase.getRecords())
                c.setCompletedPurchase(completedPurchase);
        }
        CompletedPurchase saved = repo.save(completedPurchase);
        repo.flush();
        return saved;
    }

    @Override
    public void deleteCompletedPurchaseById(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public List<CompletedPurchaseDTO> getFilteredListOfCPs(String taxId, Date startDate, Date endDate, String numSerial1, String numSerial2, Boolean notPaidOnly, String paymentMethod) {
        return new ArrayList<>(
                repo.applyFilterChainAndReturnResults(taxId, startDate, endDate, numSerial1, numSerial2, notPaidOnly, paymentMethod).stream().map(
                        cp -> cp.toDTO(true)
                ).toList()
        );
    }

    @Override
    public List<CompletedPurchaseDTO> getCompletedPurchasesByMediatorIdAndDates(Date startDate, Date endDate, String mediatorId) {
        return new ArrayList<CompletedPurchaseDTO>(
                repo.getCompletedPurchasesByMediatorAndDate(startDate, endDate, mediatorId).stream().map(
                        cp -> cp.toDTO(true)
                ).toList()
        );
    }

    @Override
    public List<ProductWithQuantity> getCpsByDatesAsProductWithQuantities(Date date1, Date date2) {
        List<ProductWithQuantity> cps = repo.getCpsByDatesAsProductWithQuantities(date1, date2);
        for (ProductWithQuantity pq : cps) {
            if (pq.getQuantity() == null) {
                pq.setQuantity(0);
            }
        }
        return cps;

    }

}
