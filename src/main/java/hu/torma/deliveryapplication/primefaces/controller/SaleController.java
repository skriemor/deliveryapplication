package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;
import hu.torma.deliveryapplication.DTO.UnitDTO;
import hu.torma.deliveryapplication.service.ProductService;
import hu.torma.deliveryapplication.service.SaleService;
import hu.torma.deliveryapplication.service.StorageService;
import hu.torma.deliveryapplication.service.UnitService;
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
import java.util.logging.Logger;

@SessionScope
@Controller("saleController")
public class SaleController implements Serializable {

    String errorMessage = "";

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    Logger log = Logger.getLogger("Validatorlog");
    private ArrayList<ProductDTO> listFiveProduct = new ArrayList<>();


    private PurchasedProductDTO one, two, three, four, five, six;


    private List<SortMeta> sortBy;


    @Autowired
    StorageService sService;
    @Autowired
    ProductService pService;

    @Autowired
    UnitService uService;

    @Autowired
    SaleService service;

    private String label;

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    private String label2;
    private SaleDTO dto;
    private List<SaleDTO> dtoList;
    private String dateRange;

    private PurchasedProductDTO productDTO;

    public PurchasedProductDTO getProductDTO() {
        if (this.productDTO == null) this.productDTO = new PurchasedProductDTO();
        return this.productDTO;
    }

    public void updateAccountNum() {
        if (this.dto.getBuyer() != null)
            this.dto.setAccountNumber(this.dto.getBuyer().getAccountNum());
    }

    public void setProductDTO(PurchasedProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    @PostConstruct
    public void init() {
        errorMessage = "";
        checkFive();
        setUpFive();
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
        one.setCorrPercent(5);
        one.setUnitPrice(one.getProduct().getPrice());

        two = new PurchasedProductDTO();
        two.setProduct(listFiveProduct.get(1));
        two.setUnitPrice(two.getProduct().getPrice());
        two.setCorrPercent(5);

        three = new PurchasedProductDTO();
        three.setProduct(listFiveProduct.get(2));
        three.setUnitPrice(three.getProduct().getPrice());
        three.setCorrPercent(5);

        four = new PurchasedProductDTO();
        four.setProduct(listFiveProduct.get(3));
        four.setUnitPrice(four.getProduct().getPrice());
        four.setCorrPercent(5);

        five = new PurchasedProductDTO();
        five.setProduct(listFiveProduct.get(4));
        five.setUnitPrice(five.getProduct().getPrice());
        five.setCorrPercent(5);

        six = new PurchasedProductDTO();
        six.setProduct(listFiveProduct.get(5));
        six.setCorrPercent(8);
        six.setUnitPrice(six.getProduct().getPrice());
    }

    @PostConstruct
    public void getAllSales() {
        this.dtoList = service.getAllSales();
        this.setLabel("Hozzáadás");
        this.setLabel2("Termék hozzáadása");
    }

    public void setDto(SaleDTO dto) {
        this.dto = dto;
    }

    public void setDtoList(List<SaleDTO> dtoList) {
        this.dtoList = dtoList;
    }

    public void setSortBy(List<SortMeta> sortBy) {
        this.sortBy = sortBy;
    }

    private Boolean checkQuantity(PurchasedProductDTO dtoe) {
        if (dtoe.getQuantity() == null) return true;
        if (dtoe.getQuantity() > sService.getSupplyOf(dtoe.getProduct(), dto)) return false;
        return true;
    }

    public Boolean validateStorage() {
        String productString = "";
        if (!checkQuantity(one)) productString += one.getProduct().getId() + " ";
        if (!checkQuantity(two)) productString += two.getProduct().getId() + ' ';
        if (!checkQuantity(three)) productString += three.getProduct().getId() + " ";
        if (!checkQuantity(four)) productString += four.getProduct().getId() + " ";
        if (!checkQuantity(five)) productString += five.getProduct().getId() + " ";
        if (!checkQuantity(six)) productString += six.getProduct().getId();

        log.info(productString);
        if (!productString.equals("")) {
            productString = "Hiba, " + "a(z) " + productString + " termékből/termékekből nincs elég";
            errorMessage = productString;
            return false;
        } else {
            return true;
        }

    }

    public void sixSave() {

        this.dto.setProductList(new ArrayList<>());
        if (one.getUnitPrice() != null && one.getQuantity() != null) {
            this.setProductDTO(one);
            uiSaveProduct();
        }
        if (two.getUnitPrice() != null && two.getQuantity() != null) {
            this.setProductDTO(two);
            uiSaveProduct();
        }
        if (three.getUnitPrice() != null && three.getQuantity() != null) {
            this.setProductDTO(three);
            uiSaveProduct();
        }
        if (four.getUnitPrice() != null && four.getQuantity() != null) {
            this.setProductDTO(four);
            uiSaveProduct();
        }
        if (five.getUnitPrice() != null && five.getQuantity() != null) {
            this.setProductDTO(five);
            uiSaveProduct();
        }
        if (six.getUnitPrice() != null && six.getQuantity() != null) {
            this.setProductDTO(six);
            uiSaveProduct();
        }
    }

    public void uiSaveProduct() {
        if (this.dto.getProductList() == null) this.dto.setProductList(new ArrayList<>());
        if (this.dto.getProductList().contains(this.productDTO)) this.dto.getProductList().remove(this.productDTO);
        this.dto.getProductList().add(this.productDTO);
        this.productDTO = new PurchasedProductDTO();
        this.setLabel2("Termék hozzáadása");
    }


    public void uiSaveSale() {
        if (!validateStorage()) return;
        if (this.dto.getProductList() == null) this.dto.setProductList(new ArrayList<>());
        java.sql.Date date = new Date(System.currentTimeMillis());
        this.dto.setBookingDate(date);
        service.saveSale(this.dto);
        manageStorage();
        getAllSales();
        this.setDto(new SaleDTO());
        this.setProductDTO(new PurchasedProductDTO());
        this.errorMessage = "";
    }

    private void manageStorage() {
        for (PurchasedProductDTO p : this.dto.getProductList()) {
            sService.createQuantityIfNotExists(p.getProduct());
        }
    }

    public void deleteSale() {
        if (this.dto != null) {
            service.deleteSale(this.dto);
        }
        this.getAllSales();
        this.dto = new SaleDTO();
        errorMessage = "";
    }

    public void editSale(SelectEvent<SaleDTO> _dto) {
        emptySix();
        this.setLabel("Módosítás");
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());
        errorMessage = "";
    }

