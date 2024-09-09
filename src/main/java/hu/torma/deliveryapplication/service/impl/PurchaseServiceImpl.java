package hu.torma.deliveryapplication.service.impl;


import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import hu.torma.deliveryapplication.repository.PurchaseRepository;
import hu.torma.deliveryapplication.service.PurchaseService;
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
        return repo.findAndFetchPPsById(id).map(purchase -> purchase.toDTO(false, false, true)).orElse(null);
    }

    @Override
    public List<PurchaseDTO> getAllPurchases() {
        return new ArrayList<>(repo.findAll().stream().map(purchase -> purchase.toDTO(true, true, false)).toList());
    }

    @Override
    public PurchaseDTO getPurchase(PurchaseDTO dto) {
        return repo.findById(dto.getId()).map(purchase -> purchase.toDTO(true, true, false)).orElse(null);
    }

    @Override
    @Transactional
    public PurchaseDTO savePurchase(PurchaseDTO purchaseDto) {
        logger.info("Save was called");
        for (var v : purchaseDto.getProductList())
            v.setPurchase(purchaseDto); //to make relations work by assigning purchase to each of purchased products' ends
        return repo.save(purchaseDto.toEntity(true, true)).toDTO(true,true, true);
    }

    @Override
    public Purchase savePurchase(Purchase purchase) {
        return repo.save(purchase);
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
        return repo.findAndFetchPPsById(id).orElseGet(() -> {
            Purchase p = new Purchase();
            p.setRemainingPrice(0.0);
            p.setTotalPrice(0.0);
            p.setProductList(null);
            p.setId(-1);
            return p;
        }).toDTO(true, true, true);
    }

    @Override
    public Purchase getPurchaseEntityById(Integer id) {
        return repo.findPurchaseFetchAllById(id).orElse(null);
    }

    @Override
    public PurchaseDTO getPurchaseForSelectionById(Integer id) {
        return repo.findAndFetchPPsById(id).map(purchase -> purchase.toDTO(true, true, true)).orElseThrow(() -> new RuntimeException("id: " + id + " not found in db"));
    }

    @Override
    public List<PurchaseDTO> getAllPurchasesForSelection() {
        return new ArrayList<>(repo.findAll().stream().map(purchase -> purchase.toDTO(true, true, true)).toList());
    }

    @Override
    public List<PurchaseDTO> getPsByStartingDate(Date startDate) {
        return new ArrayList<PurchaseDTO>(
                repo.findAllByReceiptDateAfter(startDate).stream().map(
                        purchase -> purchase.toDTO(true, true, true)
                ).toList()
        );
    }

    @Override
    public List<PurchaseDTO> getPsByEndingDate(Date endDate) {
        return new ArrayList<PurchaseDTO>(
                repo.findAllByReceiptDateBefore(endDate).stream().map(
                        purchase -> purchase.toDTO(true, true, true)
                ).toList()
        );
    }

    @Override
    public List<PurchaseDTO> getPsByBothDates(Date startDate, Date endDate) {
        return new ArrayList<PurchaseDTO>(
                repo.findAllByReceiptDateBetween(startDate, endDate).stream().map(
                        purchase -> purchase.toDTO(true, true, true)
                ).toList()
        );
    }

    @Override
    public List<PurchaseDTO> getPurchasesByMediatorIdAndDates(Date startDate, Date endDate, String mediatorId) {
        return new ArrayList<PurchaseDTO>(
                repo.getPurchasesByMediatorAndDate(startDate, endDate, mediatorId).stream().map(
                        purchase -> purchase.toDTO(true, true, true)
                ).toList()
        );
    }

    @Override
    public List<PurchaseDTO> applyFilterChainAndReturnPurchases(String name, Date startDate, Date endDate, Boolean unPaidOnly) {
        return new ArrayList<PurchaseDTO>(
                repo.applyFilterChainAndReturnPurchases(name, startDate, endDate, unPaidOnly).stream().map(
                        purchase -> purchase.toDTO(true, true, true)
                ).toList()
        );
    }

    @Override
    public List<Integer> getPricesOnLastPurchase(String vendorId) {
        List<Integer> ints = repo.getLastPurchasePricesByVendorTaxId(vendorId);
        return !ints.isEmpty() ? ints : Arrays.asList(0, 0, 0, 0, 0, 0);
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
    public List<PurchaseDTO> getAllPurchasesAndFetchPPs() {
        return new ArrayList<>(repo.getAllAndFetchPPs().stream().map(purchase -> purchase.toDTO(true, true, true)).toList());
    }

    @Override
    public List<PurchaseDTO> getAllPurchasesForListing() {
        return new ArrayList<>(repo.findAll().stream().map(purchase -> purchase.toDTO(true, true, true)).toList());
    }

    @Override
    public PurchaseDTO getPurchaseAndFetchPPsById(Integer id) {
        return repo.findAndFetchPPsById(id).map(purchase -> purchase.toDTO(true, true, true)).orElseThrow(() -> new RuntimeException("id: " + id + " not found in db"));
    }
}
