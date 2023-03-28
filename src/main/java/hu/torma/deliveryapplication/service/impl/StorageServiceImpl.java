package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.QuantityDTO;
import hu.torma.deliveryapplication.entity.Quantity;
import hu.torma.deliveryapplication.repository.QuantityRepository;
import hu.torma.deliveryapplication.service.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StorageServiceImpl implements StorageService {

    ModelMapper mapper = new ModelMapper();
    @Autowired
    QuantityRepository repo;

    @Override
    public Double getSupplyOf(ProductDTO dto) {
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
     * @param qnt amount to add to product in storage
     * @return true if successful
     */
    @Override
    public Boolean addToQuantity(ProductDTO dto, Double qnt) {
        createQuantityIfNotExists(dto);
        Quantity quantity = repo.getQuantityByProductName(dto.getId());
        quantity.setAmount(quantity.getAmount() + qnt);
        repo.save(quantity);
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
        return new ArrayList<QuantityDTO>(
                repo.findAll().stream()
                        .map(q -> mapper.map(q, QuantityDTO.class))
                        .toList());
    }
}
