package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.DisplayUnit;
import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;
import hu.torma.deliveryapplication.entity.PurchasedProduct;
import hu.torma.deliveryapplication.primefaces.sumutils.SaleSumPojo;
import hu.torma.deliveryapplication.repository.CompletionRecordRepository;
import hu.torma.deliveryapplication.repository.PurchaseRepository;
import hu.torma.deliveryapplication.repository.PurchasedProductRepository;
import hu.torma.deliveryapplication.repository.SaleRepository;
import hu.torma.deliveryapplication.service.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class StorageServiceImpl implements StorageService {

    List<String> prodStrings = Arrays.asList("I.OSZTÁLYÚ", "II.OSZTÁLYÚ", "III.OSZTÁLYÚ", "IV.OSZTÁLYÚ", "GYÖKÉR", "IPARI");
    Logger log = Logger.getLogger("Fos");

    ModelMapper mapper = new ModelMapper();

    @Autowired
    private CompletionRecordRepository cRepo;
    @Autowired
    PurchaseRepository pRepo;

    @Autowired
    PurchasedProductRepository ppRepo;
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
        if (tmp.size() > 0) {
            amount -= tmp.stream().map(PurchasedProduct::getQuantity).mapToInt(Integer::intValue).sum();
        }


        return amount;
    }

    @Override
    public List<DisplayUnit> getDisplayUnits() {
        return ppRepo.getRealStorage();
    }



    @Override
    public List<DisplayUnit> getDisplayUnitsWithDates(Date date1, Date date2) {
        List<DisplayUnit> tmp = ppRepo.getRealStorageWithDates(date1, date2);
        List<String> notNulls = tmp.stream().map(DisplayUnit::getProductName).toList();
        List<DisplayUnit> actual = new ArrayList<DisplayUnit>();

        int it1 = 0, it2 = 0;
        while (it1 < 6 && it2 < 6) {
            if (tmp.size() < 1 || tmp.size() <= it2 ||!prodStrings.get(it1).equals(tmp.get(it2).getProductName())) {
                actual.add(new DisplayUnit(prodStrings.get(it1), 0));
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
    public SaleSumPojo getFictionalStorageAsObject() {
        return ppRepo.getFictionalStorage();
    }

    @Override
    public SaleSumPojo getFictionalStorageAsObjectDates(Date date1, Date date2) {
        return ppRepo.getFictionalStorageWithDates(date1, date2);
    }


}
