package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.DTO.UnitDTO;
import hu.torma.deliveryapplication.service.ProductService;
import hu.torma.deliveryapplication.service.PurchaseService;
import hu.torma.deliveryapplication.service.StorageService;
import hu.torma.deliveryapplication.service.UnitService;
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
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SessionScope
@Controller("purchaseController")
public class PurchaseController implements Serializable {

    public Double getSixTotal() {
        Double temp = 0.0;
        Double sum = 0.0;
        try {
            if (one.getQuantity() != null && one.getProduct().getCompPercent() != null && one.getUnitPrice() != null) {
                sum = one.getUnitPrice() * one.getQuantity() * (1 + (0.01 * one.getProduct().getCompPercent()));
                one.setTotalPrice(sum);
                one.setQuantity2(one.getQuantity() * ((100.0 - one.getCorrPercent()) / 100));
                temp += sum;
            }
            if (two.getQuantity() != null && two.getProduct().getCompPercent() != null && two.getUnitPrice() != null) {
                sum = two.getUnitPrice() * two.getQuantity() * (1 + (0.01 * two.getProduct().getCompPercent()));
                two.setTotalPrice(sum);
                two.setQuantity2(two.getQuantity() * ((100.0 - two.getCorrPercent()) / 100));
                temp += sum;
            }
            if (three.getQuantity() != null && three.getProduct().getCompPercent() != null && three.getUnitPrice() != null) {
                sum = three.getUnitPrice() * three.getQuantity() * (1 + (0.01 * three.getProduct().getCompPercent()));
                three.setTotalPrice(sum);
                three.setQuantity2(three.getQuantity() * ((100.0 - three.getCorrPercent()) / 100));
                temp += sum;
            }
            if (four.getQuantity() != null && four.getProduct().getCompPercent() != null && four.getUnitPrice() != null) {
                sum = four.getUnitPrice() * four.getQuantity() * (1 + (0.01 * four.getProduct().getCompPercent()));
                four.setTotalPrice(sum);
                four.setQuantity2(four.getQuantity() * ((100.0 - four.getCorrPercent()) / 100));
                temp += sum;
            }
            if (five.getQuantity() != null && five.getProduct().getCompPercent() != null && five.getUnitPrice() != null) {
                sum = five.getUnitPrice() * five.getQuantity() * (1 + (0.01 * five.getProduct().getCompPercent()));
                five.setTotalPrice(sum);
                five.setQuantity2(five.getQuantity() * ((100.0 - five.getCorrPercent()) / 100));
                temp += sum;
            }
            if (six.getQuantity() != null && six.getProduct().getCompPercent() != null && six.getUnitPrice() != null) {
                sum = six.getUnitPrice() * six.getQuantity() * (1 + (0.01 * six.getProduct().getCompPercent()));
                six.setTotalPrice(sum);
                six.setQuantity2(six.getQuantity() * ((100.0 - six.getCorrPercent()) / 100));
                temp += sum;
            }

        } catch (Exception e) {

        }
        return (double) (Math.round(temp * 100) / 100);
    }

    public void setSixTotal(Double sixTotal) {
        this.sixTotal = sixTotal;
    }

    private Double sixTotal;
    private ArrayList<ProductDTO> listFiveProduct = new ArrayList<>();

    public Boolean getDisableedit() {
        return disableedit;
    }

    public void setDisableedit(Boolean disableedit) {
        this.disableedit = disableedit;
    }

    private Boolean disableedit = false;
    private PurchasedProductDTO one, two, three, four, five, six;

    private StreamedContent file;

    Logger logger = Logger.getLogger("BOOL");
    private List<SortMeta> sortBy;

    @Autowired
    private PDFcreator pdFcreator;
    private Boolean pdfdisabled;

    @Autowired
    StorageService sService;
    @Autowired
    ProductService pService;

    @Autowired
    UnitService uService;

    @Autowired
    PurchaseService service;


