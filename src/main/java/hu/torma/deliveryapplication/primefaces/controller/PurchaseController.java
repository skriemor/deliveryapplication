package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.service.PurchaseService;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SessionScope
@Controller("purchaseController")
public class PurchaseController implements Serializable {

    private List<SortMeta> sortBy;


    @Autowired
    PurchaseService service;

    private String label;
    private PurchaseDTO dto;
    private List<PurchaseDTO> dtoList;

    private List<PurchasedProductDTO> productList;

    private String dateRange;



    private PurchasedProductDTO productDTO;



    public PurchasedProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(PurchasedProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    @PostConstruct
    public void init() {
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder()
                .field("id")
                .order(SortOrder.ASCENDING)
                .build());
        dateRange = (LocalDate.now().getYear() - 50) + ":" + (LocalDate.now().getYear() + 5);

    }

    @PostConstruct
    public void getAllPurchases() {
        this.dtoList = service.getAllPurchases();
        this.setLabel("Hozzáadás");
    }

    public void setDto(PurchaseDTO dto) {
        this.dto = dto;
    }

    public void setDtoList(List<PurchaseDTO> dtoList) {
        this.dtoList = dtoList;
    }

    public void setSortBy(List<SortMeta> sortBy) {
        this.sortBy = sortBy;
    }

    public void uiSavePurchase() {
        if (this.dto.getTotalPrice() == null) this.dto.setTotalPrice(0);
        java.sql.Date date = new Date(System.currentTimeMillis());
        this.dto.setBookedDate(date);
        service.savePurchase(this.dto);
        getAllPurchases();
        this.setDto(new PurchaseDTO());
    }

    public void deletePurchase() {
        if (this.dto != null) {
            service.deletePurchase(this.dto);
        }
        this.getAllPurchases();
        this.dto = new PurchaseDTO();
    }

    public void editPurchase(SelectEvent<PurchaseDTO> _dto) {
        this.setLabel("Módosítás");
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());
    }

    public void editProduct(SelectEvent<PurchasedProductDTO> _dto) {
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());
    }
    public void newPurchase() {
        this.dto = new PurchaseDTO();
        this.setLabel("Hozzáadás");
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public PurchaseDTO getDto() {
        if (this.dto == null) {
            this.dto = new PurchaseDTO();
        }
        return this.dto;
    }

    public String getLabel() {
        return label;
    }

    public List<SortMeta> getSortBy() {
        return sortBy;
    }

    public List<PurchaseDTO> getDtoList() {
        return dtoList;
    }

    public PurchaseService getService() {
        return service;
    }

    public void setService(PurchaseService service) {
        this.service = service;
    }

    public List<PurchasedProductDTO> getProductList() {
        return productList;
    }


    public void setProductList(List<PurchasedProductDTO> productList) {
        this.productList = productList;
    }
    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

}
