package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;
import hu.torma.deliveryapplication.DTO.UnitDTO;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.entity.PurchasedProduct;
import hu.torma.deliveryapplication.entity.Sale;
import hu.torma.deliveryapplication.service.ProductService;
import hu.torma.deliveryapplication.service.SaleService;
import hu.torma.deliveryapplication.service.StorageService;
import hu.torma.deliveryapplication.service.UnitService;
import hu.torma.deliveryapplication.utility.dateutil.DateConverter;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

@SessionScope
@Controller("saleController")
@DependsOn("dbInit")
public class SaleController implements Serializable {
    @Autowired StorageService sService;
    @Autowired ProductService pService;
    @Autowired UnitService uService;
    @Autowired SaleService service;

    @Setter @Getter String errorMessage = "";

    Logger log = Logger.getLogger("Validatorlog");
    @Setter private ArrayList<ProductDTO> listFiveProduct = new ArrayList<>();


    @Getter @Setter private PurchasedProductDTO one;
    @Getter @Setter private PurchasedProductDTO two;
    @Getter @Setter private PurchasedProductDTO three;
    @Getter @Setter private PurchasedProductDTO four;
    @Getter @Setter private PurchasedProductDTO five;
    @Getter @Setter private PurchasedProductDTO six;


    @Setter private List<SortMeta> sortBy;

    public int getWeightSum() {
        int sum = 0;
        if (one.getQuantity() != null) sum += one.getQuantity();
        if (two.getQuantity() != null) sum += two.getQuantity();
        if (three.getQuantity() != null) sum += three.getQuantity();
        if (four.getQuantity() != null) sum += four.getQuantity();
        if (five.getQuantity() != null) sum += five.getQuantity();
        if (six.getQuantity() != null) sum += six.getQuantity();


        return sum;
    }

    public String getFormattedNumber(int num) {
        return NumberFormat.getNumberInstance(Locale.US).format(num).replaceAll(",", " ");
    }


    private String label;

    @Getter @Setter private String label2;
    @Setter private SaleDTO dto;
    @Setter private List<SaleDTO> dtoList;
    @Setter private PurchasedProductDTO productDTO;

    public PurchasedProductDTO getProductDTO() {
        if (this.productDTO == null) this.productDTO = new PurchasedProductDTO();
        return this.productDTO;
    }

    @PostConstruct
    public void init() {
        errorMessage = "";
        setUpFive();
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder().field("id").order(SortOrder.ASCENDING).build());
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

    public String toDottedDate(java.util.Date dt) {
        return DateConverter.toDottedDate(dt);
    }

    private void copyPPDataToEntityPPs(Sale entity, PurchasedProductDTO... pps) {
        List<PurchasedProduct> entityPps = entity.getProductList();
        for (PurchasedProductDTO pp : pps) {
            Optional<PurchasedProduct> ppWithGivenProduct = entityPps.stream().filter(
                    entityPP -> Objects.equals(entityPP.getProduct().getId(), pp.getProduct().getId())
            ).findFirst();

            if (ppWithGivenProduct.isPresent()) {
                ppWithGivenProduct.get().setQuantity(pp.getQuantity());
            } else {
                PurchasedProduct ppEntity = pp.toEntity(true, false, false);
                ppEntity.setSale(entity);
                if (ppEntity.getQuantity() == null) {
                    ppEntity.setQuantity(0);
                }
                if (ppEntity.getQuantity2() == null) {
                    ppEntity.setQuantity2(0);
                }
                entity.getProductList().add(ppEntity);
            }
        }
    }

    private void copyBasicPurchaseDataToEntity(Sale entity) {
        entity.setBuyer(dto.getBuyer().toEntity());
        entity.setCurrency(dto.getCurrency());
        entity.setPrice(dto.getPrice());
        entity.setReceiptId(dto.getReceiptId());
        entity.setReceiptDate(dto.getReceiptDate());
        entity.setDeadLine(dto.getDeadLine());
        entity.setCompletionDate(dto.getCompletionDate());
        entity.setGlobalgap(dto.getGlobalgap());
        entity.setLetai(dto.getLetai());
    }

