package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.service.*;
import hu.torma.deliveryapplication.utility.Quant;
import hu.torma.deliveryapplication.utility.pdf.PDFcreator;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.IntStream;


@Controller
public class CompletedPurchaseController implements Serializable {

    private Integer selectionCounter;

    public Integer getSelectionCounter() {
        if (selectionCounter == null) {
            selectionCounter = 0;
        }
        return selectionCounter;
    }

    public void setSelectionCounter(Integer selectionCounter) {
        this.selectionCounter = selectionCounter;
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

    public PurchaseDTO getpItemForSelectOneMenu() {
        return pItemForSelectOneMenu;
    }

    public void setpItemForSelectOneMenu(PurchaseDTO pItemForSelectOneMenu) {
        this.pItemForSelectOneMenu = pItemForSelectOneMenu;
    }

    PurchaseDTO pItemForSelectOneMenu;


    List<CompletionRecordDTO> tempRecords;

    List<CompletionRecordDTO> validRecords;

    Boolean shouldLook;

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


    public Integer getNetSum() {
        return quantities.stream().mapToInt(Quant::getNum).sum();
    }

    public Integer getDtoWeight() {
        return IntStream.range(0, 6).map(a -> getTotalAmountOf(a)).sum();
    }

    public Integer getDtoTotalV() {
        return tempRecords.stream().mapToInt(CompletionRecordDTO::getPrice).sum();
    }

    public Double getNetTotalV() {
        return (double) Math.round(getGrossTotalV() / 1.12);
    }

    public Double getGrossTotalV() {
        return (double) Math.round(getGrossAvgPriceV() * getDtoWeight());
    }

    public Double getGrossAvgPriceV() {
        return (double) Math.round(getDtoTotalV() / (double) getDtoWeight() * 100) / 100;
    }

    public Double getNetAvgPriceV() {
        return (double) Math.round(getGrossAvgPriceV() / 1.12 * 100) / 100;
    }

    public Double getDiffV() {
        return getGrossTotalV() - getNetTotalV();
    }


    @Autowired
    private CompletionRecordService recordService;

    public Integer getPriceOf(int i) {
        if (quantities.get(i).getNum() == 0 || this.purchaseDTO.getProductList() == null) return 0;
        var g = this.purchaseDTO.getProductList().get(i);
        Integer sum = (int) (g.getUnitPrice() * quantities.get(i).getNum() * (1 + (0.01 * g.getProduct().getCompPercent())));
        return sum;
    }

    public String getFormattedNumber(double num) {
        if (num - (int) num == 0.0) {
            return NumberFormat.getNumberInstance(Locale.US).format((int) num).replaceAll(",", " ");
        }
        return NumberFormat.getNumberInstance(Locale.US).format(num).replaceAll(",", " ");
    }

    public String getFormattedPriceOf(int g) {
        return NumberFormat.getNumberInstance(Locale.US).format(getPriceOf(g)).replaceAll(",", " ");

    }

    public String getFormattedSixTotal() {
        return NumberFormat.getNumberInstance(Locale.US).format(getSixTotal()).replaceAll(",", " ");

    }


    public String getDtoTotal() {
        var nuum = tempRecords.stream().mapToInt(CompletionRecordDTO::getPrice).sum();
        return NumberFormat.getNumberInstance(Locale.US).format(nuum).replaceAll(",", " ");
    }

    public Integer getSixTotal() {
        int temp = 0;
        int sum;
        try {
            var list = this.purchaseDTO.getProductList();
            if (list != null) {
                for (int i = 0; i < 6; i++) {
                    sum = (int) (list.get(i).getUnitPrice() * quantities.get(i).getNum() * (1 + (0.01 * list.get(i).getProduct().getCompPercent())));
                    temp += sum;
                }
            }

        } catch (Exception e) {
        }
        //this.dto.setTotalPrice(temp.doubleValue());
        return temp;
    }

    @Autowired
    PurchaseService purchaseService;


    public List<CompletionRecordDTO> getTempRecords() {
        return tempRecords;
    }

    public void updateRemainingPrice(int id) {
        logger.info("updating remaining price of " + id);
        var temp = purchaseService.getPurchaseById(id);
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
        purchaseService.savePurchase(temp);
    }


    public void setSixTotal(Double sixTotal) {
        this.sixTotal = sixTotal;
    }

    private Double sixTotal;
    private ArrayList<ProductDTO> listFiveProduct = new ArrayList<>();


    private ArrayList<Quant> quantities = new ArrayList<>(Arrays.asList(new Quant(0), new Quant(0), new Quant(0), new Quant(0), new Quant(0), new Quant(0)));

    private StreamedContent file;

    Logger logger = Logger.getLogger("BOOL");
    private List<SortMeta> sortBy;

    @Autowired
    private PDFcreator pdFcreator;


    private PurchaseDTO purchaseDTO;
    private Boolean pdfdisabled;

    @Autowired
    StorageService sService;
    @Autowired
    CompletedPurchaseService cService;
    @Autowired
    ProductService pService;

    @Autowired
    UnitService uService;

    @Autowired
    PurchaseService OPservice;
    private String label;

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    private String label2;
    private CompletedPurchaseDTO dto;
    private String dateRange;

    private PurchasedProductDTO productDTO;

    public PurchasedProductDTO getProductDTO() {
        if (this.productDTO == null) this.productDTO = new PurchasedProductDTO();
        return this.productDTO;
    }

    public void setProductDTO(PurchasedProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    private void newCP() {
        var stacktrace = Thread.currentThread().getStackTrace()[2].getMethodName();


        logger.info("newCP called by " + stacktrace);


        logger.info("new CP created");
        this.dto = new CompletedPurchaseDTO();
        var recordse = new ArrayList<CompletionRecordDTO>();
        dto.setRecords(recordse);
        updateAvailablePurchases();

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
        tempRecords = new ArrayList<>();
        this.purchaseDTO = new PurchaseDTO();
        newCP();

        pdfdisabled = true;
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder()
                .field("id")
                .order(SortOrder.ASCENDING)
                .build());
        dateRange = (LocalDate.now().getYear() - 50) + ":" + (LocalDate.now().getYear() + 5);
        updateAvailablePurchases();

    }


    public void setDto(CompletedPurchaseDTO dto) {
        this.dto = dto;
    }


    public void setSortBy(List<SortMeta> sortBy) {
        this.sortBy = sortBy;
    }


    public StreamedContent getFile() {
        return file;
    }

    /**
     * Also updates serials
     */
    private void updateRemainingPrices() {

        if (beforeEditList != null) for (var c : beforeEditList) {
            updateRemainingPrice(c.getPurchaseId());


        }

        if (dto.getRecords() != null) for (var c : dto.getRecords()) {
            updateRemainingPrice(c.getPurchaseId());

        }
    }

    public String getSumOfRecordPrices() {
        return NumberFormat.getNumberInstance(Locale.US).format(tempRecords.stream().mapToInt(CompletionRecordDTO::getPrice).sum()).replaceAll(",", " ");

    }

    @Autowired
    SiteService siteService;

    public void uiSaveCompletedPurchase() {
        //if (true)return;
        if (this.dto == null) return;
        if (this.dto.getSite() == null) {
            var sites = siteService.getAllSites();
            if (sites != null && sites.size() > 0)
                this.dto.setSite(siteService.getAllSites().get(0));
        }
        dto.setRecords(tempRecords);
        getSixTotal();
        logger.warning("uiSaveCalled");
        Date date = new Date(System.currentTimeMillis());
        this.dto.setBookedDate(date);
        //setQuants();
        logger.info("DTO's ONE IS: " + dto.getOne());

        if (this.dto.getRecords() == null || this.dto.getRecords().size() < 1) {
            this.dto.setTotalPrice(0);
        }
        if (this.dto.getRecords() != null && this.dto.getRecords().size() > 0) {
            this.dto.setTotalPrice(getGrossTotalV().intValue());

        }

        var b = cService.saveCompletedPurchase(dto);

        getAllPurchases();
        updateRemainingPrices();

        newCP();
        tempRecords.clear();

        this.setPurchaseDTO(new PurchaseDTO());
        this.setProductDTO(new PurchasedProductDTO());
        this.pdfdisabled = true;
        emptySix();
        updateAvailablePurchases();
    }


    private void calculateTotalPrice() {

    }

    public void deletePurchase() {


        cService.deleteCompletedPurchase(dto);

        updateRemainingPrices();
        this.getAllPurchases();
        newCP();
        this.purchaseDTO = new PurchaseDTO();
        this.pdfdisabled = true;
        tempRecords.clear();
        emptySix();
        updateAvailablePurchases();
        selectionCounter=0;
    }

    List<CompletionRecordDTO> beforeEditList;

    public void editPurchase(SelectEvent<CompletedPurchaseDTO> _dto) {
        //emptySix();
        tempRecords = _dto.getObject().getRecords();
        beforeEditList = _dto.getObject().getRecords();
        if (beforeEditList != null && beforeEditList.size() > 0) {
            var fdo = purchaseService.getPurchaseById(beforeEditList.get(0).getPurchaseId());
            this.setPurchaseDTO(purchaseService.getPurchaseById(beforeEditList.get(0).getPurchaseId()));
            acquireQuants();
            this.pItemForSelectOneMenu = fdo;
        }

        this.setLabel("Felv.jegy Módosítása");
        this.pdfdisabled = false;
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());

        updateAvailablePurchases();
        this.purchaseDTO = new PurchaseDTO();
        logger.info(_dto.getObject().getRecords().getClass().getName());

        selectionCounter=0;
    }

