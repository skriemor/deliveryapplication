package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.entity.Sale;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SaleService {
    List<SaleDTO> getAllSales();

    void saveSale(SaleDTO SaleDTO);

    Sale save(Sale sale);

    void deleteSale(SaleDTO SaleDTO);

    SaleDTO addProductToSale(SaleDTO SaleDTO, PurchasedProductDTO SaledProductDTO);

    List<SaleDTO> getSalesByStartingDate(Date startDate);

    List<SaleDTO> getSalesByEndingDate(Date endDate);

    List<SaleDTO> getSalesByBothDates(Date startDate, Date endDate);
    List<SaleDTO> applyFilterChainAndReturnSales(String name, String currency, Date startDate, Date endDate, Boolean unPaidOnly, String paper, Boolean letaiOnly, Boolean globalGapOnly);

    List<ProductWithQuantity> getSalesByDates(Date date1, Date date2);

    List<ProductWithQuantity> getOfficialSalesByDates(Date date1, Date date2);

    Optional<SaleDTO> getSaleById(int id);

    Optional<Sale> getSaleEntityById(int id);
}