    private void emptySix() {
        one.setQuantity(0);
        two.setQuantity(0);
        three.setQuantity(0);
        four.setQuantity(0);
        five.setQuantity(0);
        six.setQuantity(0);
    }

    public void editProduct(SelectEvent<PurchasedProductDTO> _dto) {
        this.setLabel2("Módosítás");
        BeanUtils.copyProperties(_dto.getObject(), this.getProductDTO());
    }

    public void newSale() {
        this.dto = new SaleDTO();
        this.setLabel("Hozzáadás");
        errorMessage = "";
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public SaleDTO getDto() {
        if (this.dto == null) {
            this.dto = new SaleDTO();
        }
        return this.dto;
    }

    public String getLabel() {
        return label;
    }

    public List<SortMeta> getSortBy() {
        return sortBy;
    }

    public List<SaleDTO> getDtoList() {
        return dtoList;
    }

    public SaleService getService() {
        return service;
    }

    public void setService(SaleService service) {
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

    private void checkFive() {
        UnitDTO kgUnit = uService.getUnitByName("kg");
        ProductDTO product = new ProductDTO();
        if (!pService.exists("I.OSZTÁLYÚ")) {
            product.setPrice(672);
            product.setFirstUnit(kgUnit);
            product.setSecondUnit(kgUnit);
            product.setCompPercent(0);
            product.setTariffnum("0");
            product.setId("I.OSZTÁLYÚ");
            pService.saveProduct(product);
        }
        if (!pService.exists("II.OSZTÁLYÚ")) {
            product = new ProductDTO();
            product.setPrice(560);
            product.setFirstUnit(kgUnit);
            product.setSecondUnit(kgUnit);
            product.setCompPercent(0);
            product.setTariffnum("0");
            product.setId("II.OSZTÁLYÚ");
            pService.saveProduct(product);

        }
        if (!pService.exists("III.OSZTÁLYÚ")) {
            product = new ProductDTO();
            product.setPrice(448);
            product.setFirstUnit(kgUnit);
            product.setSecondUnit(kgUnit);
            product.setCompPercent(0);
            product.setTariffnum("0");
            product.setId("III.OSZTÁLYÚ");
            pService.saveProduct(product);

        }
        if (!pService.exists("IV.OSZTÁLYÚ")) {
            product = new ProductDTO();
            product.setPrice(224);
            product.setFirstUnit(kgUnit);
            product.setSecondUnit(kgUnit);
            product.setCompPercent(0);
            product.setTariffnum("0");
            product.setId("IV.OSZTÁLYÚ");
            pService.saveProduct(product);

        }
        if (!pService.exists("GYÖKÉR")) {
            product = new ProductDTO();
            product.setPrice(56);
            product.setFirstUnit(kgUnit);
            product.setSecondUnit(kgUnit);
            product.setCompPercent(0);
            product.setTariffnum("0");
            product.setId("GYÖKÉR");
            pService.saveProduct(product);

        }
        if (!pService.exists("IPARI")) {
            product = new ProductDTO();
            product.setPrice(300);
            product.setFirstUnit(kgUnit);
            product.setSecondUnit(kgUnit);
            product.setCompPercent(0);
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
