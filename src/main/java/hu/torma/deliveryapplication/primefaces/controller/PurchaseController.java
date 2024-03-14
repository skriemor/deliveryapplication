package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.service.*;
import hu.torma.deliveryapplication.utility.dateutil.DateConverter;
import hu.torma.deliveryapplication.utility.pdf.PDFcreator;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;


@Controller
public class PurchaseController implements Serializable {
    @Autowired
    CompletionRecordService recordService;
    @Autowired
    private PDFcreator pdFcreator;
    @Autowired
    StorageService sService;
    @Autowired
    ProductService pService;
    @Autowired
    UnitService uService;
    @Autowired
    PurchaseService service;
    @Autowired
    PurchasedProductService purchasedProductService;
    @Autowired
    SiteService siteService;
    @Autowired
    CompletedPurchaseController completedPurchaseController;
    private Double sixTotal;
    private ArrayList<ProductDTO> listFiveProduct = new ArrayList<>();
    private PurchasedProductDTO one, two, three, four, five, six;
    private StreamedContent file;
    Logger logger = Logger.getLogger("BOOL");
    private List<SortMeta> sortBy;
    private Boolean pdfdisabled;
    private String label;
    private Boolean isSafeToDelete;
    private String label2;
    private PurchaseDTO dto;
    private List<PurchaseDTO> dtoList;
    private String dateRange;
    private PurchasedProductDTO productDTO;

