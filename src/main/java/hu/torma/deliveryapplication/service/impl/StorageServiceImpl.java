package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.QuantityDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;
import hu.torma.deliveryapplication.entity.Quantity;
import hu.torma.deliveryapplication.repository.PurchaseRepository;
import hu.torma.deliveryapplication.repository.QuantityRepository;
import hu.torma.deliveryapplication.repository.SaleRepository;
import hu.torma.deliveryapplication.service.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class StorageServiceImpl implements StorageService {
    Logger log = Logger.getLogger("Fos");

    ModelMapper mapper = new ModelMapper();
    @Autowired
    QuantityRepository repo;

    @Autowired
    PurchaseRepository pRepo;

    @Autowired
    SaleRepository sRepo;
    @Override
    public Double getSupplyOf(ProductDTO dto, SaleDTO saleToIgnore) {
        calculateQuantityAmount(saleToIgnore);
        return repo.getAmountOfProduct(dto.getId());
    }

    /**
     * Substract an amount from stored product
     *
     * @param dto product to substract amount from
     * @param qnt amount to substract from product in storage
     * @return true if successful
     */
    @Override
    public Boolean subQuantityOf(ProductDTO dto, Double qnt) {
        Quantity quantity = repo.getQuantityByProductName(dto.getId());
        quantity.setAmount(quantity.getAmount() - qnt);
        repo.save(quantity);
        return true;
    }

    /**
     * Add an amount to stored product
     *
     * @param dto product to add amount to
     * @return true if successful
     */
    @Override
    public Boolean addToQuantity(ProductDTO dto) {
        createQuantityIfNotExists(dto);
        return true;
    }

    public Boolean calculateQuantityAmount(SaleDTO dto) {
        Double amount;
        for (Quantity q : repo.findAll()) {
            amount = 0.0;
            amount += pRepo.findAll().stream()
                    .map(c -> c.getProductList()
                            .stream()
                            .filter(f -> f.getProduct().getId().equals(q.getProduct().getId()))
                            .findFirst().get().getQuantity2())
                    .collect(Collectors.summingDouble(Double::doubleValue));

            amount -= sRepo.findAll().stream().filter(p -> {
                         if (p.getId() == dto.getId()) return false;
                        return true;
                    }
                    )
                    .map(c -> c.getProductList()
                            .stream()
                            .filter(f -> f.getProduct().getId().equals(q.getProduct().getId()))
                            .findFirst().get().getQuantity())
                    .collect(Collectors.summingDouble(Double::doubleValue));


            Quantity qant = repo.findById(q.getId()).get();
            amount = Math.floor(amount * 100) / 100;
            qant.setAmount(amount);
            repo.save(qant);
        }
        return true;
    }
    public Boolean calculateQuantityAmount() {
        Double amount;
        for (Quantity q : repo.findAll()) {
            amount = 0.0;
            amount += pRepo.findAll().stream()
                    .map(c -> c.getProductList()
                            .stream()
                            .filter(f -> f.getProduct().getId().equals(q.getProduct().getId()))
                            .findFirst().get().getQuantity2())
                    .collect(Collectors.summingDouble(Double::doubleValue));

            amount -= sRepo.findAll().stream()
                    .map(c -> c.getProductList()
                            .stream()
                            .filter(f -> f.getProduct().getId().equals(q.getProduct().getId()))
                            .findFirst().get().getQuantity())
                    .collect(Collectors.summingDouble(Double::doubleValue));


            Quantity qant = repo.findById(q.getId()).get();
            amount = Math.floor(amount * 100) / 100;
            qant.setAmount(amount);
            repo.save(qant);
        }
        return true;
    }

    @Override
    public void createQuantityIfNotExists(ProductDTO dto) {
        if (!repo.existsByProductName(dto.getId())) {
            QuantityDTO qDto = new QuantityDTO();
            qDto.setProduct(dto);
            qDto.setAmount(0.0);
            repo.save(mapper.map(qDto, Quantity.class));
        }
    }

    @Override
    public ArrayList<QuantityDTO> getAllQuantities() {
        calculateQuantityAmount();
        return new ArrayList<QuantityDTO>(
                repo.findAll().stream()
                        .map(q -> mapper.map(q, QuantityDTO.class))
                        .toList());
    }
}
