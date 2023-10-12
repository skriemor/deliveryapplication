package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.primefaces.sumutils.MediatorData;
import hu.torma.deliveryapplication.primefaces.sumutils.PurchaseSumObj;
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
import java.util.*;
import java.util.logging.Logger;

@SessionScope
@Controller("displayController")
@Setter
@Getter
public class DisplayController implements Serializable {
    List<MediatorData> mediatorData;

    List<CompletionRecordDTO> CPRecords;
    String numSerial;

    private CompletedPurchaseDTO CPSums;

    private boolean shouldFilterByPaper, paidOnly;

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


    private void doFilterMediator() {
        mediatorDTOS = mediatorService.getAllMediators();
    }


    public List<PurchaseDTO> refreshPurchaseDTOS() {
        purchaseDTOS = purchaseService.applyFilterChainAndReturnPurchases(filterName, filterDateFrom, filterDateTo, fullyPaidFilter);
        sumPurchases();
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

    public void refreshMediatorDisplays() {
        var tmp = new ArrayList<MediatorDisplay>();
        mediatorDTOS = mediatorService.getAllMediators();
        doFilterMediator();
        generateDisplays();
    }


    Logger log = Logger.getLogger("MEDIATOR");

    public void generateDisplays() {
        mediatorData = mediatorService.getMediatorData(filterDateFrom3,filterDateTo3);
    }


    @Autowired
    CompletionRecordService recordService;


    public String toDottedDate(java.util.Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+01"));

        return dt == null ? "0000.01.01" : sdf.format(dt);
    }

    public String getFormattedNumber(int num) {
        return NumberFormat.getNumberInstance(Locale.US).format(num).replaceAll(",", " ");
    }

    PurchaseSumObj purchaseSumObj = new PurchaseSumObj();

    public void sumPurchases() {
        int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0;
        Double totalPriceSum = 0.0, remainingpriceSum = 0.0;

        for (var pur : purchaseDTOS) {
            ones += pur.getProductList().get(0).getQuantity2();
            twos += pur.getProductList().get(1).getQuantity2();
            threes += pur.getProductList().get(2).getQuantity2();
            fours += pur.getProductList().get(3).getQuantity2();
            fives += pur.getProductList().get(4).getQuantity2();
            sixes += pur.getProductList().get(5).getQuantity2();

            totalPriceSum += pur.getTotalPrice();
            remainingpriceSum += pur.getRemainingPrice();
        }

        purchaseSumObj = new PurchaseSumObj(ones, twos, threes, fours, fives, sixes, totalPriceSum, remainingpriceSum);

    }

    public Integer getMediatorTotalPriceSumByMediator(String medName){
        return mediatorData.stream().filter(med->med.getMediatorName().equals(medName)).mapToInt(MediatorData::getTotalPrice).sum();
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
