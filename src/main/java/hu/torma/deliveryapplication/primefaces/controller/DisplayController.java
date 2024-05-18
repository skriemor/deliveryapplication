package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.primefaces.sumutils.MediatorData;
import hu.torma.deliveryapplication.primefaces.sumutils.PurchaseSumObj;
import hu.torma.deliveryapplication.primefaces.sumutils.SaleSumPojo;
import hu.torma.deliveryapplication.service.*;
import hu.torma.deliveryapplication.utility.dateutil.DateConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@ViewScoped // TODO: fix incorrect annotations
@ManagedBean(name = "displayController")
@DependsOn("dbInit")
public class DisplayController implements Serializable {
    @Autowired CompletionRecordService recordService;
    @Getter @Setter List<MediatorData> mediatorData;
    @Getter @Setter List<CompletionRecordDTO> CPRecords;
    @Getter @Setter String numSerial1, numSerial2;
    @Getter @Setter MediatorDTO mediator;
    @Getter @Setter private CompletedPurchaseDTO CPSums;
    @Getter @Setter private boolean shouldFilterByPaper;
    @Getter @Setter private Boolean paidOnly;
    @Getter @Setter private String filterPaymentMethodCP;
    @Getter @Setter private VendorDTO filterName;
    @Getter @Setter private VendorDTO filterName4;
    @Getter @Setter private BuyerDTO filterName2;
    @Getter @Setter private SaleSumPojo saleSumPojo;
    @Getter @Setter private String filterCurrency2;
    @Getter @Setter private String filterPaper2;
    @Getter @Setter private Boolean filterUnpaidOnly2;
    @Getter @Setter private Boolean filterLetaiOnly2;
    @Getter @Setter private Boolean filterGlobalGapOnly2;
    @Getter @Setter private Boolean fullyPaidFilter;
    @Getter @Setter private Date filterDateFrom;
    @Getter @Setter private Date filterDateFrom2;
    @Getter @Setter private Date filterDateFrom3;
    @Getter @Setter private Date filterDateFrom4;
    @Getter @Setter private Date filterDateTo;
    @Getter @Setter private Date filterDateTo2;
    @Getter @Setter private Date filterDateTo3;
    @Getter @Setter private Date filterDateTo4;
    @Getter @Setter private List<PurchaseDTO> purchaseDTOS;
    @Getter @Setter private List<CompletedPurchaseDTO> CPDTOS;
    @Getter @Setter private List<SaleDTO> saleDTOS;
    @Getter @Setter private List<PurchaseDTO> mediatorDisplaysP;
    @Getter @Setter private List<CompletedPurchaseDTO> mediatorDisplaysCP;
    @Getter @Setter PurchaseSumObj cpSumObj = new PurchaseSumObj(0, 0, 0, 0, 0, 0, 0.0, 0.0);
    @Getter @Setter PurchaseSumObj splitCPSumObj = new PurchaseSumObj(0, 0, 0, 0, 0, 0, 0.0, 0.0);
    @Getter @Setter PurchaseSumObj summage = new PurchaseSumObj(0, 0, 0, 0, 0, 0, 0.0, 0.0);
    @Getter @Setter Boolean felvJegy;

    @Autowired StorageService storageService;
    @Autowired CompletedPurchaseService CPService;
    @Autowired ProductService productService;
    @Autowired MediatorService mediatorService;
    @Autowired UnitService unitService;


    @Autowired PurchaseService purchaseService;

    @Autowired SaleService saleService;

    @PostConstruct
    public void init() {
        felvJegy = false;
        purchaseDTOS = new ArrayList<>();
        CPDTOS = new ArrayList<>();
        saleDTOS = new ArrayList<>();
    }

    public List<PurchaseDTO> refreshPurchaseDTOS() {
        purchaseDTOS = purchaseService.applyFilterChainAndReturnPurchases(filterName == null ? null : filterName.getTaxId(), filterDateFrom, filterDateTo, fullyPaidFilter);
        purchaseSumObj = sumPurchases(purchaseDTOS);
        return new ArrayList<>(purchaseDTOS);
    }

