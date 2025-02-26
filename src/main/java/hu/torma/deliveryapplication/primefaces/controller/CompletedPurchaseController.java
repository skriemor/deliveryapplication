package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.entity.CompletedPurchase;
import hu.torma.deliveryapplication.entity.CompletionRecord;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.entity.Site;
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
import javax.persistence.Tuple;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ViewScoped
@ManagedBean("completedPurchaseController")
@DependsOn("dbInit")
public class CompletedPurchaseController implements Serializable {
    @Autowired SiteService siteService;
    @Autowired CompletedPurchaseService cService;
    @Autowired PurchaseService OPservice;
    @Autowired private PurchaseService purchaseService;
    @Autowired private CompletionRecordService recordService;
    @Getter @Setter private PurchaseDTO pItemForSelectOneMenu;
    @Getter @Setter private List<CompletionRecordDTO> tempRecords;
    @Getter @Setter List<CompletionRecordDTO> recordsThatSubTheSelectedPurchase = new ArrayList<>();
    private List<CompletionRecordDTO> beforeEditList;
    @Setter private Integer selectionCounter;
    private final List<String> tempNamesList = new ArrayList<>(Arrays.asList("I.OSZTÁLYÚ", "II.OSZTÁLYÚ", "III.OSZTÁLYÚ", "IV.OSZTÁLYÚ", "GYÖKÉR", "IPARI"));
    @Getter @Setter private ArrayList<Quant> quantities = new ArrayList<>(Arrays.asList(new Quant(0), new Quant(0), new Quant(0), new Quant(0), new Quant(0), new Quant(0)));
    @Getter @Setter private List<SortMeta> sortBy;
    @Getter @Setter public ArrayList<PurchaseDTO> availablePurchases;
    @Getter @Setter private PurchaseDTO purchaseDTO;
    @Getter @Setter private String label;
    @Getter List<CompletedPurchaseDTO> dtoList;
    @Setter private CompletedPurchaseDTO dto;
    @Getter private Double sixTotal;
    @Getter private Double netAvgPrice;
    @Getter private Double diff;
    @Getter private Double grossTotal;
    @Getter private Double grossAvgPrice;
    @Getter private Double netTotal;
    @Getter private Double netSum;
    @Getter private Integer dtoWeight;
    @Getter private Double dtoTotalV;
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

    private Double calculateSixTotal() {
        if (purchaseDTO == null || purchaseDTO.getProductList() == null) {
            return 0.0;
        }

        List<PurchasedProductDTO> list = purchaseDTO.getProductList();
        return IntStream.range(0, Math.min(6, list.size())).mapToDouble(i -> (list.get(i).getUnitPrice() * quantities.get(i).getNum() * (1 + 0.01 * list.get(i).getProduct().getCompPercent()))).sum();
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

    private Double calculateNetSum() {
        return quantities.stream().mapToDouble(Quant::getNum).sum();
    }

    private Integer calculateDtoWeight() {
        return tempRecords.stream().mapToInt(record ->
                  record.getOne()
                + record.getTwo()
                + record.getThree()
                + record.getFour()
                + record.getFive()
                + record.getSix()
        ).sum();
    }

    private Double calculateDtoTotalV() {
        return tempRecords.stream().mapToDouble(CompletionRecordDTO::getPrice).sum();
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
        if (quantities.get(i).getNum() == 0 || purchaseDTO == null || purchaseDTO.getProductList() == null) return 0;
        var g = purchaseDTO.getProductList().get(i);
        return (int) (g.getUnitPrice() * quantities.get(i).getNum() * 1.00);
    }

    public String getFormattedPriceOf(int g) {
        return NumberFormat.getNumberInstance(Locale.US).format(getPriceOf(g)).replaceAll(",", " ");
    }

    public String getSumOfRecordPrices() {
        return NumberFormat.getNumberInstance(Locale.US).format(dtoTotalV).replaceAll(",", " ");
    }

    public String getFormattedNumber(double num) {
        return NumberFormat.getNumberInstance(Locale.US).format(num).replace(",", " ");
    }

    public String getFormattedNumber(Integer num) {
        return NumberFormat.getNumberInstance(Locale.US).format(num).replace(",", " ");
    }

    public String getFormattedSixTotal() {
        return NumberFormat.getNumberInstance(Locale.US).format(sixTotal).replaceAll(",", " ");
    }

    public void updateRemainingPrice(int id) {
        evaluateAndSetPurchaseRemainingPrice(id);
    }

    private void evaluateAndSetPurchaseRemainingPrice(int id) {
        Purchase purchase = purchaseService.getPurchaseEntityById(id);
        if (purchase != null) {
            Tuple priceAndSerials = purchaseService.getConcatedSerialsAndMaskedPricesById(id);
            Double newRemainingPrice = priceAndSerials == null ? purchase.getTotalPrice() : purchase.getTotalPrice() - ((BigInteger) priceAndSerials.get(0)).doubleValue();
            String newConcatenatedReceipt = priceAndSerials == null ? "" : (String) priceAndSerials.get(1);

            purchase.setRemainingPrice(newRemainingPrice);
            purchase.setReceiptId(newConcatenatedReceipt);
            purchaseService.savePurchase(purchase);
        }
    }

    private void newCP() {
        dto = new CompletedPurchaseDTO();
        dto.setRecords(new ArrayList<>());
        tempRecords = new ArrayList<>();
        beforeEditList = new ArrayList<>();
        purchaseDTO = new PurchaseDTO();
        updateAvailablePurchases();
        emptySix();
        selectionCounter = 0;
    }


    @PostConstruct
    public void init() {
        initPItem();
        newCP();
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder().field("id").order(SortOrder.ASCENDING).build());
        reInitCalculatedNumbers();
    }

