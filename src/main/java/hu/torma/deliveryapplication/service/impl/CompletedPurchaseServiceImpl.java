package hu.torma.deliveryapplication.service.impl;


import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.entity.CompletedPurchase;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import hu.torma.deliveryapplication.repository.CompletedPurchaseRepository;
import hu.torma.deliveryapplication.service.CompletedPurchaseService;
import org.modelmapper.ModelMapper;
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
                repo.findAll().stream().map(
                        c -> mapper.map(c, CompletedPurchaseDTO.class)
                ).toList()
        );
    }

    @Override
    public CompletedPurchaseDTO getCompletedPurchaseById(Integer id) {
        CompletedPurchase cp = repo.findAndFetchRecordsById(id);
        CompletedPurchaseDTO mappedCP = mapper.map(cp, CompletedPurchaseDTO.class);
        return mappedCP;
    }

    @Override
    public List<CompletedPurchaseListingDTO> getCompletedPurchasesForListing() {
        return new ArrayList<>(
                repo.findAllAndFetchRecordsAndPurchases().stream().map(
                        c -> mapper.map(c, CompletedPurchaseListingDTO.class)
                ).toList()
        );
    }

    @Override
    public List<CompletedPurchaseDTO> getAllCompletedPurchasesWithRecords() {
        List<CompletedPurchaseDTO> purchasesWithRecords = new ArrayList<>();
        mapper.map(repo.findAllAndFetchRecords(), purchasesWithRecords);
        return purchasesWithRecords;
    }

    @Override
    public CompletedPurchaseDTO getCompletedPurchase(CompletedPurchaseDTO CompletedPurchaseDTO) {
        return mapper.map(repo.findById(CompletedPurchaseDTO.getId()), CompletedPurchaseDTO.class);
    }

    @Override
    @Transactional
    public CompletedPurchaseDTO saveCompletedPurchase(CompletedPurchaseDTO CompletedPurchaseDTO) {
        if (CompletedPurchaseDTO.getRecords() != null) {
            for (var c : CompletedPurchaseDTO.getRecords())
                c.setCompletedPurchase(CompletedPurchaseDTO);
        }
        var g = mapper.map(repo.save(mapper.map(CompletedPurchaseDTO, CompletedPurchase.class)), CompletedPurchaseDTO.class);
        return g;
    }

    @Override
    @Transactional
    public CompletedPurchaseWithMinimalsDTO saveCompletedPurchase(CompletedPurchaseWithMinimalsDTO dto) {
        if (dto.getRecords() != null) {
            for (var c : dto.getRecords())
                c.setCompletedPurchase(dto.toIdOnly());
        }
        return mapper.map(repo.save(mapper.map(dto, CompletedPurchase.class)), CompletedPurchaseWithMinimalsDTO.class);
    }

    @Override
    public Date getEarliestPurchaseDate(Integer id) {
        return repo.getEarliestPurchaseDate(id).orElseGet(Date::new);
    }

    @Override
    @Transactional
    public void deleteCompletedPurchase(CompletedPurchaseDTO CompletedPurchaseDTO) {
        repo.deleteById(CompletedPurchaseDTO.getId());
    }

    @Override
    public void deleteCompletedPurchaseById(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public List<CompletedPurchaseDTO> getCPsByStartingDate(Date startDate) {
        return new ArrayList<CompletedPurchaseDTO>(
                repo.findAllByReceiptDateAfter(startDate).stream().map(
                        c -> mapper.map(c, CompletedPurchaseDTO.class)
                ).toList()
        );
    }

    @Override
    public List<CompletedPurchaseDTO> getCPsByEndingDate(Date endDate) {
        return new ArrayList<CompletedPurchaseDTO>(
                repo.findAllByReceiptDateBefore(endDate).stream().map(
                        c -> mapper.map(c, CompletedPurchaseDTO.class)
                ).toList()
        );
    }

    @Override
    public List<CompletedPurchaseDTO> getCPsByBothDates(Date startDate, Date endDate) {
        return new ArrayList<CompletedPurchaseDTO>(
                repo.findAllByReceiptDateBetween(startDate, endDate).stream().map(
                        c -> mapper.map(c, CompletedPurchaseDTO.class)
                ).toList()
        );
    }

    @Override
    public List<CompletedPurchaseDTO> getFilteredListOfCPs(String name, Date startDate, Date endDate, String numSerial1, String numSerial2, Boolean notPaidOnly, String paymentMethod) {
        return new ArrayList<CompletedPurchaseDTO>(
                repo.applyFilterChainAndReturnResults(name, startDate, endDate, numSerial1, numSerial2, notPaidOnly, paymentMethod).stream().map(
                        c -> mapper.map(c, CompletedPurchaseDTO.class)
                ).toList()
        );
    }

    @Override
    public List<CompletedPurchaseDTO> getCompletedPurchasesByMediatorIdAndDates(Date startDate, Date endDate, String mediatorId) {
        return new ArrayList<CompletedPurchaseDTO>(
                repo.getCompletedPurchasesByMediatorAndDate(startDate, endDate, mediatorId).stream().map(
                        c -> mapper.map(c, CompletedPurchaseDTO.class)
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