    private String label;


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
        checkFive();
        setUpFive();
        pdfdisabled = true;
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder()
                .field("id")
                .order(SortOrder.ASCENDING)
                .build());
        dateRange = (LocalDate.now().getYear() - 50) + ":" + (LocalDate.now().getYear() + 5);

    }

    private void setUpFive() {
        listFiveProduct.add(pService.getProductById("I.OSZTÁLYÚ"));
        listFiveProduct.add(pService.getProductById("II.OSZTÁLYÚ"));
        listFiveProduct.add(pService.getProductById("III.OSZTÁLYÚ"));
        listFiveProduct.add(pService.getProductById("IV.OSZTÁLYÚ"));
        listFiveProduct.add(pService.getProductById("GYÖKÉR"));
        listFiveProduct.add(pService.getProductById("IPARI"));
        one = new PurchasedProductDTO();
        one.setProduct(listFiveProduct.get(0));
        one.setCorrPercent(5.0);
        one.setUnitPrice(one.getProduct().getPrice());

        two = new PurchasedProductDTO();
        two.setProduct(listFiveProduct.get(1));
        two.setUnitPrice(two.getProduct().getPrice());
        two.setCorrPercent(5.0);

        three = new PurchasedProductDTO();
        three.setProduct(listFiveProduct.get(2));
        three.setUnitPrice(three.getProduct().getPrice());
        three.setCorrPercent(5.0);

        four = new PurchasedProductDTO();
        four.setProduct(listFiveProduct.get(3));
        four.setUnitPrice(four.getProduct().getPrice());
        four.setCorrPercent(5.0);

        five = new PurchasedProductDTO();
        five.setProduct(listFiveProduct.get(4));
        five.setUnitPrice(five.getProduct().getPrice());
        five.setCorrPercent(5.0);

        six = new PurchasedProductDTO();
        six.setProduct(listFiveProduct.get(5));
        six.setCorrPercent(8.0);
        six.setUnitPrice(six.getProduct().getPrice());
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

    public void enableEdit() {
        disableedit = true;
    }

    public void sixSave() {
        disableedit = false;
        if (one.getUnitPrice() !=null && one.getQuantity()!=null) {
            this.setProductDTO(one);

            uiSaveProduct();
        }
        if (two.getUnitPrice() !=null && two.getQuantity()!=null) {
            this.setProductDTO(two);
            uiSaveProduct();
        }
        if (three.getUnitPrice() !=null && three.getQuantity()!=null) {
            this.setProductDTO(three);
            uiSaveProduct();
        }
        if (four.getUnitPrice() !=null && four.getQuantity()!=null) {
            this.setProductDTO(four);
            uiSaveProduct();
        }
        if (five.getUnitPrice() !=null && five.getQuantity()!=null) {
            this.setProductDTO(five);
            uiSaveProduct();
        }
        if (six.getUnitPrice() !=null && six.getQuantity()!=null) {
            this.setProductDTO(six);
            uiSaveProduct();
        }

    }
    public void uiSaveProduct() {
        logger.info("saveproductcalled");
        if (this.dto.getProductList() == null) this.dto.setProductList(new ArrayList<>());
        if (this.dto.getProductList().contains(this.productDTO)) this.dto.getProductList().remove(this.productDTO);
        this.dto.getProductList().add(this.productDTO);
        this.productDTO = new PurchasedProductDTO();
        this.setLabel2("Termék hozzáadása");
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
        logger.warning("uiSaveCalled");
        if (this.dto.getProductList() == null) this.dto.setProductList(new ArrayList<>());
        calculateTotalPrice();
        java.sql.Date date = new Date(System.currentTimeMillis());
        this.dto.setBookedDate(date);
        service.savePurchase(this.dto);
        manageStorage();
        getAllPurchases();
        this.setDto(new PurchaseDTO());
        this.setProductDTO(new PurchasedProductDTO());
        this.pdfdisabled = true;

    }

    private void manageStorage() {

        for (PurchasedProductDTO p: this.dto.getProductList()) {
            sService.addToQuantity(p.getProduct(), p.getQuantity2());
        }
    }
    private void calculateTotalPrice() {
        if (this.dto.getProductList() == null) {
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




    public void newProduct() {
        this.productDTO = new PurchasedProductDTO();
        this.setLabel2("Termék hozzáadása");
    }

    public void deleteProduct() {
        this.dto.setProductList(new ArrayList<>());

    }

    public Boolean getPdfdisabled() {
        return pdfdisabled;
    }

    public void setPdfdisabled(Boolean pdfdisabled) {
        this.pdfdisabled = pdfdisabled;
    }

    private void checkFive() {
        UnitDTO kgUnit = uService.getUnitByName("kg");
        ProductDTO product = new ProductDTO();
        if (!pService.exists("I.OSZTÁLYÚ")) {
            product.setPrice(672.0);
            product.setFirstUnit(kgUnit);
            product.setSecondUnit(kgUnit);
            product.setCompPercent(0.0);
            product.setTariffnum("0");
            product.setId("I.OSZTÁLYÚ");
            pService.saveProduct(product);
        }
        if (!pService.exists("II.OSZTÁLYÚ")) {
            product = new ProductDTO();
            product.setPrice(560.0);
            product.setFirstUnit(kgUnit);
            product.setSecondUnit(kgUnit);
            product.setCompPercent(0.0);
            product.setTariffnum("0");
            product.setId("II.OSZTÁLYÚ");
            pService.saveProduct(product);

        }
        if (!pService.exists("III.OSZTÁLYÚ")) {
            product = new ProductDTO();
            product.setPrice(448.0);
            product.setFirstUnit(kgUnit);
            product.setSecondUnit(kgUnit);
            product.setCompPercent(0.0);
            product.setTariffnum("0");
            product.setId("III.OSZTÁLYÚ");
            pService.saveProduct(product);

        }
        if (!pService.exists("IV.OSZTÁLYÚ")) {
            product = new ProductDTO();
            product.setPrice(224.0);
            product.setFirstUnit(kgUnit);
            product.setSecondUnit(kgUnit);
            product.setCompPercent(0.0);
            product.setTariffnum("0");
            product.setId("IV.OSZTÁLYÚ");
            pService.saveProduct(product);

        }
        if (!pService.exists("GYÖKÉR")) {
            product = new ProductDTO();
            product.setPrice(56.0);
            product.setFirstUnit(kgUnit);
            product.setSecondUnit(kgUnit);
            product.setCompPercent(0.0);
            product.setTariffnum("0");
            product.setId("GYÖKÉR");
            pService.saveProduct(product);

        }
        if (!pService.exists("IPARI")) {
            product = new ProductDTO();
            product.setPrice(300.0);
            product.setFirstUnit(kgUnit);
            product.setSecondUnit(kgUnit);
            product.setCompPercent(0.0);
            product.setTariffnum("0");
            product.setId("IPARI");
            pService.saveProduct(product);

        }

    }

    public ArrayList<ProductDTO> getListFiveProduct() {
        return listFiveProduct;
    }

    public void setListFiveProduct(ArrayList<ProductDTO> listFiveProduct) {
        this.listFiveProduct = listFiveProduct;
    }

    public PurchasedProductDTO getOne() {
        return one;
    }

    public void setOne(PurchasedProductDTO one) {
        this.one = one;
    }

    public PurchasedProductDTO getTwo() {
        return two;
    }

    public void setTwo(PurchasedProductDTO two) {
        this.two = two;
    }

    public PurchasedProductDTO getThree() {
        return three;
    }

    public void setThree(PurchasedProductDTO three) {
        this.three = three;
    }

    public PurchasedProductDTO getFour() {
        return four;
    }

    public void setFour(PurchasedProductDTO four) {
        this.four = four;
    }

    public PurchasedProductDTO getFive() {
        return five;
    }

    public void setFive(PurchasedProductDTO five) {
        this.five = five;
    }

    public PurchasedProductDTO getSix() {
        return six;
    }

    public void setSix(PurchasedProductDTO six) {
        this.six = six;
    }
}
