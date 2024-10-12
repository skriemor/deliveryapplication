package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.repository.ProductRepository;
import hu.torma.deliveryapplication.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository repo;

    @Override
    public List<ProductDTO> getAllProducts() {
        return new ArrayList<>(
                repo.findAllFetchAll().stream().map(
                        prod -> prod.toDTO(true)
                ).toList()
        );
    }

    @Override
    public ProductDTO getProduct(ProductDTO ProductDTO) {
        return repo.findById(ProductDTO.getId()).map(                        prod -> prod.toDTO(true)
        ).orElse(null);
    }

    @Override
    @Transactional
    public ProductDTO saveProduct(ProductDTO dto) {
        return repo.save(dto.toEntity(true)).toDTO(true);
    }

    @Override
    @Transactional
    public void deleteProduct(ProductDTO ProductDTO) {
        repo.deleteById(ProductDTO.getId());
    }

    @Override
    public ProductDTO getProductById(String s) {
        return repo.findById(s).map(prod -> prod.toDTO(true)).orElse(null);
    }

    @Override
    public Boolean exists(String s) {
        return repo.existsById(s);
    }
}
