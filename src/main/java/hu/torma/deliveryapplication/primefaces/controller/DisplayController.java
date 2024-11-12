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

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;

@ViewScoped // TODO: fix incorrect annotations
@ManagedBean("displayController")
@DependsOn("dbInit")
public class DisplayController implements Serializable {
    @Getter @Setter String numSerial1;
    @Getter @Setter String numSerial2;
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
    @Getter @Setter private List<MediatorData> mediatorData;
    @Getter @Setter private PurchaseSumObj cpSumObj = generateEmptyPurchaseSumObj();
    @Getter @Setter private PurchaseSumObj splitCPSumObj = generateEmptyPurchaseSumObj();
    @Getter @Setter private PurchaseSumObj summage = generateEmptyPurchaseSumObj();
    @Getter @Setter private PurchaseSumObj purchaseSumObj = generateEmptyPurchaseSumObj();
    @Getter @Setter private Boolean felvJegy;

    @Autowired CompletedPurchaseService CPService;
    @Autowired MediatorService mediatorService;
    @Autowired PurchaseService purchaseService;
    @Autowired SaleService saleService;

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null && facesContext.isPostback()) {
            return;
        }

        felvJegy = false;
        purchaseDTOS = new ArrayList<>();
        CPDTOS = new ArrayList<>();
        saleDTOS = new ArrayList<>();
        mediatorData = new ArrayList<>();

        cpSumObj = generateEmptyPurchaseSumObj();
        splitCPSumObj = generateEmptyPurchaseSumObj();
        summage = generateEmptyPurchaseSumObj();
        purchaseSumObj = generateEmptyPurchaseSumObj();

        numSerial1 = null;
        numSerial2 = null;
        mediator = null;
        CPSums = null;
        shouldFilterByPaper = false;
        paidOnly = null;
        filterPaymentMethodCP = null;
        filterName = null;
        filterName4 = null;
        filterName2 = null;
        saleSumPojo = null;
        filterCurrency2 = null;
        filterPaper2 = null;
        filterUnpaidOnly2 = null;
        filterLetaiOnly2 = null;
        filterGlobalGapOnly2 = null;
        fullyPaidFilter = null;
        filterDateFrom = null;
        filterDateFrom2 = null;
        filterDateFrom3 = null;
        filterDateFrom4 = null;
        filterDateTo = null;
        filterDateTo2 = null;
        filterDateTo3 = null;
        filterDateTo4 = null;
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
        CPDTOS = CPService.getFilteredListOfCPs(filterName4 == null ? null : filterName4.getTaxId(), filterDateFrom4, filterDateTo4, Objects.equals(numSerial1, "") ? null : numSerial1, Objects.equals(numSerial2, "") ? null : numSerial2, paidOnly, filterPaymentMethodCP);
        cpSumObj = getSumsOfCPs(CPDTOS);
        return new ArrayList<>(CPDTOS);
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

    public String getFormattedNumber(Double num) {
        return NumberFormat.getNumberInstance(Locale.US).format(num).replaceAll(",", " ");
    }


    public PurchaseSumObj sumPurchases(List<PurchaseDTO> pDTOs) {
        if (pDTOs == null) {
            return new PurchaseSumObj(0, 0, 0, 0, 0, 0, 0.0, 0.0);
        }

        int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0;
        Double totalPriceSum = 0.0, remainingpriceSum = 0.0;

        for (var pur : pDTOs) {
            List<PurchasedProductDTO> productList = pur.getProductList();
            ones += productList.get(0).getQuantity2();
            twos += productList.get(1).getQuantity2();
            threes += productList.get(2).getQuantity2();
            fours += productList.get(3).getQuantity2();
            fives += productList.get(4).getQuantity2();
            sixes += productList.get(5).getQuantity2();

            totalPriceSum += pur.getTotalPrice();
            remainingpriceSum += pur.getRemainingPrice();
        }

        return new PurchaseSumObj(ones, twos, threes, fours, fives, sixes, totalPriceSum, remainingpriceSum);

    }

    public Integer getMediatorTotalPriceSumByMediator(String medName) {
        return mediatorData.stream().filter(med -> med.getMediatorName().equals(medName)).mapToInt(MediatorData::getTotalPrice).sum();
    }

    public PurchaseSumObj getSumsOfCPs(List<CompletedPurchaseDTO> cps) {
        if (cps == null) {
            return new PurchaseSumObj(0, 0, 0, 0, 0, 0, 0.0, 0.0);
        }

        var recs = cps.stream()
                .flatMap(cp -> Optional.ofNullable(cp.getRecords())
                        .stream()
                        .flatMap(List::stream))
                .toList();


        int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0;
        double totalPriceSum = cps.stream().mapToDouble(CompletedPurchaseDTO::getTotalPrice).sum();

        for (var pur : recs) {
            ones += pur.getOne();
            twos += pur.getTwo();
            threes += pur.getThree();
            fours += pur.getFour();
            fives += pur.getFive();
            sixes += pur.getSix();
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

    public PurchaseSumObj generateEmptyPurchaseSumObj() {
        return new PurchaseSumObj(0, 0, 0, 0, 0, 0, 0.0, 0.0);
    }
}
