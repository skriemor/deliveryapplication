package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import hu.torma.deliveryapplication.repository.SaleRepository;
import hu.torma.deliveryapplication.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class SaleServiceImpl implements SaleService {
    Logger logger = Logger.getLogger("SaleLogger");
    @Autowired
    SaleRepository repo;

    @Override
    public List<SaleDTO> getAllSales() {
        return new ArrayList<SaleDTO>(
                repo.findAll().stream().map(
                       sale -> sale.toDTO(true, true)
                ).toList()
        );
    }

    @Override
    public SaleDTO getSale(SaleDTO saledto) {
        return repo.findById(saledto.getId()).map(sale -> sale.toDTO(true, true)).orElse(null);
    }

    @Override
    @Transactional
    public SaleDTO saveSale(SaleDTO dto) {
        logger.info("Save was called");
        for (var v : dto.getProductList())
            v.setSale(dto); //to make relations work by assigning purchase to each of purchased products' ends
        return repo.save(dto.toEntity(true, true)).toDTO(true, true);
    }

    @Override
    @Transactional
    public void deleteSale(SaleDTO SaleDTO) {
        repo.deleteById(SaleDTO.getId());
    }

    @Override
    public SaleDTO addProductToSale(SaleDTO SaleDTO, PurchasedProductDTO saledProductDTO) {
        SaleDTO.getProductList().add(saledProductDTO);
        return SaleDTO;
    }

    @Override
    public List<SaleDTO> getSalesByStartingDate(Date startDate) {
        return new ArrayList<SaleDTO>(
                repo.findAllByReceiptDateAfter(startDate).stream().map(
                        sale -> sale.toDTO(true, true)
                ).toList()
        );
    }

    @Override
    public List<SaleDTO> getSalesByEndingDate(Date endDate) {
        return new ArrayList<SaleDTO>(
                repo.findAllByReceiptDateBefore(endDate).stream().map(
                        sale -> sale.toDTO(true, true)
                ).toList()
        );
    }

    @Override
    public List<SaleDTO> getSalesByBothDates(Date startDate, Date endDate) {
        return new ArrayList<SaleDTO>(
                repo.findAllByReceiptDateBetween(startDate, endDate).stream().map(
                        sale -> sale.toDTO(true, true)
                ).toList()
        );
    }

    @Override
    public List<SaleDTO> applyFilterChainAndReturnSales(String name, String currency, Date startDate, Date endDate, Boolean unPaidOnly, String paper, Boolean letaiOnly, Boolean globalGapOnly) {
        return new ArrayList<SaleDTO>(
                repo.applyFilterChainAndReturnSales(name, currency, startDate, endDate, unPaidOnly, paper, letaiOnly, globalGapOnly).stream().map(
                        sale -> sale.toDTO(true, true)
                ).toList()
        );
    }


    List<String> prodStrings = Arrays.asList("I.OSZTÁLYÚ", "II.OSZTÁLYÚ", "III.OSZTÁLYÚ", "IV.OSZTÁLYÚ", "GYÖKÉR", "IPARI");

    public List<ProductWithQuantity> getGeneralizedQuantities(List<ProductWithQuantity> tmp) {
        List<ProductWithQuantity> actual = new ArrayList<>();
        int it1 = 0, it2 = 0;
        while (it1 < 6 && it2 < 6) {
            if (tmp.isEmpty() || tmp.size() <= it2 || !prodStrings.get(it1).equals(tmp.get(it2).getProduct())) {
                actual.add(new ProductWithQuantity(prodStrings.get(it1), 0));
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
    public List<ProductWithQuantity> getSalesByDates(Date date1, Date date2) {
        List<ProductWithQuantity> tmp = repo.getProductsWithQuantitiesByDates(date1, date2);
        return getGeneralizedQuantities(tmp);
    }

    @Override
    public List<ProductWithQuantity> getOfficialSalesByDates(Date date1, Date date2) {
        List<ProductWithQuantity> tmp = repo.getOfficialProductsWithQuantitiesByDates(date1, date2);
        return getGeneralizedQuantities(tmp);
    }
}
