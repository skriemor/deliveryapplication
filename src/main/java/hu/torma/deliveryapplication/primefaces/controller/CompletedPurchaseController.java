package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.entity.PurchasedProduct;
import hu.torma.deliveryapplication.service.*;
import hu.torma.deliveryapplication.utility.Quant;
import hu.torma.deliveryapplication.utility.dateutil.DateConverter;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.IntStream;

@ViewScoped
@ManagedBean("completedPurchaseController")
@DependsOn("dbInit")
public class CompletedPurchaseController implements Serializable {
    @Autowired StorageService sService;
    @Autowired SiteService siteService;
    @Autowired CompletedPurchaseService cService;
    @Autowired ProductService pService;
    @Autowired UnitService uService;
    @Autowired PurchaseService OPservice;
    @Autowired private PurchaseService purchaseService;
    @Autowired private CompletionRecordService recordService;
    @Getter @Setter private PurchaseDTO pItemForSelectOneMenu;
    @Getter @Setter private List<CompletionRecordDTO> tempRecords;
    @Getter @Setter List<CompletionRecordDTO> validRecords;
    List<CompletionRecordDTO> beforeEditList;
    @Setter private Integer selectionCounter;
    private final List<String> tempNamesList = new ArrayList<>(Arrays.asList("I.OSZTÁLYÚ", "II.OSZTÁLYÚ", "III.OSZTÁLYÚ", "IV.OSZTÁLYÚ", "GYÖKÉR", "IPARI"));
    @Getter @Setter private ArrayList<Quant> quantities = new ArrayList<>(Arrays.asList(new Quant(0), new Quant(0), new Quant(0), new Quant(0), new Quant(0), new Quant(0)));
    @Getter @Setter private List<SortMeta> sortBy;
    @Getter @Setter public ArrayList<PurchaseDTO> availablePurchases;
    @Getter @Setter private PurchaseDTO purchaseDTO;
    @Getter @Setter private String label;
    @Getter List<CompletedPurchasegDTO> dtoList;
    @Setter private CompletedPurchaseDTO dto;
    @Getter private Integer sixTotal;
    @Getter private Double netAvgPrice;
    @Getter private Double diff;
    @Getter private Double grossTotal;
    @Getter private Double grossAvgPrice;
    @Getter private Double netTotal;
    @Getter private Integer netSum;
    @Getter private Integer dtoWeight;
    @Getter private Integer dtoTotalV;
    @Getter private Double netTotalV;
    @Getter private Double grossTotalV;
    @Getter private Double grossAvgPriceV;
    @Getter private Double netAvgPriceV;
    @Getter private Double diffV;

    public Integer getSelectionCounter() {
        if (selectionCounter == null) {
            selectionCounter = 0;
        }
        return selectionCounter;
    }

    public void nextSelection() {
        if (tempRecords != null) {
            if (selectionCounter + 1 >= tempRecords.size()) {
                selectionCounter = 0;
            } else {
                selectionCounter++;
            }
        }
    }

    private void initPItem() {
        this.pItemForSelectOneMenu = new PurchaseDTO();
        pItemForSelectOneMenu.setId(0);
        pItemForSelectOneMenu.setRemainingPrice(0.0);
        VendorDTO vend = new VendorDTO();
        vend.setVendorName("");
        pItemForSelectOneMenu.setVendor(vend);
    }

    public void reInitCalculatedNumbers() {
        sixTotal = calculateSixTotal();
        netSum = calculateNetSum();
        grossAvgPrice = calculateGrossAvgPrice();
        grossTotal = calculateGrossTotal();
        netTotal = calculateNetTotal();
        netAvgPrice = calculateNetAvgPrice();
        diff = calculateDiff();

        dtoWeight = calculateDtoWeight();
        dtoTotalV = calculateDtoTotalV();
        grossAvgPriceV = calculateGrossAvgPriceV();
        grossTotalV = calculateGrossTotalV();
        netTotalV = calculateNetTotalV();
        netAvgPriceV = calculateNetAvgPriceV();
        diffV = calculateDiffV();
    }

    private Integer calculateSixTotal() {
        if (purchaseDTO == null || purchaseDTO.getProductList() == null) {
            return 0;
        }

        List<PurchasedProductDTO> list = purchaseDTO.getProductList();
        return IntStream.range(0, Math.min(6, list.size())).map(i -> (int) (list.get(i).getUnitPrice() * quantities.get(i).getNum() * (1 + 0.01 * list.get(i).getProduct().getCompPercent()))).sum();
    }

    private Double calculateNetAvgPrice() {
        return (double) Math.round(grossAvgPrice / 1.12 * 100) / 100;
    }

    private Double calculateDiff() {
        return grossTotal - netTotal;
    }

