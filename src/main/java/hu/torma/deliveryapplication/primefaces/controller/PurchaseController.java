package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.service.*;
import hu.torma.deliveryapplication.utility.dateutil.DateConverter;
import hu.torma.deliveryapplication.utility.pdf.PDFcreator;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Tuple;
import java.io.Serializable;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@ViewScoped
@ManagedBean("purchaseController")
@DependsOn("dbInit")
public class PurchaseController implements Serializable {
    @Autowired CompletionRecordService recordService;
    @Autowired private PDFcreator pdFcreator;
    @Autowired StorageService sService;
    @Autowired ProductService pService;
    @Autowired UnitService uService;
    @Autowired PurchaseService service;
    @Autowired PurchasedProductService purchasedProductService;
    @Autowired SiteService siteService;
    @Getter @Setter private PurchasedProductDTO one;
    @Getter @Setter private PurchasedProductDTO two;
    @Getter @Setter private PurchasedProductDTO three;
    @Getter @Setter private PurchasedProductDTO four;
    @Getter @Setter private PurchasedProductDTO five;
    @Getter @Setter private PurchasedProductDTO six;
    @Getter private StreamedContent file;
    Logger logger = Logger.getLogger("BOOL");
    @Getter @Setter private List<SortMeta> sortBy;
    @Getter private Boolean isSafeToDelete;
    @Setter private PurchaseDTO dto;
    @Getter @Setter private List<PurchaseDTO> dtoList;


    public Double getNetAvgPrice() {
        return (double) Math.round(getGrossAvgPrice() / 1.12 * 100) / 100;
    }

    public Double getDiff() {
        return getGrossTotal() - getNetTotal();
    }

    public Double getGrossTotal() {
        return (double) Math.round(getGrossAvgPrice() * getNetSum());
    }

    public Double getGrossAvgPrice() {
        return (double) Math.round(getSixTotal() / (double) getNetSum() * 100) / 100;
    }

    public Double getNetTotal() {
        return (double) Math.round(getGrossTotal() / 1.12);
    }

    public Integer getNetOf(PurchasedProductDTO dto) {
        if (dto.getQuantity() == null || dto.getCorrPercent() == null) {
            return 0;
        }
        dto.setQuantity2((int) (dto.getQuantity() * ((100 - dto.getCorrPercent()) / 100.0)));
        return dto.getQuantity2();
    }

    public String getFormattedPriceOf(PurchasedProductDTO i) {
        return NumberFormat.getNumberInstance(Locale.US).format(calculateSetAndGetTotalPriceOf(i)).replaceAll(",", " ");
    }

    public Integer calculateSetAndGetTotalPriceOf(PurchasedProductDTO dto_) {
        if (dto_.getQuantity() == null || dto_.getUnitPrice() == null || dto_.getCorrPercent() == null) return 0;
        dto_.setQuantity2((int) (dto_.getQuantity() * ((100 - dto_.getCorrPercent()) / 100.0)));
        Integer sum = (int) (dto_.getUnitPrice() * dto_.getQuantity2() * (1 + (0.01 * dto_.getProduct().getCompPercent())));
        dto_.setTotalPrice(sum);
        return sum;
    }

    private int setAndSumPurchasedProductDtoPrices(PurchasedProductDTO... dtos) {
        return Arrays.stream(dtos).mapToInt(d -> {
            if (d.getQuantity() != null && d.getProduct().getCompPercent() != null && d.getUnitPrice() != null) {
                d.setQuantity2(getNetOf(d));
                d.setTotalPrice((int) (d.getUnitPrice() * d.getQuantity2() * (1 + (0.01 * one.getProduct().getCompPercent()))));
                return d.getTotalPrice();
            }
            return 0;
        }).sum();
    }

    public Integer getSixTotal() {
        return setAndSumPurchasedProductDtoPrices(one, two, three, four, five, six);
    }

    public String getIntedNum(double d) {
        return NumberFormat.getNumberInstance(Locale.US).format(d).replaceAll(",", "");
    }

    public String toDottedDate(java.util.Date dt) {
        return DateConverter.toDottedDate(dt);
    }

    private void setupPurchasedProducts() {
        one = new PurchasedProductDTO();
        one.setProduct(pService.getProductById("I.OSZTÁLYÚ"));
        one.setCorrPercent(5);
        one.setUnitPrice(one.getProduct().getPrice());
        one.setQuantity(null);

        two = new PurchasedProductDTO();
        two.setProduct(pService.getProductById("II.OSZTÁLYÚ"));
        two.setUnitPrice(two.getProduct().getPrice());
        two.setCorrPercent(5);
        two.setQuantity(null);

        three = new PurchasedProductDTO();
        three.setProduct(pService.getProductById("III.OSZTÁLYÚ"));
        three.setUnitPrice(three.getProduct().getPrice());
        three.setCorrPercent(5);
        three.setQuantity(null);

        four = new PurchasedProductDTO();
        four.setProduct(pService.getProductById("IV.OSZTÁLYÚ"));
        four.setUnitPrice(four.getProduct().getPrice());
        four.setCorrPercent(5);
        four.setQuantity(null);

        five = new PurchasedProductDTO();
        five.setProduct(pService.getProductById("GYÖKÉR"));
        five.setUnitPrice(five.getProduct().getPrice());
        five.setCorrPercent(5);
        five.setQuantity(null);

        six = new PurchasedProductDTO();
        six.setProduct(pService.getProductById("IPARI"));
        six.setCorrPercent(8);
        six.setUnitPrice(six.getProduct().getPrice());
        six.setQuantity(null);
    }

