package hu.torma.deliveryapplication.service.impl;


import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.repository.PurchaseRepository;
import hu.torma.deliveryapplication.service.PurchaseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    Logger logger = Logger.getLogger("PRODUCTLIST");
    @Autowired
    PurchaseRepository repo;
    ModelMapper mapper = new ModelMapper();

    @Override
    public List<PurchaseDTO> getAllPurchases() {
        return new ArrayList<PurchaseDTO>(
                repo.findAll().stream().map(
                        c -> mapper.map(c, PurchaseDTO.class)
                ).toList()
        );
    }

    @Override
    public PurchaseDTO getPurchase(PurchaseDTO PurchaseDTO) {
        return mapper.map(repo.findById(PurchaseDTO.getId()), PurchaseDTO.class);
    }

    @Override
    @Transactional
    public PurchaseDTO savePurchase(PurchaseDTO PurchaseDTO) {
        logger.info("Save was called");
        for (var v : PurchaseDTO.getProductList())
            v.setPurchase(PurchaseDTO); //to make relations work by assigning purchase to each of purchased products' ends
        var g = mapper.map(repo.save(mapper.map(PurchaseDTO, Purchase.class)), PurchaseDTO.class);
        return g;
    }

    @Override
    @Transactional
    public void deletePurchase(PurchaseDTO PurchaseDTO) {
        repo.deleteById(PurchaseDTO.getId());
    }

    @Override
    public PurchaseDTO addProductToPurchase(PurchaseDTO PurchaseDTO, PurchasedProductDTO PurchasedProductDTO) {
        PurchaseDTO.getProductList().add(PurchasedProductDTO);
        return PurchaseDTO;
    }

    @Override
    public PurchaseDTO getPurchaseById(Integer id) {
        var g = mapper.map(repo.findById(id).orElseGet(() -> {
            Purchase p = new Purchase();
            p.setRemainingPrice(0.0);
            p.setTotalPrice(0.0);
            p.setProductList(null);
            p.setId(-1);
            return p;
        }), PurchaseDTO.class);
        //logger.info("found " + g.getId()+ " <-- id,    size of completeddtolist: "+g.getPurchaseDTOS().size());
        return g;
    }

    @Override
    public List<PurchaseDTO> getPsByStartingDate(Date startDate) {
        return new ArrayList<PurchaseDTO>(
                repo.findAllByReceiptDateAfter(startDate).stream().map(
                        c -> mapper.map(c, PurchaseDTO.class)
                ).toList()
        );
    }

    @Override
    public List<PurchaseDTO> getPsByEndingDate(Date endDate) {
        return new ArrayList<PurchaseDTO>(
                repo.findAllByReceiptDateBefore(endDate).stream().map(
                        c -> mapper.map(c, PurchaseDTO.class)
                ).toList()
        );
    }

    @Override
    public List<PurchaseDTO> getPsByBothDates(Date startDate, Date endDate) {
        return new ArrayList<PurchaseDTO>(
                repo.findAllByReceiptDateBetween(startDate, endDate).stream().map(
                        c -> mapper.map(c, PurchaseDTO.class)
                ).toList()
        );
    }

    @Override
    public List<PurchaseDTO> applyFilterChainAndReturnPurchases(String name, Date startDate, Date endDate, Boolean unPaidOnly) {
        return new ArrayList<PurchaseDTO>(
                repo.applyFilterChainAndReturnPurchases(name, startDate, endDate, unPaidOnly).stream().map(
                        c -> mapper.map(c, PurchaseDTO.class)
                ).toList()
        );
    }
}