    private Double calculateGrossTotal() {
        return (double) Math.round(grossAvgPrice * netSum);
    }

    private Double calculateGrossAvgPrice() {
        return (double) Math.round(sixTotal / (double) netSum * 100) / 100;
    }

    private Double calculateNetTotal() {
        return (double) Math.round(grossTotal / 1.12);
    }

    private Integer calculateNetSum() {
        return quantities.stream().mapToInt(Quant::getNum).sum();
    }

    private Integer calculateDtoWeight() {
        return IntStream.range(0, Math.min(6, tempRecords.size())).map(this::getTotalAmountOf).sum();
    }

    private Integer calculateDtoTotalV() {
        return tempRecords.stream().mapToInt(CompletionRecordDTO::getPrice).sum();
    }

    private Double calculateNetTotalV() {
        return (double) Math.round(grossTotalV / 1.12);
    }

    private Double calculateGrossTotalV() {
        return (double) Math.round(grossAvgPriceV * dtoWeight);
    }

    private Double calculateGrossAvgPriceV() {
        return (double) Math.round(dtoTotalV / (double) dtoWeight * 100) / 100;
    }

    private Double calculateNetAvgPriceV() {
        return (double) Math.round(grossAvgPriceV / 1.12 * 100) / 100;
    }

    private Double calculateDiffV() {
        return grossTotalV - netTotalV;
    }

    public Integer getPriceOf(int i) {
        if (quantities.get(i).getNum() == 0 || purchaseDTO.getProductList() == null) return 0;
        var g = purchaseDTO.getProductList().get(i);
        return (int) (g.getUnitPrice() * quantities.get(i).getNum() * (1 + (0.01 * g.getProduct().getCompPercent())));
    }

    public String getFormattedPriceOf(int g) {
        return NumberFormat.getNumberInstance(Locale.US).format(getPriceOf(g)).replaceAll(",", " ");
    }

    public String getSumOfRecordPrices() {
        return NumberFormat.getNumberInstance(Locale.US).format(dtoTotalV).replaceAll(",", " ");
    }

    public String getFormattedNumber(double num) {
        if (num - (int) num == 0.0) {
            return NumberFormat.getNumberInstance(Locale.US).format((int) num).replaceAll(",", " ");
        }
        return NumberFormat.getNumberInstance(Locale.US).format(num).replaceAll(",", " ");
    }

    public String getFormattedSixTotal() {
        return NumberFormat.getNumberInstance(Locale.US).format(sixTotal).replaceAll(",", " ");
    }

    @Transactional
    public void updateRemainingPrice(int id) {
        var temp = purchaseService.getPurchaseById(id);
        var tempList = temp.getProductList();
        if (tempList == null) return;
        var total = temp.getTotalPrice();
        var records = recordService.findAllByPurchaseId(id);
        StringBuilder tempstring = new StringBuilder();
        for (var r : records) {
            total -= (int) (tempList.get(0).getUnitPrice() * r.getOne() * (1 + (0.01 * tempList.get(0).getProduct().getCompPercent())));
            total -= (int) (tempList.get(1).getUnitPrice() * r.getTwo() * (1 + (0.01 * tempList.get(1).getProduct().getCompPercent())));
            total -= (int) (tempList.get(2).getUnitPrice() * r.getThree() * (1 + (0.01 * tempList.get(2).getProduct().getCompPercent())));
            total -= (int) (tempList.get(3).getUnitPrice() * r.getFour() * (1 + (0.01 * tempList.get(3).getProduct().getCompPercent())));
            total -= (int) (tempList.get(4).getUnitPrice() * r.getFive() * (1 + (0.01 * tempList.get(4).getProduct().getCompPercent())));
            total -= (int) (tempList.get(5).getUnitPrice() * r.getSix() * (1 + (0.01 * tempList.get(5).getProduct().getCompPercent())));
            tempstring.append(r.getCompletedPurchase().getNewSerial()).append(" ");
        }
        temp.setReceiptId(tempstring.toString());
        temp.setRemainingPrice(total);
        purchaseService.savePurchase(temp);
    }

    private void newCP() {
        dto = new CompletedPurchaseDTO();
        dto.setRecords(new ArrayList<>());
        tempRecords = new ArrayList<>();
        beforeEditList = new ArrayList<>();
        purchaseDTO = new PurchaseDTO();
        updateAvailablePurchases();
        emptySix();
    }

    private void copySerials(List<CompletedPurchaseDTO> list) {
        if (list != null && list.size() > 0 && (list.get(0).getNewSerial() == null || list.get(0).getNewSerial().equals(""))) {
            for (var g : list) {
                if (g.getSerial() != null && g.getSerial() != 0) {
                    String v = String.valueOf(g.getSerial());
                    if (v.charAt(0) != '0') v = 0 + v;
                    g.setNewSerial(v);
                    cService.saveCompletedPurchase(g);
                }
            }
        }
    }

