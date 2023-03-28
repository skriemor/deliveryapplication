package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.QuantityDTO;

import java.util.ArrayList;

public interface StorageService {
    Double getSupplyOf(ProductDTO dto);
    Boolean subQuantityOf(ProductDTO dto, Double qnt);

    Boolean addToQuantity(ProductDTO dto, Double qnt);

    void createQuantityIfNotExists(ProductDTO dto);

    ArrayList<QuantityDTO> getAllQuantities();
}