    private void acquireQuants() {
        var acq = beforeEditList.get(0);
        quantities.get(0).setNum(acq.getOne());
        quantities.get(1).setNum(acq.getTwo());
        quantities.get(2).setNum(acq.getThree());
        quantities.get(3).setNum(acq.getFour());
        quantities.get(4).setNum(acq.getFive());
        quantities.get(5).setNum(acq.getSix());

    }

    private void emptySix() {
        for (var i : quantities) i.setNum(0);
    }

    public void newPurchase() {

        tempRecords.clear();
        emptySix();
        newCP();
        this.purchaseDTO = new PurchaseDTO();
        this.setLabel("Felv. jegy Hozzáadása");
        updateAvailablePurchases();
        selectionCounter=0;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public CompletedPurchaseDTO getDto() {

        if (this.dto == null) {
            newCP();
        }
        return this.dto;
    }

    public String getLabel() {
        return label;
    }

    public List<SortMeta> getSortBy() {
        return sortBy;
    }

    List<CompletedPurchaseDTO> dtoList;

    @PostConstruct
    public void getAllPurchases() {
        this.dtoList = cService.getAllCompletedPurchases();
        copySerials(this.dtoList);
        this.setLabel("Hozzáadás");
        this.setLabel2("Termék hozzáadása");
    }

    public List<CompletedPurchaseDTO> getDtoList() {
        return dtoList;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }


    public Boolean getPdfdisabled() {
        return pdfdisabled;
    }

    public void setPdfdisabled(Boolean pdfdisabled) {
        this.pdfdisabled = pdfdisabled;
    }


    public ArrayList<ProductDTO> getListFiveProduct() {
        return listFiveProduct;
    }

    public void setListFiveProduct(ArrayList<ProductDTO> listFiveProduct) {
        this.listFiveProduct = listFiveProduct;
    }


    public PurchaseDTO getPurchaseDTO() {
        return purchaseDTO;
    }

    public void setPurchaseDTO(PurchaseDTO purchaseDTO) {
        this.purchaseDTO = purchaseDTO;
        if (purchaseDTO.getId() != null) validRecords = recordService.findAllByPurchaseId(purchaseDTO.getId());
        logger.info(validRecords != null ? String.valueOf(validRecords.size()) : " ");

    }

    public ArrayList<Quant> getQuantities() {
        return quantities;
    }

    public void setQuantities(ArrayList<Quant> quantities) {
        this.quantities = quantities;
    }


    public int getMaxQuantOf(int i) {

        if (purchaseDTO.getProductList() == null) {
            //logger.info("PurchaseDTO's productlist was null");
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
        //int toSub = quantities.get(i).getNum();
        return original - toSub;
    }

    public void setQuants() {
        if (dto.getRecords() == null) return;
        quantities.get(0).setNum(tempRecords.stream().mapToInt(CompletionRecordDTO::getOne).sum());
        quantities.get(1).setNum(tempRecords.stream().mapToInt(CompletionRecordDTO::getTwo).sum());
        quantities.get(2).setNum(tempRecords.stream().mapToInt(CompletionRecordDTO::getThree).sum());
        quantities.get(3).setNum(tempRecords.stream().mapToInt(CompletionRecordDTO::getFour).sum());
        quantities.get(4).setNum(tempRecords.stream().mapToInt(CompletionRecordDTO::getFive).sum());
        quantities.get(5).setNum(tempRecords.stream().mapToInt(CompletionRecordDTO::getSix).sum());
    }


    public Integer getTotalAmountOf(int i) {
        switch (i) {
            case 0:
                return tempRecords.stream().mapToInt(CompletionRecordDTO::getOne).sum();
            case 1:
                return tempRecords.stream().mapToInt(CompletionRecordDTO::getTwo).sum();
            case 2:
                return tempRecords.stream().mapToInt(CompletionRecordDTO::getThree).sum();
            case 3:
                return tempRecords.stream().mapToInt(CompletionRecordDTO::getFour).sum();
            case 4:
                return tempRecords.stream().mapToInt(CompletionRecordDTO::getFive).sum();
            case 5:
                return tempRecords.stream().mapToInt(CompletionRecordDTO::getSix).sum();

        }
        return 0;
    }

    List tempNamesList = new ArrayList(Arrays.asList("I.OSZTÁLYÚ", "II.OSZTÁLYÚ", "III.OSZTÁLYÚ", "IV.OSZTÁLYÚ", "GYÖKÉR", "IPARI"));

    public void addRecord() {
        if (this.purchaseDTO.getId() == null) {
            return;
        }
        if (this.dto.getRecords() == null) {
            logger.info("records were null");
            this.dto.setRecords(new ArrayList<CompletionRecordDTO>());
        }
        var recordDTO = new CompletionRecordDTO();
        StringBuffer sB = new StringBuffer();

        boolean wasWrong = false;
        for (int i = 0; i < 6; i++) {
            if (quantities.get(i).getNum() > getMaxQuantOf(i)) {
                wasWrong = true;
                sB.append(tempNamesList.get(i) + ", ");
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
        recordDTO.setPurchaseId(purchaseDTO.getId());

        recordDTO.setPrice(getSixTotal());
        tempRecords.add(recordDTO);
        logger.info("Added record" + recordDTO);
        this.purchaseDTO = new PurchaseDTO();
        emptySix();
        updateAvailablePurchases();
    }

    public String toDottedDate(java.util.Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+01"));

        return dt == null ? "0000.01.01" : sdf.format(dt);
    }

    public void removeRecord() {
        if (tempRecords.size() < 1) return;
        tempRecords.remove(tempRecords.size() - 1);
        this.purchaseDTO = new PurchaseDTO();
        for (var q : quantities) q.setNum(0);
        updateAvailablePurchases();

    }
    public void removeRecord2() {
        if (tempRecords != null && selectionCounter >= tempRecords.size()) return;
        pItemForSelectOneMenu = purchaseService.getPurchaseById(tempRecords.get(selectionCounter.intValue()).getPurchaseId());
        tempRecords.remove(selectionCounter.intValue());
        for (var q : quantities) q.setNum(0);
        updateAvailablePurchases();
        selectionCounter=0;
    }

    public void onItemSelectedListener(SelectEvent event) {
        purchaseDTO = (PurchaseDTO) event.getObject();
    }

    public ArrayList<PurchaseDTO> getAvailablePurchases() {
        return availablePurchases;
    }

    public void setAvailablePurchases(ArrayList<PurchaseDTO> availablePurchases) {
        this.availablePurchases = availablePurchases;
    }

    public String getPrice(PurchaseDTO __dto) {

        return NumberFormat.getNumberInstance(Locale.US).format(__dto.getRemainingPrice()).replaceAll(",", " ");
    }

    public ArrayList<PurchaseDTO> availablePurchases;


    public ArrayList<PurchaseDTO> updateAvailablePurchases() {
        logger.info("Getting available purchases");
        ArrayList<Integer> usedIDs = new ArrayList<>();
        if (tempRecords != null)
            usedIDs.addAll(tempRecords.stream().map(CompletionRecordDTO::getPurchaseId).toList());
        availablePurchases = new ArrayList<>(OPservice.getAllPurchases().stream().filter(c -> !usedIDs.contains(c.getId()))
                .filter(v -> v.getRemainingPrice() != 0).toList());
        return availablePurchases;
    }

    public void logRecordList() {
        if (dto == null || dto.getRecords() == null) {
            logger.warning("RECORDLIST OR RECVORD NULL ");
            return;
        }
        logger.warning("RECORDLIST SIZE: " + dto.getRecords().size());
    }
}
