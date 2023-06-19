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
@Controller("completedPurchaseController")
public class CompletedPurchaseController implements Serializable {



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
    @Autowired
    private CompletionRecordService recordService;

    public Integer getPriceOf(int i) {
        if (quantities.get(i).getNum() == 0 || this.purchaseDTO.getProductList() == null) return 0;
        var g = this.purchaseDTO.getProductList().get(i);
        Integer sum = (int) (g.getUnitPrice() * quantities.get(i).getNum() * (1 + (0.01 * g.getProduct().getCompPercent())));
        return sum;
    }

    public String getFormattedNumber(double num) {
        if (num - (int) num == 0.0){
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

    public int getRemainingPrice(int id) {
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

    public String getFormattedRemainingPrice(int id) {

        return NumberFormat.getNumberInstance(Locale.US).format(getRemainingPrice(id)).replaceAll(",", " ");

    }

    public void setSixTotal(Double sixTotal) {
        this.sixTotal = sixTotal;
    }

    private Double sixTotal;
    private ArrayList<ProductDTO> listFiveProduct = new ArrayList<>();


    private CompletionRecordDTO recordDTO;

    private ArrayList<CompletionRecordDTO> recordDTOS = new ArrayList<>();

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

    @PostConstruct
    public void init() {
        this.purchaseDTO = new PurchaseDTO();
        this.dto = new CompletedPurchaseDTO();
        pdfdisabled = true;
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder()
                .field("id")
                .order(SortOrder.ASCENDING)
                .build());
        dateRange = (LocalDate.now().getYear() - 50) + ":" + (LocalDate.now().getYear() + 5);
    }


    @PostConstruct
    public void getAllPurchases() {
        // this.dtoList = purchaseDTO.getCompletedPurchaseDTOS();
        this.setLabel("Felv.jegy Mentése");
        this.setLabel2("Termék hozzáadása");
    }

    public void setDto(CompletedPurchaseDTO dto) {
        this.dto = dto;
    }


    public void setSortBy(List<SortMeta> sortBy) {
        this.sortBy = sortBy;
    }


    public void pdf() {
        //file = pdFcreator.createDownload(this.dto);
        /*
        file = DefaultStreamedContent.builder()
                .name("modified.xlsx")
                .contentType("application/vnd.ms-excel")
                .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/demo/excel/modified.xlsx"))
                .build();
        */
    }

    public StreamedContent getFile() {
        return file;
    }


    public void uiSaveCompletedPurchase() {
        //if (true)return;
        if (this.dto == null) return;
        getSixTotal();
        logger.warning("uiSaveCalled");
        Date date = new Date(System.currentTimeMillis());
        this.dto.setBookedDate(date);
        setQuants();
        calculateTotalPrice();
        logger.info("DTO's ONE IS: " + dto.getOne());
        this.dto.setTotalPrice(dto.getRecords().stream().mapToInt(CompletionRecordDTO::getPrice).sum());
        var b = cService.saveCompletedPurchase(dto);
        getAllPurchases();


        this.setDto(new CompletedPurchaseDTO());


        this.setPurchaseDTO(new PurchaseDTO());
        this.setProductDTO(new PurchasedProductDTO());
        this.pdfdisabled = true;
        emptySix();
    }


    private void calculateTotalPrice() {

    }

    public void deletePurchase() {


        cService.deleteCompletedPurchase(dto);
        this.getAllPurchases();
        this.dto = new CompletedPurchaseDTO();
        this.purchaseDTO = new PurchaseDTO();
        this.pdfdisabled = true;
    }

    public void editPurchase(SelectEvent<CompletedPurchaseDTO> _dto) {
        //emptySix();

        this.setLabel("Felv.jegy Módosítása");
        this.pdfdisabled = false;
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());
        acquireQuants();
    }

    private void acquireQuants() {
        quantities.get(0).setNum(dto.getOne());
        quantities.get(1).setNum(dto.getTwo());
        quantities.get(2).setNum(dto.getThree());
        quantities.get(3).setNum(dto.getFour());
        quantities.get(4).setNum(dto.getFive());
        quantities.get(5).setNum(dto.getSix());

    }

    private void emptySix() {
        for (var i : quantities) i.setNum(0);
    }

    public void newPurchase() {
        emptySix();
        this.dto = new CompletedPurchaseDTO();
        this.purchaseDTO = new PurchaseDTO();
        this.setLabel("Felv. jegy Hozzáadása");
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public CompletedPurchaseDTO getDto() {
        if (this.dto == null) {
            this.dto = new CompletedPurchaseDTO();
        }
        return this.dto;
    }

    public String getLabel() {
        return label;
    }

    public List<SortMeta> getSortBy() {
        return sortBy;
    }

    public List<CompletedPurchaseDTO> getDtoList() {
        return cService.getAllCompletedPurchases();
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

        var validRecords = recordService.getAllCompletionRecords().stream().filter(c -> c.getPurchaseId() == (long) purchaseDTO.getId()).toList();

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
        /*
        var records = new ArrayList<>(recordService.getAllCompletionRecords().stream().filter(c -> c.getPurchaseId() == (long) purchaseDTO.getId()).toList());
        if (dto.getRecords() != null) for (var a : dto.getRecords()) {
            if (a.getPurchaseId() == purchaseDTO.getId().longValue() && !records.contains(a)) records.add(a);
        }
        */

        logger.warning("dto one is: " + dto.getRecords().stream().map(CompletionRecordDTO::getOne).mapToInt(Integer::intValue).sum() + "before save");
        dto.setOne(dto.getRecords().stream().map(CompletionRecordDTO::getOne).mapToInt(Integer::intValue).sum());
        dto.setTwo(dto.getRecords().stream().map(CompletionRecordDTO::getTwo).mapToInt(Integer::intValue).sum());
        dto.setThree(dto.getRecords().stream().map(CompletionRecordDTO::getThree).mapToInt(Integer::intValue).sum());
        dto.setFour(dto.getRecords().stream().map(CompletionRecordDTO::getFour).mapToInt(Integer::intValue).sum());
        dto.setFive(dto.getRecords().stream().map(CompletionRecordDTO::getFive).mapToInt(Integer::intValue).sum());
        dto.setSix(dto.getRecords().stream().map(CompletionRecordDTO::getSix).mapToInt(Integer::intValue).sum());

    }

    public ArrayList<CompletionRecordDTO> getRecordDTOS() {

        return dto.getRecords() == null ? new ArrayList<>() : new ArrayList<>(dto.getRecords());
    }

    public void setRecordDTOS(ArrayList<CompletionRecordDTO> recordDTOS) {
        this.recordDTOS = recordDTOS;
    }

    public CompletionRecordDTO getRecordDTO() {
        return recordDTO;
    }

    public void setRecordDTO(CompletionRecordDTO recordDTO) {
        this.recordDTO = recordDTO;
    }

    public void editRecord(SelectEvent<CompletionRecordDTO> _dto) {
        BeanUtils.copyProperties(_dto.getObject(), this.recordDTO);
    }

    public void addRecord() {
        if (this.purchaseDTO.getId() == null) {
            //logger.info("purchasedto id was null");
            return;
        }
        if (this.dto.getRecords() == null) this.dto.setRecords(new ArrayList<>());
        if (this.recordDTO == null) recordDTO = new CompletionRecordDTO();
        for (int i = 0; i < 6; i++) {
            if (quantities.get(i).getNum() > getMaxQuantOf(i)) {
                logger.info(i + "th/rd element " + quantities.get(i).getNum() + " was greater than" + getMaxQuantOf(i));
                return;
            }
        }
        recordDTO.setOne(quantities.get(0).getNum());
        recordDTO.setTwo(quantities.get(1).getNum());
        recordDTO.setThree(quantities.get(2).getNum());
        recordDTO.setFour(quantities.get(3).getNum());
        recordDTO.setFive(quantities.get(4).getNum());
        recordDTO.setSix(quantities.get(5).getNum());
        recordDTO.setPurchaseId((long) purchaseDTO.getId());

        recordDTO.setPrice(getSixTotal());

        dto.getRecords().add(recordDTO);
        logger.info("Added record" + recordDTO.toString());
        this.recordDTO = new CompletionRecordDTO();
        this.purchaseDTO = new PurchaseDTO();
        emptySix();

    }
    public String toDottedDate(java.util.Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+01"));

        return dt==null?"0000.01.01":sdf.format(dt);
    }
    public void removeRecord() {
        if (this.dto.getRecords().size() < 1) return;
        if (this.recordDTO != null) dto.getRecords().remove(dto.getRecords().size() - 1);
        this.recordDTO = new CompletionRecordDTO();
        //T3
    }

    public void onItemSelectedListener(SelectEvent event) {
        purchaseDTO = (PurchaseDTO) event.getObject();
    }

    public ArrayList<PurchaseDTO> getAvailablePurchases() {
        var usedIDs = new ArrayList<Integer>();
        if (dto.getRecords() != null)
            usedIDs.addAll(dto.getRecords().stream().map(v -> v.getPurchaseId().intValue()).toList());
        var temp = OPservice.getAllPurchases().stream().filter(c -> !usedIDs.contains(c.getId())).filter(v -> getRemainingPrice(v.getId()) != 0).toList();
        return new ArrayList<>(temp);
    }
}
