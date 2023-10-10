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

import java.util.Date;
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
        return ppRepo.getRealStorageWithDates(date1,date2);
    }
    @Override
    public SaleSumPojo getFictionalStorageAsObject() {
        return ppRepo.getFictionalStorage();
    }

    @Override
    public SaleSumPojo getFictionalStorageAsObjectDates(Date date1, Date date2) {
        return ppRepo.getFictionalStorageWithDates(date1,date2);
    }


}