    @PostConstruct
    public void init() {
        setupPurchasedProducts();
        newPurchase();
        getAllPurchases();
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder().field("id").order(SortOrder.ASCENDING).build());
    }

    public void getAllPurchases() {
        dtoList = service.getAllPurchasesForListing();
    }

    private void copyPurchasedProductsIntoPurchaseDto(PurchasedProductDTO... dtos) {
        dto.setProductList(new ArrayList<>());
        Arrays.stream(dtos).forEach(pp -> {
            if (pp.getUnitPrice() == null) pp.setUnitPrice(0);
            if (pp.getQuantity() == null) pp.setQuantity(0);
            if (pp.getTotalPrice() == null) {
                pp.setTotalPrice(calculateSetAndGetTotalPriceOf(pp) == null ? 0 : calculateSetAndGetTotalPriceOf(pp));
            }
            dto.getProductList().add(pp);
        });
    }

    public void pdf(boolean shouldPrint) {
        if (shouldPrint) {
            if (dto.getProductList() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "HIBA", "Mentse a jegyet dátummal együtt, mielőtt letölti!"));
                return;
            }
            file = pdFcreator.createDownload(dto);
        }
    }

    private void evaluateAndSetPurchaseRemainingPrice() {
        if (dto.getId() != null) {
            Tuple priceAndSerials = service.getConcatedSerialsAndMaskedPricesById(dto.getId());
            dto.setRemainingPrice(dto.getTotalPrice() - Double.parseDouble(priceAndSerials.get(0).toString()));
            dto.setReceiptId(priceAndSerials.get(1).toString());
        } else {
            dto.setRemainingPrice(dto.getTotalPrice());
        }
    }

    private void preparePurhcaseSite() {
        if (dto.getSite() == null) {
            dto.setSite(siteService.getSiteById("-"));
        }
    }

    public void uiSavePurchase(boolean shouldPrint) {
        copyPurchasedProductsIntoPurchaseDto(one, two, three, four, five, six);
        calculateAndSetTotalPrice();
        dto.setBookedDate(new Date(System.currentTimeMillis()));
        evaluateAndSetPurchaseRemainingPrice();
        preparePurhcaseSite();
        dto = service.savePurchase(dto);
        pdf(shouldPrint);
        getAllPurchases();
        newPurchase();
        //completedPurchaseController.updateAvailablePurchases();
    }

    private void calculateAndSetTotalPrice() {
        dto.setTotalPrice((double) dto.getProductList().stream().map(PurchasedProductDTO::getTotalPrice).mapToInt(Integer::intValue).sum());
    }

    public void deletePurchase() {
        service.deletePurchase(dto);
        getAllPurchases();
        newPurchase();
    }

    private void editSix() {
        one = dto.getProductList().get(0);
        two = dto.getProductList().get(1);
        three = dto.getProductList().get(2);
        four = dto.getProductList().get(3);
        five = dto.getProductList().get(4);
        six = dto.getProductList().get(5);
    }

    public void newPurchase() {
        setupPurchasedProducts();
        dto = new PurchaseDTO();
    }

    public PurchaseDTO getDto() {
        if (dto == null) {
            dto = new PurchaseDTO();
        }
        return dto;
    }

    public String getFormattedSixTotal() {
        return NumberFormat.getNumberInstance(Locale.US).format(getSixTotal()).replaceAll(",", " ");
    }

    public String getFormattedNumber(double num) {
        if (num - (int) num == 0.0) {
            return NumberFormat.getNumberInstance(Locale.US).format((int) num).replaceAll(",", " ");
        }
        return NumberFormat.getNumberInstance(Locale.US).format(num).replaceAll(",", " ");
    }

    public int getNetSum() {
        return Stream.of(one, two, three, four, five, six)
                .mapToInt(PurchasedProductDTO::getNetOf)
                .sum();
    }

    public void getLastPrices() {
        if (dto != null && dto.getVendor() != null && dto.getVendor().getTaxId() != null) {
            List<Integer> ints = service.getPricesOnLastPurchase(dto.getVendor().getTaxId());
            one.setUnitPrice(ints.get(0));
            two.setUnitPrice(ints.get(1));
            three.setUnitPrice(ints.get(2));
            four.setUnitPrice(ints.get(3));
            five.setUnitPrice(ints.get(4));
            six.setUnitPrice(ints.get(5));
        }
    }

    public void reConnectChildren() {
        List<PurchaseDTO> purchaseList = service.getAllPurchases();
        for (var c : purchaseList) {
            logger.info("Attempting to get children of " + c.getId());
            for (var d : c.getProductList()) {
                int proposedAmount = (int) ((double) d.getQuantity2() / ((100.0 - d.getCorrPercent()) / 100.0));

                int proposedAmount2 = (int) ((double) (d.getQuantity2() + 1) / ((100.0 - d.getCorrPercent()) / 100.0));
                logger.info(proposedAmount + " " + proposedAmount2);
                if ((int) ((double) proposedAmount * ((100.0 - d.getCorrPercent()) / 100.0)) == d.getQuantity2()) {
                    d.setQuantity(proposedAmount);
                    logger.info("Chosen first amount");
                } else if ((int) ((double) proposedAmount2 * ((100.0 - d.getCorrPercent()) / 100.0)) == d.getQuantity2()) {
                    d.setQuantity(proposedAmount2);
                    logger.info("Chosen second amount");
                }
            }
            service.savePurchase(c);
        }
    }

    public void onRowSelect(SelectEvent<PurchaseDTO> event) {
        dto = service.getPurchaseAndFetchPPsById(event.getObject().getId());
        isSafeToDelete = !recordService.existsByPurchaseId(dto.getId());
        editSix();
    }
}
