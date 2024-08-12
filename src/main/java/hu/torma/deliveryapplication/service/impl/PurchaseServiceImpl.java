package hu.torma.deliveryapplication.service.impl;


import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import hu.torma.deliveryapplication.repository.PurchaseRepository;
import hu.torma.deliveryapplication.service.PurchaseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    Logger logger = Logger.getLogger("PRODUCTLIST");
    @Autowired
    PurchaseRepository repo;

    @Override
    public PurchaseDTO getRecordlessPurchaseById(Integer id) {
        return mapper.map(repo.findAndFetchPPsById(id), PurchaseDTO.class);
    }

    @Override
    public List<PurchaseDTO> getAllPurchases() {
        return new ArrayList<>(repo.findAll().stream().map(c -> mapper.map(c, PurchaseDTO.class)).toList());
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
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public PurchaseWithoutRecordsDTO savePurchase(PurchaseWithoutRecordsDTO dto) {
        logger.info("Save was called");
        for (var v : dto.getProductList())
            v.setPurchase(dto); //to make relations work by assigning purchase to each of purchased products' ends
        var g = mapper.map(repo.save(mapper.map(dto, Purchase.class)), PurchaseWithoutRecordsDTO.class);
        return g;
    }


    @Override
    @Transactional
    public void deletePurchase(PurchaseDTO PurchaseDTO) {
        repo.deleteById(PurchaseDTO.getId());
    }

    @Override
    @Transactional
    public void deletePurchase(PurchaseWithoutRecordsDTO dto) {
        repo.deleteById(dto.getId());
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
    public PurchaseDTO getPurchaseForSelectionById(Integer id) {
        return mapper.map(repo.findById(id), PurchaseDTO.class);
    }

    @Override
    public List<PurchaseDTO> getAllPurchasesForSelection() {
        return new ArrayList<>(repo.findAll().stream().map(purchase -> mapper.map(purchase, PurchaseDTO.class)).toList());
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
    public List<PurchaseDTO> getPurchasesByMediatorIdAndDates(Date startDate, Date endDate, String mediatorId) {
        return new ArrayList<PurchaseDTO>(
                repo.getPurchasesByMediatorAndDate(startDate, endDate, mediatorId).stream().map(
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

    @Override
    public List<Integer> getPricesOnLastPurchase(String vendorId) {
        List<Integer> ints = repo.getLastPurchasePricesByVendorTaxId(vendorId);
        return ints.size() > 0 ? ints : Arrays.asList(0, 0, 0, 0, 0, 0);
    }

    List<String> prodStrings = Arrays.asList("I.OSZTÁLYÚ", "II.OSZTÁLYÚ", "III.OSZTÁLYÚ", "IV.OSZTÁLYÚ", "GYÖKÉR", "IPARI");

    @Override
    public List<ProductWithQuantity> getPurchasesByDates(Date date1, Date date2) {
        List<ProductWithQuantity> tmp = repo.getProductsWithQuantitiesByDates(date1, date2);
        List<ProductWithQuantity> actual = new ArrayList<>();

        /**
         * Transpone an unentire list to an entire list that contains all possible products in the correct order
         */
        int it1 = 0, it2 = 0;
        while (it1 < 6 && it2 < 6) {
            if (tmp.size() < 1 || tmp.size() <= it2 || !prodStrings.get(it1).equals(tmp.get(it2).getProduct())) {
                actual.add(new ProductWithQuantity(prodStrings.get(it1), 0));
                it1++;
            } else {
                actual.add(tmp.get(it2));
                it1++;
                it2++;
            }
        }
        return actual;
    }

    @Override
    public Tuple getConcatedSerialsAndMaskedPricesById(Integer id) {
        return repo.getConcatedSerialsAndMaskedPricesById(id);
    }

    @Override
    public List<PurchaseWithoutRecordsDTO> getAllPurchasesAndFetchPPs() {
        return new ArrayList<>(repo.getAllAndFetchPPs().stream().map(purchase -> mapper.map(purchase, PurchaseWithoutRecordsDTO.class)).toList());
    }

    @Override
    public List<PurchaseMinimalDTO> getAllPurchasesForListing() {
        return new ArrayList<>(repo.findAll().stream().map(purchase -> mapper.map(purchase, PurchaseMinimalDTO.class)).toList());
    }

    @Override
    public PurchaseWithoutRecordsDTO getPurchaseAndFetchPPsById(Integer id) {
        return mapper.map(repo.findAndFetchPPsById(id), PurchaseWithoutRecordsDTO.class);
    }
}
