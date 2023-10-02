package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.entity.Sale;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;

import java.util.Date;
import java.util.List;

public interface SaleService {
    List<SaleDTO> getAllSales();

    SaleDTO getSale(SaleDTO SaleDTO);

    SaleDTO saveSale(SaleDTO SaleDTO);

    void deleteSale(SaleDTO SaleDTO);

    SaleDTO addProductToSale(SaleDTO SaleDTO, PurchasedProductDTO SaledProductDTO);

    List<SaleDTO> getSalesByStartingDate(Date startDate);

    List<SaleDTO> getSalesByEndingDate(Date endDate);

    List<SaleDTO> getSalesByBothDates(Date startDate, Date endDate);
    List<SaleDTO> applyFilterChainAndReturnSales(String name, String currency, Date startDate, Date endDate, Boolean unPaidOnly, String paper, Boolean letaiOnly, Boolean globalGapOnly);
}