    public Double calculateSetAndGetTotalPriceOf(PurchasedProductDTO dto_) {
        if (dto_.getQuantity() == null || dto_.getUnitPrice() == null || dto_.getCorrPercent() == null) return 0.0;
        dto_.setQuantity2((int) (dto_.getQuantity() * ((100 - dto_.getCorrPercent()) / 100.0)));
        Double sum = (dto_.getUnitPrice() * dto_.getQuantity2() * (1 + (0.01 * dto_.getProduct().getCompPercent())));
        dto_.setTotalPrice(sum);
        return sum;
    }

    private void fixUpPPs(PurchasedProductDTO... dtos) {
        dto.setProductList(new ArrayList<>());

        Arrays.stream(dtos).forEach(pp -> {
            if (pp.getUnitPrice() == null) pp.setUnitPrice(0);
            if (pp.getQuantity() == null) pp.setQuantity(0);
            if (pp.getTotalPrice() == null) {
                pp.setTotalPrice(calculateSetAndGetTotalPriceOf(pp));
            }
            dto.getProductList().add(pp);
        });
    }

    public void uiSaveSale() {
        boolean update = dto.getId() != null;

        this.dto.setBookingDate(new Date(System.currentTimeMillis()));

        if (update) { // modosul
            Sale entity = service.getSaleEntityById(dto.getId()).orElseThrow(
                () -> new RuntimeException("A módosítandó eladás ID alapján nem található az adatbázisban")
            );
            copyPPDataToEntityPPs(entity, one, two, three, four, five, six);
            copyBasicPurchaseDataToEntity(entity);
            entity = service.save(entity);
            dto = entity.toDTO(true, true);
        } else { // uj eladas
            fixUpPPs(one, two, three, four, five, six);
            service.saveSale(this.dto);
        }

        this.setDto(new SaleDTO());
        this.setProductDTO(new PurchasedProductDTO());
        this.errorMessage = "";
        getAllSales();
        nullQuants();
    }

    private void nullQuants() {
        this.one.setQuantity(null);
        this.two.setQuantity(null);
        this.three.setQuantity(null);
        this.four.setQuantity(null);
        this.five.setQuantity(null);
        this.six.setQuantity(null);

    }

    public void deleteSale() {
        if (this.dto != null) {
            service.deleteSale(this.dto);
        }
        this.getAllSales();
        this.dto = new SaleDTO();
        errorMessage = "";
    }

    public void editSale(SelectEvent<SaleDTO> clickEvent) {
        SaleDTO sale = clickEvent.getObject() == null ? null : clickEvent.getObject();

        if (sale == null) {
            throw new RuntimeException("A kijelölt sor üres objektummal, vagy üres objektumId-vel tért vissza.");
        }

        dto = service.getSaleById(sale.getId()).orElseThrow(() -> new RuntimeException("A kijelölt sorhoz nem tartozik DB bejegyzés"));

        emptySix();
        setQuants();
        errorMessage = "";
        this.setLabel("Módosítás");
    }

    private void emptySix() {
        one.setQuantity(null);
        two.setQuantity(null);
        three.setQuantity(null);
        four.setQuantity(null);
        five.setQuantity(null);
        six.setQuantity(null);
    }

    private void setQuants() {
        for (var c : dto.getProductList()) {
            if (c.getProduct().getId().equals("I.OSZTÁLYÚ")) one.setQuantity(c.getQuantity());
            if (c.getProduct().getId().equals("II.OSZTÁLYÚ")) two.setQuantity(c.getQuantity());
            if (c.getProduct().getId().equals("III.OSZTÁLYÚ")) three.setQuantity(c.getQuantity());
            if (c.getProduct().getId().equals("IV.OSZTÁLYÚ")) four.setQuantity(c.getQuantity());
            if (c.getProduct().getId().equals("GYÖKÉR")) five.setQuantity(c.getQuantity());
            if (c.getProduct().getId().equals("IPARI")) six.setQuantity(c.getQuantity());
        }


    }

    public void editProduct(SelectEvent<PurchasedProductDTO> _dto) {
        this.setLabel2("Módosítás");
        BeanUtils.copyProperties(_dto.getObject(), this.getProductDTO());
    }

    public void newSale() {
        this.dto = new SaleDTO();
        this.setLabel("Hozzáadás");
        errorMessage = "";
        nullQuants();
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


    public void newProduct() {
        this.productDTO = new PurchasedProductDTO();
        this.setLabel2("Termék hozzáadása");
    }

    public void deleteProduct() {
        this.dto.setProductList(new ArrayList<>());
    }


    public ArrayList<ProductDTO> getListFiveProduct() {
        return listFiveProduct;
    }

}
