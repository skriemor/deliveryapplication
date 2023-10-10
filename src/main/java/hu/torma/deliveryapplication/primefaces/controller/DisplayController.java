package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.primefaces.sumutils.SaleSumPojo;
import hu.torma.deliveryapplication.service.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@SessionScope
@Controller("displayController")
@Setter
@Getter
public class DisplayController implements Serializable {

    List<CompletionRecordDTO> CPRecords;
    String numSerial;

    private CompletedPurchaseDTO CPSums;

    private boolean shouldFilterByPaper, paidOnly;

    private String mediator;

    private String filterPaymentMethodCP;
    private String filterName, filterName2, filterName4;

    private SaleSumPojo saleSumPojo;
    private String filterCurrency2, filterPaper2;
    private Boolean filterUnpaidOnly2, filterLetaiOnly2, filterGlobalGapOnly2;
    private Boolean fullyPaidFilter;

    private Date filterDateFrom, filterDateFrom2, filterDateFrom3, filterDateFrom4;

    private Date filterDateTo, filterDateTo2, filterDateTo3, filterDateTo4;

    private List<PurchaseDTO> purchaseDTOS;
    private List<CompletedPurchaseDTO> CPDTOS;
    private List<SaleDTO> saleDTOS;
    private List<MediatorDisplay> mediatorDisplays;

    @Autowired
    StorageService storageService;
    @Autowired
    CompletedPurchaseService CPService;
    @Autowired
    ProductService productService;
    @Autowired
    MediatorService mediatorService;
    @Autowired
    UnitService unitService;

    private List<MediatorDTO> mediatorDTOS;
    @Autowired
    PurchaseService purchaseService;

    @Autowired
    SaleService saleService;

    @PostConstruct
    public void init() {
        purchaseDTOS = new ArrayList<>();
        CPDTOS = new ArrayList<>();
        mediatorDTOS = new ArrayList<>();
        saleDTOS = new ArrayList<>();
    }

    public void refreshPurchases() {
        purchaseDTOS = purchaseService.getAllPurchases();
    }

    public void refreshSales() {
        saleDTOS = saleService.getAllSales();
    }

    public void refreshCompletedpurchases() {
        CPDTOS = CPService.getAllCompletedPurchases();
        CPRecords = CPDTOS.stream().flatMap(cp -> cp.getRecords().stream()).toList();
    }

    public void refreshMediators() {
        mediatorDTOS = mediatorService.getAllMediators();
    }



    private void doFilterMediator() {
        if (mediator != null) {
            mediatorDTOS = mediatorDTOS.stream().filter(c -> c.getId().toLowerCase().contains(mediator.toLowerCase())).toList();
        }

    }


    public List<PurchaseDTO> refreshPurchaseDTOS() {
        purchaseDTOS = purchaseService.applyFilterChainAndReturnPurchases(filterName, filterDateFrom, filterDateTo, fullyPaidFilter);
        return new ArrayList<>(purchaseDTOS);
    }

    public ArrayList<SaleDTO> refreshSaleDTOS() {
        saleDTOS = saleService.applyFilterChainAndReturnSales(filterName2, filterCurrency2, filterDateFrom2, filterDateTo2, filterUnpaidOnly2, filterPaper2, filterLetaiOnly2, filterGlobalGapOnly2);
        this.saleSumPojo = new SaleSumPojo(saleDTOS);
        return new ArrayList<>(saleDTOS);
    }


    public ArrayList<CompletedPurchaseDTO> refreshCPDTOS() {
        CPDTOS = CPService.getFilteredListOfCPs(filterName4, filterDateFrom4, filterDateTo4, numSerial, paidOnly, filterPaymentMethodCP);
        CPRecords = CPDTOS.stream().flatMap(cp -> cp.getRecords().stream()).toList();
        return new ArrayList<>(CPDTOS);
    }

    public ArrayList<MediatorDisplay> refreshMediatorDisplays() {
        var tmp = new ArrayList<MediatorDisplay>();
        mediatorDTOS = mediatorService.getAllMediators();
        doFilterMediator();
        generateDisplays();
        return new ArrayList<>(mediatorDisplays);
    }