    @PostConstruct
    public void init() {
        initPItem();
        newCP();
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder().field("id").order(SortOrder.ASCENDING).build());
        reInitCalculatedNumbers();
    }

    private void updateRemainingPrices() {
        if (beforeEditList != null) for (var c : beforeEditList) {
            updateRemainingPrice(c.getPurchase().getId());
        }
        if (dto.getRecords() != null) for (var c : dto.getRecords()) {
            updateRemainingPrice(c.getPurchase().getId());
        }
    }

    public void uiSaveCompletedPurchase() {
        if (this.dto == null) return;
        if (this.dto.getSite() == null) {
            var sites = siteService.getAllSites();
            if (sites != null && sites.size() > 0) this.dto.setSite(siteService.getAllSites().get(0));
        }
        dto.setRecords(tempRecords);
        Date date = new Date(System.currentTimeMillis());
        this.dto.setBookedDate(date);
        if (this.dto.getRecords() == null || this.dto.getRecords().size() < 1) {
            this.dto.setTotalPrice(0);
        }
        if (this.dto.getRecords() != null && this.dto.getRecords().size() > 0) {
            this.dto.setTotalPrice(getGrossTotalV().intValue());
        }

        cService.saveCompletedPurchase(dto);
        updateRemainingPrices();
        tempRecords.clear();
        this.purchaseDTO = new PurchaseDTO();
        emptySix();
        newCP();
    }

    public void deletePurchase() {
        cService.deleteCompletedPurchaseById(dto.getId());
        updateRemainingPrices();
        newCP();
        selectionCounter = 0;
    }

    public java.util.Date getEarliestPurchaseOf(CompletedPurchaseDTO completedPurchase) {
        return completedPurchase.getRecords().stream()
                .map(CompletionRecordDTO::getPurchase)
                .map(PurchaseDTO::getReceiptDate)
                .min(java.util.Date::compareTo)
                .orElse(null);
    }

    public void editPurchase(SelectEvent<CompletedPurchaseDTO> _dto) {
        dto = cService.getCompletedPurchaseById(_dto.getObject().getId());
        tempRecords = dto.getRecords();
        beforeEditList = new ArrayList<>(tempRecords);

        if (!beforeEditList.isEmpty()) {
            purchaseDTO = purchaseService.getRecordlessPurchaseById(beforeEditList.get(0).getPurchase().getId());
            acquireRecords();
            acquireQuants();
            this.pItemForSelectOneMenu = purchaseDTO;
        } else {
            validRecords = new ArrayList<>();
            purchaseDTO = null;
        }
        updateAvailablePurchases();
        selectionCounter = 0;
        reInitCalculatedNumbers();
    }

    private void acquireQuants() {
        var preEditFirst = beforeEditList.get(0);
        quantities.get(0).setNum(preEditFirst.getOne());
        quantities.get(1).setNum(preEditFirst.getTwo());
        quantities.get(2).setNum(preEditFirst.getThree());
        quantities.get(3).setNum(preEditFirst.getFour());
        quantities.get(4).setNum(preEditFirst.getFive());
        quantities.get(5).setNum(preEditFirst.getSix());
    }

    private void emptySix() {
        quantities.forEach(quant -> quant.setNum(0));
    }

    public void newPurchase() {
        tempRecords.clear();
        emptySix();
        newCP();
        this.purchaseDTO = new PurchaseDTO();
        this.dto = new CompletedPurchaseDTO();
        this.setLabel("Felv. jegy Hozzáadása");
        updateAvailablePurchases();
        selectionCounter = 0;
    }

    public CompletedPurchaseDTO getDto() {
        if (this.dto == null) {
            this.dto = new CompletedPurchaseDTO();
        }
        return this.dto;
    }

    @PostConstruct
    public void getAllPurchases() {
        this.dtoList = cService.getCompletedPurchasesForListing();
    }

    public int getMaxQuantOf(int i) {
        if (purchaseDTO == null || purchaseDTO.getProductList() == null) {
            return 0;
        }

        int original = purchaseDTO.getProductList().get(i).getQuantity2();
        int toSub = 0;

        switch (i) {
            case 0 -> toSub = validRecords.stream().mapToInt(CompletionRecordDTO::getOne).sum();
            case 1 -> toSub = validRecords.stream().mapToInt(CompletionRecordDTO::getTwo).sum();
            case 2 -> toSub = validRecords.stream().mapToInt(CompletionRecordDTO::getThree).sum();
            case 3 -> toSub = validRecords.stream().mapToInt(CompletionRecordDTO::getFour).sum();
            case 4 -> toSub = validRecords.stream().mapToInt(CompletionRecordDTO::getFive).sum();
            case 5 -> toSub = validRecords.stream().mapToInt(CompletionRecordDTO::getSix).sum();
        }
        return original - toSub;
    }

    public Integer getTotalAmountOf(int i) {
        return switch (i) {
            case 0 -> tempRecords.stream().mapToInt(CompletionRecordDTO::getOne).sum();
            case 1 -> tempRecords.stream().mapToInt(CompletionRecordDTO::getTwo).sum();
            case 2 -> tempRecords.stream().mapToInt(CompletionRecordDTO::getThree).sum();
            case 3 -> tempRecords.stream().mapToInt(CompletionRecordDTO::getFour).sum();
            case 4 -> tempRecords.stream().mapToInt(CompletionRecordDTO::getFive).sum();
            case 5 -> tempRecords.stream().mapToInt(CompletionRecordDTO::getSix).sum();
            default -> 0;
        };
    }

    public void addRecord() {
        if (this.purchaseDTO.getId() == null) {
            return;
        }
        if (this.dto.getRecords() == null) {
            this.dto.setRecords(new ArrayList<>());
        }
        var recordDTO = new CompletionRecordDTO();
        StringBuilder sB = new StringBuilder();

        boolean wasWrong = false;
        for (int i = 0; i < 6; i++) {
            if (quantities.get(i).getNum() > getMaxQuantOf(i)) {
                wasWrong = true;
                sB.append(tempNamesList.get(i)).append(", ");
            }
        }
        if (sB.length() > 1 || wasWrong) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "HIBA", "Helytelen mennyiség: " + sB.substring(0, sB.length() - 2)));
            return;
        }

        recordDTO.setOne(quantities.get(0).getNum());
        recordDTO.setTwo(quantities.get(1).getNum());
        recordDTO.setThree(quantities.get(2).getNum());
        recordDTO.setFour(quantities.get(3).getNum());
        recordDTO.setFive(quantities.get(4).getNum());
        recordDTO.setSix(quantities.get(5).getNum());
        recordDTO.setPurchase(purchaseDTO);

        recordDTO.setCompletedPurchase(this.dto);

        recordDTO.setPrice(getSixTotal());
        tempRecords = new ArrayList<>(tempRecords.stream().filter(a -> recordDTO.getPurchase().getId().equals(a.getPurchase().getId())).toList());
        tempRecords.add(recordDTO);
        this.purchaseDTO = new PurchaseDTO();
        emptySix();
        updateAvailablePurchases();
    }

    public String toDottedDate(java.util.Date dt) {
        return DateConverter.toDottedDate(dt);
    }

    public void removeRecord2() {
        if (tempRecords == null || selectionCounter >= tempRecords.size()) return;
        pItemForSelectOneMenu = tempRecords.get(selectionCounter).getPurchase();
        tempRecords.remove(selectionCounter.intValue());
        emptySix();
        updateAvailablePurchases();
        selectionCounter = 0;
    }

    public void updateAvailablePurchases() {
        List<Integer> usedIDs = tempRecords != null && !tempRecords.isEmpty() ?
                tempRecords.stream().map(CompletionRecordDTO::getPurchase).map(PurchaseDTO::getId).toList() :
                Collections.emptyList();

        availablePurchases = new ArrayList<>(OPservice.getAllPurchasesForSelection().stream()
                .filter(c -> !usedIDs.contains(c.getId()))
                .filter(v -> v.getRemainingPrice() != 0)
                .toList());

        if (beforeEditList != null) {
            PurchaseDTO purchaseDTO1;
            List<CompletionRecordDTO> priceRecords;

            for (var a : beforeEditList) {
                boolean wasFound = false;
                for (var b : tempRecords) {
                    if (Objects.equals(b.getPurchase().getId(), a.getPurchase().getId())) {
                        wasFound = true;
                        break;
                    }
                }
                for (var h : availablePurchases) {
                    if (Objects.equals(h.getId(), a.getPurchase().getId())) {
                        wasFound = true;
                        break;
                    }
                }
                if (!wasFound) {
                    priceRecords = recordService.findAllByPurchaseId(a.getPurchase().getId());
                    purchaseDTO1 = OPservice.getPurchaseForSelectionById(a.getPurchase().getId());

                    purchaseDTO1.setRemainingPrice(purchaseDTO1.getTotalPrice() - (double) priceRecords.stream().mapToInt(CompletionRecordDTO::getPrice).sum() + a.getPrice());
                    availablePurchases.add(purchaseDTO1);
                }
            }
        }
    }

    public void acquireRecords() {
       if (dto != null && dto.getRecords() != null && dto.getId() != null) {
            validRecords = dto.getRecords().stream()
                    .filter(record -> !Objects.equals(record.getCompletedPurchase().getId(), dto.getId()))
                    .toList();
       }
    }
}
