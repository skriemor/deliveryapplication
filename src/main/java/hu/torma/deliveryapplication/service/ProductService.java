package hu.torma.deliveryapplication.service;


import hu.torma.deliveryapplication.DTO.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    ProductDTO getProduct(ProductDTO ProductDTO);

    ProductDTO saveProduct(ProductDTO ProductDTO);

    void deleteProduct(ProductDTO ProductDTO);

    ProductDTO getProductById(String s);
}