    private ArrayList<PurchaseDTO> getPurchasedProductListDateFiltered() {
        List<PurchaseDTO> lst = new ArrayList<>();
        if (filterDateFrom3 != null && filterDateTo3 != null) {
            lst = purchaseService.getPsByBothDates(filterDateFrom3, filterDateTo3);
        } else if (filterDateFrom3 != null && filterDateTo3 == null) {
            lst = purchaseService.getPsByStartingDate(filterDateFrom3);
        } else if (filterDateFrom3 == null && filterDateTo3 != null) {
            lst = purchaseService.getPsByEndingDate(filterDateTo3);
        } else {
            lst = purchaseService.getAllPurchases();
        }


        return new ArrayList<>(lst);
    }

    public void generateDisplays() {
        mediatorDisplays = new ArrayList<>();
        mediatorDTOS.forEach(m -> {
            var disp = new MediatorDisplay();

            var tmpsv = getPurchasedProductListDateFiltered();
            var tmps = tmpsv.stream().filter(p -> p.getVendor().getMediator() != null && p.getVendor().getMediator().getId().equals(m.getId())).flatMap(g -> g.getProductList().stream()).toList();

            disp.setOne(tmps.stream().filter(c -> c.getProduct().getId().equals("I.OSZTÁLYÚ")).mapToInt(PurchasedProductDTO::getQuantity2).sum());
            disp.setTwo(tmps.stream().filter(c -> c.getProduct().getId().equals("II.OSZTÁLYÚ")).mapToInt(PurchasedProductDTO::getQuantity2).sum());
            disp.setThree(tmps.stream().filter(c -> c.getProduct().getId().equals("III.OSZTÁLYÚ")).mapToInt(PurchasedProductDTO::getQuantity2).sum());
            disp.setFour(tmps.stream().filter(c -> c.getProduct().getId().equals("IV.OSZTÁLYÚ")).mapToInt(PurchasedProductDTO::getQuantity2).sum());
            disp.setFive(tmps.stream().filter(c -> c.getProduct().getId().equals("GYÖKÉR")).mapToInt(PurchasedProductDTO::getQuantity2).sum());
            disp.setSix(tmps.stream().filter(c -> c.getProduct().getId().equals("IPARI")).mapToInt(PurchasedProductDTO::getQuantity2).sum());
            disp.setMediatorName(m.getId());
            mediatorDisplays.add(disp);
        });
    }


    @Autowired
    CompletionRecordService recordService;

    public String getFormattedRemainingPrice(int id) {
        return NumberFormat.getNumberInstance(Locale.US).format(getRemaningPrice(id)).replaceAll(",", " ");
    }

    public int getRemaningPrice(int id) {
        var temp = purchaseService.getPurchaseById(id);
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

    public String toDottedDate(java.util.Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+01"));

        return dt == null ? "0000.01.01" : sdf.format(dt);
    }

    public String getFormattedNumber(int num) {
        return NumberFormat.getNumberInstance(Locale.US).format(num).replaceAll(",", " ");
    }


    public Integer sumOnes() {
        if (CPRecords == null) return 0;
        return CPRecords.stream().mapToInt(CompletionRecordDTO::getOne).sum();
    }

    public Integer sumTwos() {
        if (CPRecords == null) return 0;
        return CPRecords.stream().mapToInt(CompletionRecordDTO::getTwo).sum();
    }

    public Integer sumThrees() {
        if (CPRecords == null) return 0;
        return CPRecords.stream().mapToInt(CompletionRecordDTO::getThree).sum();
    }

    public Integer sumFours() {
        if (CPRecords == null) return 0;
        return CPRecords.stream().mapToInt(CompletionRecordDTO::getFour).sum();
    }

    public Integer sumFives() {
        if (CPRecords == null) return 0;
        return CPRecords.stream().mapToInt(CompletionRecordDTO::getFive).sum();
    }

    public Integer sumSixes() {
        if (CPRecords == null) return 0;
        return CPRecords.stream().mapToInt(CompletionRecordDTO::getSix).sum();
    }

    public Integer sumPrices() {
        if (CPRecords == null) return 0;
        return CPRecords.stream().mapToInt(CompletionRecordDTO::getPrice).sum();
    }

}
