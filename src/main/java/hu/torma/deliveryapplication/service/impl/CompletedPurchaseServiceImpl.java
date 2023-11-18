package hu.torma.deliveryapplication.service.impl;


import hu.torma.deliveryapplication.DTO.CompletedPurchaseDTO;
import hu.torma.deliveryapplication.entity.CompletedPurchase;
import hu.torma.deliveryapplication.repository.CompletedPurchaseRepository;
import hu.torma.deliveryapplication.service.CompletedPurchaseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CompletedPurchaseServiceImpl implements CompletedPurchaseService {
    Logger logger = Logger.getLogger("PRODUCTLIST");
    @Autowired
    CompletedPurchaseRepository repo;
    ModelMapper mapper = new ModelMapper();

    @Override
    public List<CompletedPurchaseDTO> getAllCompletedPurchases() {
        return new ArrayList<CompletedPurchaseDTO>(
                repo.findAll().stream().map(
                        c -> mapper.map(c, CompletedPurchaseDTO.class)
                ).toList()
        );
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
    public void deleteCompletedPurchase(CompletedPurchaseDTO CompletedPurchaseDTO) {
        repo.deleteById(CompletedPurchaseDTO.getId());
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
    public List<CompletedPurchaseDTO> getFilteredListOfCPs(String name, Date startDate, Date endDate, String numSerial1,String numSerial2, Boolean notPaidOnly, String paymentMethod) {
        return new ArrayList<CompletedPurchaseDTO>(
                repo.applyFilterChainAndReturnResults(name,startDate, endDate, numSerial1,numSerial2, notPaidOnly, paymentMethod).stream().map(
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
        );    }

}
