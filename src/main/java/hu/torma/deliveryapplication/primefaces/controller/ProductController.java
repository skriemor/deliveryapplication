package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.VendorDTO;
import hu.torma.deliveryapplication.service.ProductService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScope
@Controller("productController")
public class ProductController implements Serializable {

    @Getter @Setter private List<SortMeta> sortBy;
    @Getter @Autowired
    ProductService service;

    @Getter @Setter private String label;
    @Setter private ProductDTO dto;
    @Getter @Setter private List<ProductDTO> dtoList;

    @PostConstruct
    public void init() {
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder()
                .field("id")
                .order(SortOrder.ASCENDING)
                .build());
    }

    @PostConstruct
    public void getAllProducts() {
        this.dtoList = service.getAllProducts();
        this.setLabel("Hozzáadás");
    }

    public void uiSaveProduct() {
        service.saveProduct(this.dto);
        getAllProducts();
        this.setDto(new ProductDTO());
    }

    public void editProduct(SelectEvent<ProductDTO> _dto) {
        this.setLabel("Módosítás");
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());
    }

    public ProductDTO getDto() {
        if (this.dto == null) {
            this.dto = new ProductDTO();
        }
        return this.dto;
    }
}
