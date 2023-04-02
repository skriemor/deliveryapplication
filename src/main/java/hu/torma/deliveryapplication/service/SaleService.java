package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;

import java.util.List;

public interface SaleService {
    List<SaleDTO> getAllSales();

    SaleDTO getSale(SaleDTO SaleDTO);

    SaleDTO saveSale(SaleDTO SaleDTO);

    void deleteSale(SaleDTO SaleDTO);

    SaleDTO addProductToSale(SaleDTO SaleDTO, PurchasedProductDTO SaledProductDTO);
}
