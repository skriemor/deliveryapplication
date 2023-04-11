package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.QuantityDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;

import java.util.ArrayList;

public interface StorageService {
    Double getSupplyOf(ProductDTO dto, SaleDTO saleDTO);
    Boolean subQuantityOf(ProductDTO dto, Integer qnt);

    Boolean addToQuantity(ProductDTO dto);

    void createQuantityIfNotExists(ProductDTO dto);

    ArrayList<QuantityDTO> getAllQuantities();
}
