package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.DisplayUnit;
import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;
import hu.torma.deliveryapplication.entity.PurchasedProduct;
import hu.torma.deliveryapplication.primefaces.sumutils.SaleSumPojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface StorageService {

    Integer getSupplyOf(ProductDTO dto, SaleDTO saleToIgnore);

    List<DisplayUnit> getDisplayUnits();
    SaleSumPojo getFictionalStorageAsObject();
    List<DisplayUnit> getDisplayUnitsWithDates(Date date1, Date date2);

    SaleSumPojo getFictionalStorageAsObjectDates(Date date1, Date date2);
}
