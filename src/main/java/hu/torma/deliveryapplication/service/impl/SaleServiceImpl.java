package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.SaleDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;
import hu.torma.deliveryapplication.entity.Sale;
import hu.torma.deliveryapplication.repository.SaleRepository;
import hu.torma.deliveryapplication.service.SaleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class SaleServiceImpl implements SaleService {
    Logger logger = Logger.getLogger("SaleLogger");
    @Autowired
    SaleRepository repo;
    ModelMapper mapper = new ModelMapper();

    @Override
    public List<SaleDTO> getAllSales() {
        return new ArrayList<SaleDTO>(
                repo.findAll().stream().map(
                        c -> mapper.map(c, SaleDTO.class)
                ).toList()
        );
    }

    @Override
    public SaleDTO getSale(SaleDTO SaleDTO) {
        return mapper.map(repo.findById(SaleDTO.getId()), SaleDTO.class);
    }

    @Override
    @Transactional
    public SaleDTO saveSale(SaleDTO SaleDTO) {
        logger.info("Save was called");
        for (var v : SaleDTO.getProductList())
            v.setSale(SaleDTO); //to make relations work by assigning purchase to each of purchased products' ends
        var g = mapper.map(repo.save(mapper.map(SaleDTO, Sale.class)), SaleDTO.class);
        return g;
    }

    @Override
    @Transactional
    public void deleteSale(SaleDTO SaleDTO) {
        repo.deleteById(SaleDTO.getId());
    }

    @Override
    public SaleDTO addProductToSale(SaleDTO SaleDTO, PurchasedProductDTO SaledProductDTO) {
        SaleDTO.getProductList().add(SaledProductDTO);
        return SaleDTO;
    }

    @Override
    public List<SaleDTO> getSalesByStartingDate(Date startDate) {
        return new ArrayList<SaleDTO>(
                repo.findAllByReceiptDateAfter(startDate).stream().map(
                        c -> mapper.map(c, SaleDTO.class)
                ).toList()
        );
    }

    @Override
    public List<SaleDTO> getSalesByEndingDate(Date endDate) {
        return new ArrayList<SaleDTO>(
                repo.findAllByReceiptDateBefore(endDate).stream().map(
                        c -> mapper.map(c, SaleDTO.class)
                ).toList()
        );
    }

    @Override
    public List<SaleDTO> getSalesByBothDates(Date startDate, Date endDate) {
        return new ArrayList<SaleDTO>(
                repo.findAllByReceiptDateBetween(startDate, endDate).stream().map(
                        c -> mapper.map(c, SaleDTO.class)
                ).toList()
        );
    }

    @Override
    public List<SaleDTO> applyFilterChainAndReturnSales(String name, String currency, Date startDate, Date endDate, Boolean unPaidOnly, String paper, Boolean letaiOnly, Boolean globalGapOnly) {
        return new ArrayList<SaleDTO>(
                repo.applyFilterChainAndReturnSales(name,currency,startDate,endDate,unPaidOnly,paper,letaiOnly,globalGapOnly).stream().map(
                        c -> mapper.map(c, SaleDTO.class)
                ).toList()
        );
    }
}