    public ArrayList<SaleDTO> refreshSaleDTOS() {
        saleDTOS = saleService.applyFilterChainAndReturnSales(filterName2 == null ? null : filterName2.getAccountNum(), filterCurrency2, filterDateFrom2, filterDateTo2, filterUnpaidOnly2, filterPaper2, filterLetaiOnly2, filterGlobalGapOnly2);
        this.saleSumPojo = new SaleSumPojo(saleDTOS);
        return new ArrayList<>(saleDTOS);
    }

    public ArrayList<CompletedPurchaseDTO> refreshCPDTOS() {
        CPDTOS = CPService.getFilteredListOfCPs(filterName4 == null ? null : filterName4.getTaxId(), filterDateFrom4, filterDateTo4, numSerial1 == "" ? null : numSerial1, numSerial2 == "" ? null : numSerial2, paidOnly, filterPaymentMethodCP);
        cpSumObj = getSumsOfCPs(CPDTOS);
        return new ArrayList<>(CPDTOS);
    }

    public void setFelvJegy(Boolean b) {
        felvJegy = b;
    }

    public void refreshMediatorDisplays() {
        if (felvJegy == null) return;
        if (felvJegy) {
            mediatorDisplaysCP = CPService.getCompletedPurchasesByMediatorIdAndDates(filterDateFrom3, filterDateTo3, mediator == null || mediator.getId() == null ? null : mediator.getId());
            splitCPSumObj = getSumsOfCPs(mediatorDisplaysCP);
        } else {
            mediatorDisplaysP = purchaseService.getPurchasesByMediatorIdAndDates(filterDateFrom3, filterDateTo3, mediator == null || mediator.getId() == null ? null : mediator.getId());
            summage = sumPurchases(mediatorDisplaysP);
        }
    }


    public void generatePurhcaseDisplays() {
        mediatorData = mediatorService.getMediatorData(filterDateFrom3, filterDateTo3);
    }

    public String toDottedDate(java.util.Date dt) {
        return DateConverter.toDottedDate(dt);
    }

    public String getFormattedNumber(int num) {
        return NumberFormat.getNumberInstance(Locale.US).format(num).replaceAll(",", " ");
    }

    PurchaseSumObj purchaseSumObj = new PurchaseSumObj();

    public PurchaseSumObj sumPurchases(List<PurchaseDTO> pDTOs) {
        int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0;
        Double totalPriceSum = 0.0, remainingpriceSum = 0.0;

        for (var pur : pDTOs) {
            ones += pur.getProductList().get(0).getQuantity2();
            twos += pur.getProductList().get(1).getQuantity2();
            threes += pur.getProductList().get(2).getQuantity2();
            fours += pur.getProductList().get(3).getQuantity2();
            fives += pur.getProductList().get(4).getQuantity2();
            sixes += pur.getProductList().get(5).getQuantity2();

            totalPriceSum += pur.getTotalPrice();
            remainingpriceSum += pur.getRemainingPrice();
        }

        return new PurchaseSumObj(ones, twos, threes, fours, fives, sixes, totalPriceSum, remainingpriceSum);

    }

    public Integer getMediatorTotalPriceSumByMediator(String medName) {
        return mediatorData.stream().filter(med -> med.getMediatorName().equals(medName)).mapToInt(MediatorData::getTotalPrice).sum();
    }

    public PurchaseSumObj getSumsOfCPs(List<CompletedPurchaseDTO> cps) {
        var recs = cps.stream().flatMap(cp -> cp.getRecords().stream()).toList();
        int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0;
        Double totalPriceSum = 0.0;

        for (var pur : recs) {
            ones += pur.getOne();
            twos += pur.getTwo();
            threes += pur.getThree();
            fours += pur.getFour();
            fives += pur.getFive();
            sixes += pur.getSix();

            totalPriceSum += pur.getPrice();
        }
        return new PurchaseSumObj(ones, twos, threes, fours, fives, sixes, totalPriceSum, 0.0);
    }

    public Integer getSumOfSumObj(PurchaseSumObj obj) {
        if (obj == null) return 0;
        return obj.getOne() + obj.getTwo() + obj.getThree() + obj.getFour() + obj.getFive() + obj.getSix();
    }

    public Integer getSumOfSaleSum(SaleSumPojo obj) {
        if (obj == null) return 0;
        return obj.getOne() + obj.getTwo() + obj.getThree() + obj.getFour() + obj.getFive() + obj.getSix();
    }
}
