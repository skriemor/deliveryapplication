package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.DisplayUnit;
import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;
import hu.torma.deliveryapplication.entity.CompletionRecord;
import hu.torma.deliveryapplication.entity.PurchasedProduct;
import hu.torma.deliveryapplication.repository.CompletionRecordRepository;
import hu.torma.deliveryapplication.repository.PurchaseRepository;
import hu.torma.deliveryapplication.repository.SaleRepository;
import hu.torma.deliveryapplication.service.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class StorageServiceImpl implements StorageService {
    Logger log = Logger.getLogger("Fos");

    ModelMapper mapper = new ModelMapper();

    @Autowired
    private CompletionRecordRepository cRepo;
    @Autowired
    PurchaseRepository pRepo;

    @Autowired
    SaleRepository sRepo;


    @Override
    public Integer getSupplyOf(ProductDTO dto, SaleDTO saleToIgnore) {
        int amount = 0;
        amount = pRepo.findAll().stream().flatMap(c -> c.getProductList().stream())
                .filter(f -> f.getProduct().getId().equals(dto.getId()))
                .map(PurchasedProduct::getQuantity2).mapToInt(Integer::intValue).sum();

        var tmp = sRepo.findAll().stream()
                .filter(g -> saleToIgnore.getId() != g.getId())
                .flatMap(c -> c.getProductList().stream())
                .filter(f -> f.getProduct().getId().equals(dto.getId()))
                .toList();
        if (tmp.size()>0) {
            amount -= tmp.stream().map(PurchasedProduct::getQuantity).mapToInt(Integer::intValue).sum();
        }




        return amount;
    }

    private List<String> prodList = Arrays.asList("I.OSZTÁLYÚ", "II.OSZTÁLYÚ", "III.OSZTÁLYÚ", "IV.OSZTÁLYÚ","GYÖKÉR", "IPARI");
    List<CompletionRecord> records;

    @Transactional
    @Override
    public ArrayList<DisplayUnit> getDisplayUnits() {
        records = cRepo.findAll();
        int amount = 0, verifiedAmount = 0;
        ArrayList<DisplayUnit> list = new ArrayList<>();
        var tempPList = pRepo.findAll().stream().flatMap(c -> c.getProductList().stream()).toList();
        var tempSList = sRepo.findAll().stream().flatMap(c -> c.getProductList().stream()).toList();
        var tempSListVerified = sRepo.findAll().stream().filter(a -> a.getBuyer().getPaper().equals("Igen")).flatMap(c -> c.getProductList().stream()).toList();
        for (var p : prodList) {
            amount = 0;
            verifiedAmount = 0;
            for (var a : tempPList) {
                if (p.equals(a.getProduct().getId())) {
                    amount += a.getQuantity2();
                    //verifiedAmount += a.getQuantity2() - a.getActual();
                }
            }
            for (var a : tempSList) {
                if (p.equals(a.getProduct().getId())) {
                    amount -= a.getQuantity();
                    //verifiedAmount -= a.getActual();
                }
            }
            for (var a : tempSListVerified) {
                if (p.equals(a.getProduct().getId())) {
                    //amount -= a.getQuantity();
                    verifiedAmount -= a.getQuantity();
                }
            }

            switch (p) {
                case "I.OSZTÁLYÚ" ->
                        verifiedAmount += records.stream().map(CompletionRecord::getOne).mapToInt(Integer::intValue).sum();
                case "II.OSZTÁLYÚ" ->
                        verifiedAmount += records.stream().map(CompletionRecord::getTwo).mapToInt(Integer::intValue).sum();
                case "III.OSZTÁLYÚ" ->
                        verifiedAmount += records.stream().map(CompletionRecord::getThree).mapToInt(Integer::intValue).sum();
                case "IV.OSZTÁLYÚ" ->
                        verifiedAmount += records.stream().map(CompletionRecord::getFour).mapToInt(Integer::intValue).sum();
                case "GYÖKÉR" ->
                        verifiedAmount += records.stream().map(CompletionRecord::getFive).mapToInt(Integer::intValue).sum();
                case "IPARI" ->
                        verifiedAmount += records.stream().map(CompletionRecord::getSix).mapToInt(Integer::intValue).sum();
            }
            list.add(new DisplayUnit(amount, verifiedAmount, p));
        }

        return list;
    }


}
