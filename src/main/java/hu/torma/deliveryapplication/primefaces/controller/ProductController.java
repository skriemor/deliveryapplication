package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.VendorDTO;
import hu.torma.deliveryapplication.service.ProductService;
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

    private List<SortMeta> sortBy;
    @Autowired
    ProductService service;

    private String label;
    private ProductDTO dto;
    private List<ProductDTO> dtoList;

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

    public void setDto(ProductDTO dto) {
        this.dto = dto;
    }

    public void setDtoList(List<ProductDTO> dtoList) {
        this.dtoList = dtoList;
    }

    public void setSortBy(List<SortMeta> sortBy) {
        this.sortBy = sortBy;
    }

    public void uiSaveProduct() {
        service.saveProduct(this.dto);
        getAllProducts();
        this.setDto(new ProductDTO());
    }

    public void deleteProduct() {
        if (this.dto != null) {
            service.deleteProduct(this.dto);
        }
        this.getAllProducts();
        this.dto = new ProductDTO();
    }

    public void editProduct(SelectEvent<ProductDTO> _dto) {
        this.setLabel("Módosítás");
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());
    }

    public void newProduct() {
        this.dto = new ProductDTO();
        this.setLabel("Hozzáadás");
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ProductDTO getDto() {
        if (this.dto == null) {
            this.dto = new ProductDTO();
        }
        return this.dto;
    }

    public String getLabel() {
        return label;
    }

    public List<SortMeta> getSortBy() {
        return sortBy;
    }

    public List<ProductDTO> getDtoList() {
        return dtoList;
    }

    public ProductService getService() {
        return service;
    }


}
