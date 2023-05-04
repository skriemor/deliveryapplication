package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.DisplayUnit;
import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;

import java.util.ArrayList;

public interface StorageService {

    Integer getSupplyOf(ProductDTO dto, SaleDTO saleToIgnore);

    ArrayList<DisplayUnit> getDisplayUnits();
}
