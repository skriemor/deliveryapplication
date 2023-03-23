package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.entity.Product;
import hu.torma.deliveryapplication.repository.ProductRepository;
import hu.torma.deliveryapplication.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository repo;
    ModelMapper mapper = new ModelMapper();

    @Override
    public List<ProductDTO> getAllProducts() {
        return new ArrayList<ProductDTO>(
                repo.findAll().stream().map(
                        c -> mapper.map(c, ProductDTO.class)
                ).toList()
        );
    }

    @Override
    public ProductDTO getProduct(ProductDTO ProductDTO) {
        return mapper.map(repo.findById(ProductDTO.getId()), ProductDTO.class);
    }

    @Override
    @Transactional
    public ProductDTO saveProduct(ProductDTO ProductDTO) {
        return mapper.map(repo.save(mapper.map(ProductDTO, Product.class)), ProductDTO.class);
    }

    @Override
    @Transactional
    public void deleteProduct(ProductDTO ProductDTO) {
        repo.deleteById(ProductDTO.getId());
    }

    @Override
    public ProductDTO getProductById(String s) {
        return mapper.map(repo.findById(s), ProductDTO.class);
    }

    @Override
    public Boolean exists(String s) {
        return repo.existsById(s);
    }
}