    public void updateRemainingPrices(CompletedPurchase purchaseFromDb) {
        if (beforeEditList != null) for (var c : beforeEditList) {
            updateRemainingPrice(c.getPurchaseId());
        }

        Set<Integer> updatedIds =
                beforeEditList == null ? Collections.emptySet() :
                beforeEditList.stream().map(CompletionRecordDTO::getPurchaseId).collect(Collectors.toSet());

        if (purchaseFromDb != null && purchaseFromDb.getRecords() != null) {
            purchaseFromDb.getRecords()
                    .stream()
                    .map(CompletionRecord::getPurchase)
                    .filter(purchase -> !updatedIds.contains(purchase.getId()))
                    .mapToInt(Purchase::getId)
                    .forEach(this::updateRemainingPrice);
        }
    }

    private boolean validateCompletedPurchaseDto() {
        if (this.dto == null) {
            return false;
        }

        if (dto.getVendor() == null || dto.getVendor().getVendorName() == null || dto.getVendor().getTaxId() == null) {
            showErrorMessageToUser("Kérem válasszon termelőt a fenti választóban!");
            return false;
        }

        return true;
    }

    public void uiSaveCompletedPurchase() {
        if (!validateCompletedPurchaseDto()) {
            return;
        }

        CompletedPurchase entity = dto.toEntity(true, true);

        if (entity.getSite() == null) {
            List<Site> sites = siteService.getAllEntities();
            if (sites != null && !sites.isEmpty()) {
                entity.setSite(sites.get(0));
            }
        }

        List<CompletionRecord> recordsToSave = tempRecords.stream().map(record -> record.toEntity(true, true)).toList();
        entity.setRecords(recordsToSave);
        entity.setTotalPrice(tempRecords.isEmpty() ? 0.0  : grossTotalV);

        CompletedPurchase purchaseFromDb = cService.saveCompletedPurchase(entity);

        updateRemainingPrices(purchaseFromDb);
        tempRecords.clear();
        this.purchaseDTO = new PurchaseDTO();
        emptySix();
        newCP();
        getAllPurchases();
        reInitCalculatedNumbers();
    }

    public void deletePurchase() {
        cService.deleteCompletedPurchaseById(dto.getId());
        updateRemainingPrices(null);
        newPurchase();
        selectionCounter = 0;
    }

    public java.util.Date getEarliestPurchaseOf(CompletedPurchaseDTO completedPurchase) {
        return completedPurchase.getRecords().stream()
                .map(CompletionRecordDTO::getPurchase)
                .map(PurchaseDTO::getReceiptDate)
                .min(java.util.Date::compareTo)
                .orElse(Date.valueOf(LocalDate.now()));
    }