    public void reConnectChildren() {
        List<PurchaseDTO> purchaseList = service.getAllPurchases();
        List<PurchasedProductDTO> purchasedProductList = purchasedProductService.getAllPurchasedProducts();

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
            //logger.info("dto quantity or corr was 0");
            return 0;
        }
        dto.setQuantity2((int) (dto.getQuantity() * ((100 - dto.getCorrPercent()) / 100.0)));
        //logger.info("return net "+dto.getQuantity2());
        return dto.getQuantity2();
    }

    public String getFormattedPriceOf(PurchasedProductDTO i) {
        return NumberFormat.getNumberInstance(Locale.US).format(getPriceOf(i)).replaceAll(",", " ");
    }

    public Integer getPriceOf(PurchasedProductDTO dto_) {
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

    public void forceUpdateRemainingPrices() {
        for (var c : dtoList) {
            c.setRemainingPrice(getRemaningDoublePrice(c));
            service.savePurchase(c);
        }
    }

    private void updateRemainingPrices() {
        for (var c : dtoList) {
            if (c.getRemainingPrice() == null) {
                c.setRemainingPrice(getRemaningDoublePrice(c));
                service.savePurchase(c);
            }

        }
    }


    public double getRemaningDoublePrice(PurchaseDTO pc) {
        var temp = pc;
        var tempList = temp.getProductList();
        var total = temp.getTotalPrice();
        var records = recordService.getAllCompletionRecords().stream().filter(r -> r.getPurchaseId().intValue() == pc.getId().intValue()).toList();
        for (var r : records) {
            total -= (int) (tempList.get(0).getUnitPrice() * r.getOne() * (1 + (0.01 * tempList.get(0).getProduct().getCompPercent())));
            total -= (int) (tempList.get(1).getUnitPrice() * r.getTwo() * (1 + (0.01 * tempList.get(1).getProduct().getCompPercent())));
            total -= (int) (tempList.get(2).getUnitPrice() * r.getThree() * (1 + (0.01 * tempList.get(2).getProduct().getCompPercent())));
            total -= (int) (tempList.get(3).getUnitPrice() * r.getFour() * (1 + (0.01 * tempList.get(3).getProduct().getCompPercent())));
            total -= (int) (tempList.get(4).getUnitPrice() * r.getFive() * (1 + (0.01 * tempList.get(4).getProduct().getCompPercent())));
            total -= (int) (tempList.get(5).getUnitPrice() * r.getSix() * (1 + (0.01 * tempList.get(5).getProduct().getCompPercent())));
        }

        return total;
    }

    public int getRemaningPrice(int id) {
        var temp = service.getPurchaseById(id);
        var tempList = temp.getProductList();
        var total = temp.getTotalPrice();
        var records = recordService.getAllCompletionRecords().stream().filter(r -> r.getPurchaseId() == id).toList();
        for (var r : records) {
            total -= (int) (tempList.get(0).getUnitPrice() * r.getOne() * (1 + (0.01 * tempList.get(0).getProduct().getCompPercent())));
            total -= (int) (tempList.get(1).getUnitPrice() * r.getTwo() * (1 + (0.01 * tempList.get(1).getProduct().getCompPercent())));
            total -= (int) (tempList.get(2).getUnitPrice() * r.getThree() * (1 + (0.01 * tempList.get(2).getProduct().getCompPercent())));
            total -= (int) (tempList.get(3).getUnitPrice() * r.getFour() * (1 + (0.01 * tempList.get(3).getProduct().getCompPercent())));
            total -= (int) (tempList.get(4).getUnitPrice() * r.getFive() * (1 + (0.01 * tempList.get(4).getProduct().getCompPercent())));
            total -= (int) (tempList.get(5).getUnitPrice() * r.getSix() * (1 + (0.01 * tempList.get(5).getProduct().getCompPercent())));
        }

        return total.intValue();
    }

    public String getFormattedRemainingPrice(int id) {
        return NumberFormat.getNumberInstance(Locale.US).format(getRemaningPrice(id)).replaceAll(",", " ");
    }

    public String getIntedRemainingPrice(int id) {
        return NumberFormat.getNumberInstance(Locale.US).format(getRemaningPrice(id)).replaceAll(",", "");

    }

    public String getIntedNum(double d) {
        return NumberFormat.getNumberInstance(Locale.US).format(d).replaceAll(",", "");
    }

    public String toDottedDate(java.util.Date dt) {
        return DateConverter.toDottedDate(dt);
    }


    public void setSixTotal(Double sixTotal) {
        this.sixTotal = sixTotal;
    }


    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }


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
        newPurchase();
        pdfdisabled = true;
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder().field("id").order(SortOrder.ASCENDING).build());
        dateRange = (LocalDate.now().getYear() - 50) + ":" + (LocalDate.now().getYear() + 5);
    }

    private void setUpFive() {
        listFiveProduct = new ArrayList<>();
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
    public void getAllPurchases() {
        this.dtoList = service.getAllPurchases();
        updateRemainingPrices();
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

    private void copyPurchasedProductsIntoPurchaseDto(PurchasedProductDTO... dtos) {
        dto.setProductList(new ArrayList<>());
        Arrays.stream(dtos).forEach(pp -> {
            if (pp.getUnitPrice() == null) pp.setUnitPrice(0);
            if (pp.getQuantity() == null) pp.setQuantity(0);
            if (pp.getTotalPrice() == null) {
                pp.setTotalPrice(getPriceOf(pp) == null ? 0 : getPriceOf(pp));
            }
            dto.getProductList().add(pp);
        });
    }


    public void pdf() {
        if (this.dto.getProductList() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "HIBA", "Mentse a jegyet dátummal együtt, mielőtt letölti!"));
            return;
        }
        file = pdFcreator.createDownload(this.dto);
    }


    public StreamedContent getFile() {
        return file;
    }

    private void checkNullSite() {
        if (siteService.getSiteById("-") == null) {
            SiteDTO siteDTO = new SiteDTO();
            siteDTO.setId(0L);
            siteDTO.setSiteName("-");
            siteService.saveSite(siteDTO);
        }
    }

    public void uiSavePurchase(boolean shouldPrint) {
        copyPurchasedProductsIntoPurchaseDto(one, two, three, four, five, six);
        if (this.dto.getProductList() == null) {
            return;
        }
        calculateTotalPrice();
        java.sql.Date date = new Date(System.currentTimeMillis());
        this.dto.setBookedDate(date);
        this.dto.setRemainingPrice(this.dto.getTotalPrice());
        if (dto.getSite() == null) {
            checkNullSite();
            dto.setSite(siteService.getSiteById("-"));
        }

        this.dto = service.savePurchase(this.dto);
        if (shouldPrint) {
            pdf();
        }

        getAllPurchases();
        emptySix();
        this.setDto(new PurchaseDTO());
        this.setProductDTO(new PurchasedProductDTO());
        this.pdfdisabled = true;
        setUpFive();
        completedPurchaseController.updateAvailablePurchases();

    }


    private void calculateTotalPrice() {
        if (this.dto.getProductList() == null) {
            this.dto.setTotalPrice(0.0);
        } else {
            this.dto.setTotalPrice((double) this.dto.getProductList().stream().map(PurchasedProductDTO::getTotalPrice).mapToInt(Integer::intValue).sum());
        }
    }


    public void deletePurchase() {
        service.deletePurchase(this.dto);
        this.getAllPurchases();
        this.dto = new PurchaseDTO();
        this.pdfdisabled = true;
        emptySix();
        completedPurchaseController.updateAvailablePurchases();

    }

    public Boolean getIsSafeToDelete() {
        return this.isSafeToDelete;
    }

    private void editSix() {
        one = dto.getProductList().get(0);
        two = dto.getProductList().get(1);
        three = dto.getProductList().get(2);
        four = dto.getProductList().get(3);
        five = dto.getProductList().get(4);
        six = dto.getProductList().get(5);
    }

    private void emptySix() {
        setUpFive();
        one.setQuantity(null);
        two.setQuantity(null);
        three.setQuantity(null);
        four.setQuantity(null);
        five.setQuantity(null);
        six.setQuantity(null);
    }

    public void newPurchase() {
        emptySix();
        setUpFive();
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
        UnitDTO kgUnit = new UnitDTO();
        kgUnit.setId("kg");
        kgUnit.setUnitName("Kilogramm");
        uService.saveUnit(kgUnit);
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
        int sum = 0;
        sum += getNetOf(one);
        sum += getNetOf(two);
        sum += getNetOf(three);
        sum += getNetOf(four);
        sum += getNetOf(five);
        sum += getNetOf(six);
        return sum;

    }


    public void refreshReceipts() {
        getAllPurchases();
        for (var c : dtoList) {
            var id = c.getId();
            var temp = c;
            var tempList = temp.getProductList();
            var total = temp.getTotalPrice();
            var records = recordService.findAllByPurchaseId(id);
            var tempstring = "";
            for (var r : records) {
                total -= (int) (tempList.get(0).getUnitPrice() * r.getOne() * (1 + (0.01 * tempList.get(0).getProduct().getCompPercent())));
                total -= (int) (tempList.get(1).getUnitPrice() * r.getTwo() * (1 + (0.01 * tempList.get(1).getProduct().getCompPercent())));
                total -= (int) (tempList.get(2).getUnitPrice() * r.getThree() * (1 + (0.01 * tempList.get(2).getProduct().getCompPercent())));
                total -= (int) (tempList.get(3).getUnitPrice() * r.getFour() * (1 + (0.01 * tempList.get(3).getProduct().getCompPercent())));
                total -= (int) (tempList.get(4).getUnitPrice() * r.getFive() * (1 + (0.01 * tempList.get(4).getProduct().getCompPercent())));
                total -= (int) (tempList.get(5).getUnitPrice() * r.getSix() * (1 + (0.01 * tempList.get(5).getProduct().getCompPercent())));
                tempstring += r.getCompletedPurchase().getNewSerial() + " ";
            }

            temp.setReceiptId(tempstring);
            temp.setRemainingPrice(total);
            service.savePurchase(temp);
        }
    }

    public void getLastPrices() {
        if (this.dto != null && this.dto.getVendor() != null && dto.getVendor().getTaxId() != null) {
            List<Integer> ints = service.getPricesOnLastPurchase(dto.getVendor().getTaxId());
            one.setUnitPrice(ints.get(0));
            two.setUnitPrice(ints.get(1));
            three.setUnitPrice(ints.get(2));
            four.setUnitPrice(ints.get(3));
            five.setUnitPrice(ints.get(4));
            six.setUnitPrice(ints.get(5));
        }
    }

    public void onRowSelect(SelectEvent<PurchaseDTO> event) {
        BeanUtils.copyProperties(event.getObject(), dto);
        this.pdfdisabled = false;
        isSafeToDelete = !recordService.existsByPurchaseId(this.dto.getId());
        editSix();
        logger.info(isSafeToDelete ? "Safe" : "Unsafe");
    }
}
