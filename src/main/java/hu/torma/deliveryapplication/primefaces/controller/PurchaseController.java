package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.service.PurchaseService;
import hu.torma.deliveryapplication.utility.pdf.PDFcreator;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SessionScope
@Controller("purchaseController")
public class PurchaseController implements Serializable {

    private StreamedContent file;

    Logger logger = Logger.getLogger("BOOL");
    private List<SortMeta> sortBy;

    @Autowired
    private PDFcreator pdFcreator;
    private Boolean pdfdisabled;



    @Autowired
    PurchaseService service;


    private String label;

    private Double perProdTotal;

    public Double getPerProdTotal() {
        Double temp = 0.0;
        Double perUnit = 0.0;
        int quant = 0;
        try {
            quant = productDTO.getQuantity();
            perUnit = productDTO.getUnitPrice();
            if (productDTO.getCorrPerUnit() != null) perUnit += productDTO.getCorrPerUnit();
            temp = quant * perUnit * (productDTO.getProduct().getCompPercent() + 100) / 100;
            if (productDTO.getCorrPercent() != null) temp *= (productDTO.getCorrPercent() + 100) / 100;
            if (productDTO.getCorrFt() != null) temp += productDTO.getCorrFt();

        } catch (Exception e) {

        }
        return (double) (Math.round(temp * 100) / 100);
    }

    public void setPerProdTotal(Double perProdTotal) {
        this.perProdTotal = perProdTotal;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    private String label2;
    private PurchaseDTO dto;
    private List<PurchaseDTO> dtoList;
    private String dateRange;


    private PurchasedProductDTO productDTO;


    public PurchasedProductDTO getProductDTO() {
        if (this.productDTO == null) this.productDTO = new PurchasedProductDTO();
        return this.productDTO;
    }

    public void setProductDTO(PurchasedProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    @PostConstruct
    public void init() {

        pdfdisabled = true;
        perProdTotal = 0.0;
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
        this.setLabel2("Termék hozzáadása");
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

    public void pdf() {
       file = pdFcreator.createDownload(this.dto);
        /*file = DefaultStreamedContent.builder()
                .name("modified.xlsx")
                .contentType("application/vnd.ms-excel")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/demo/excel/modified.xlsx"))
                .build();
*/
    }
    public StreamedContent getFile() {
        return file;
    }
    public void uiSavePurchase() {
        if (this.dto.getProductList() == null) this.dto.setProductList(new ArrayList<>());
        calculateTotalPrice();
        java.sql.Date date = new Date(System.currentTimeMillis());
        this.dto.setBookedDate(date);
        service.savePurchase(this.dto);
        getAllPurchases();
        this.setDto(new PurchaseDTO());
        this.pdfdisabled = true;
    }

    private void calculateTotalPrice() {
        if (this.dto.getProductList()==null) {
            this.dto.setTotalPrice(0.0);
        } else {
            this.dto.setTotalPrice(this.dto.getProductList().stream().map(c -> c.getTotalPrice()).collect(Collectors.summingDouble(Double::doubleValue)));

        }
    }

    public void deletePurchase() {
        if (this.dto != null) {
            service.deletePurchase(this.dto);
        }
        this.getAllPurchases();
        this.dto = new PurchaseDTO();
        this.pdfdisabled = true;
    }

    public void editPurchase(SelectEvent<PurchaseDTO> _dto) {
        this.setLabel("Módosítás");
        this.pdfdisabled = false;
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());
    }

    public void editProduct(SelectEvent<PurchasedProductDTO> _dto) {
        this.setLabel2("Módosítás");
        BeanUtils.copyProperties(_dto.getObject(), this.getProductDTO());
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


    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public void uiSaveProduct() {
        if (this.dto.getProductList()==null) this.dto.setProductList(new ArrayList<>());
        if (this.dto.getProductList().contains(this.productDTO)) this.dto.getProductList().remove(this.productDTO);
        this.dto.getProductList().add(this.productDTO);
        this.productDTO.setTotalPrice(getPerProdTotal());
        this.productDTO = new PurchasedProductDTO();
        this.setLabel2("Termék hozzáadása");
    }


    public void newProduct() {
        this.productDTO = new PurchasedProductDTO();
        this.setLabel2("Termék hozzáadása");
    }

    public void deleteProduct() {
        this.dto.getProductList().remove(this.productDTO);

    }

    public Boolean getPdfdisabled() {
        return pdfdisabled;
    }

    public void setPdfdisabled(Boolean pdfdisabled) {
        this.pdfdisabled = pdfdisabled;
    }
}