    public void editPurchase(SelectEvent<CompletedPurchaseDTO> _dto) {
        dto = cService.getCompletedPurchaseById(_dto.getObject().getId());
        tempRecords = dto.getRecords();
        beforeEditList = new ArrayList<>(tempRecords);

        if (!beforeEditList.isEmpty()) {
            purchaseDTO = purchaseService.getPurchaseById(beforeEditList.get(0).getPurchaseId());
            acquireRecords();
            acquireQuants();
            this.pItemForSelectOneMenu = purchaseDTO;
        } else {
            recordsThatSubTheSelectedPurchase = new ArrayList<>();
            purchaseDTO = null;
            pItemForSelectOneMenu = null;
            quantities.forEach(q -> q.setNum(0));
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
        newCP();
        reInitCalculatedNumbers();
        this.setLabel("Felv. jegy Hozzáadása");
    }

    public CompletedPurchaseDTO getDto() {
        if (this.dto == null) {
            this.dto = new CompletedPurchaseDTO();
        }
        return this.dto;
    }

    @PostConstruct
    public void getAllPurchases() {
        this.dtoList = cService.getAllCompletedPurchases();
    }

    public int getMaxQuantOf(int i) {
        if (purchaseDTO == null || purchaseDTO.getProductList() == null) {
            return 0;
        }

        int original = purchaseDTO.getProductList().get(i).getQuantity2();
        int toSub = 0;

        switch (i) {
            case 0 -> toSub = recordsThatSubTheSelectedPurchase.stream().mapToInt(CompletionRecordDTO::getOne).sum();
            case 1 -> toSub = recordsThatSubTheSelectedPurchase.stream().mapToInt(CompletionRecordDTO::getTwo).sum();
            case 2 -> toSub = recordsThatSubTheSelectedPurchase.stream().mapToInt(CompletionRecordDTO::getThree).sum();
            case 3 -> toSub = recordsThatSubTheSelectedPurchase.stream().mapToInt(CompletionRecordDTO::getFour).sum();
            case 4 -> toSub = recordsThatSubTheSelectedPurchase.stream().mapToInt(CompletionRecordDTO::getFive).sum();
            case 5 -> toSub = recordsThatSubTheSelectedPurchase.stream().mapToInt(CompletionRecordDTO::getSix).sum();
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
        if (this.purchaseDTO == null || this.purchaseDTO.getId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Hiba",
                    "Kérem válasszon egy mérlegjegyet a folytatáshoz!"
                )
            );
            return;
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
        recordDTO.setPurchaseId(purchaseDTO.getId());

        recordDTO.setCompletedPurchase(this.dto);

        recordDTO.setPrice(sixTotal);

        tempRecords = new ArrayList<>(tempRecords.stream().filter(tempRecord -> !Objects.equals(recordDTO.getPurchaseId(), tempRecord.getPurchaseId())).toList());
        tempRecords.add(recordDTO);
        this.purchaseDTO = new PurchaseDTO();
        emptySix();
        updateAvailablePurchases();
        reInitCalculatedNumbers();
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
        purchaseDTO = null;
        reInitCalculatedNumbers();
    }

    public void updateAvailablePurchases() {
        List<Integer> usedIDs = tempRecords != null && !tempRecords.isEmpty() ?
                tempRecords.stream().map(CompletionRecordDTO::getPurchaseId).toList() :
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

                    purchaseDTO1.setRemainingPrice(purchaseDTO1.getTotalPrice() - priceRecords.stream().mapToDouble(CompletionRecordDTO::getPrice).sum() + a.getPrice());
                    availablePurchases.add(purchaseDTO1);
                }
            }
        }
    }

    public void acquireRecords() {
       if (purchaseDTO != null && purchaseDTO.getId() != null) {
            recordsThatSubTheSelectedPurchase = recordService.findAllByPurchaseIdExclusive(purchaseDTO.getId(), dto == null || dto.getId() == null ? -1 : dto.getId());
       }
    }

    private void showErrorMessageToUser(String errorMsg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Hiba",
                        errorMsg
                )
        );
    }
}
