package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.*;
import hu.torma.deliveryapplication.service.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SessionScope
@Controller("displayController")
@Setter
@Getter
public class DisplayController implements Serializable {

    private boolean shouldFilterByPaper, paidOnly;

    private String mediator;
    private String filterName, filterName2, filterName4;


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

    }


    private Date subOneDayFromDate(Date date) {
        LocalDate ld = date.toInstant().atZone(ZoneId.of("GMT+01")).toLocalDate();
        ld = ld.minusDays(1);
        return Date.from(ld.atStartOfDay().atZone(ZoneId.of("GMT+01")).toInstant());
    }

    private void doFilterDate() {
        if (filterDateFrom != null && filterDateTo != null) {
            purchaseDTOS = purchaseDTOS.stream().filter(p -> (p.getBookedDate().after(filterDateFrom) || p.getBookedDate().after(subOneDayFromDate(filterDateFrom))) && p.getBookedDate().before(filterDateTo)).toList();
        }
        if (filterDateFrom != null && filterDateTo == null) {
            purchaseDTOS = purchaseDTOS.stream().filter(p -> p.getBookedDate().after(filterDateFrom) || p.getBookedDate().after(subOneDayFromDate(filterDateFrom))).toList();
        }
        if (filterDateFrom == null && filterDateTo != null) {
            purchaseDTOS = purchaseDTOS.stream().filter(p -> p.getBookedDate().before(filterDateTo)).toList();
        }
    }

    private void doFilterDate4() {
        if (filterDateFrom4 != null && filterDateTo4 != null) {
            CPDTOS = CPDTOS.stream().filter(p -> (p.getBookedDate().after(filterDateFrom4) || p.getBookedDate().after(subOneDayFromDate(filterDateFrom4))) && p.getBookedDate().before(filterDateTo4)).toList();
        }
        if (filterDateFrom4 != null && filterDateTo4 == null) {
            CPDTOS = CPDTOS.stream().filter(p -> p.getBookedDate().after(filterDateFrom4) || p.getBookedDate().after(subOneDayFromDate(filterDateFrom4))).toList();
        }
        if (filterDateFrom4 == null && filterDateTo4 != null) {
            CPDTOS = CPDTOS.stream().filter(p -> p.getBookedDate().before(filterDateTo4)).toList();
        }
    }

    private void doFilterDate2() {
        if (filterDateFrom2 != null && filterDateTo2 != null) {

            saleDTOS = saleDTOS.stream().filter(p -> (p.getBookingDate().after(filterDateFrom2) || p.getBookingDate().after(subOneDayFromDate(filterDateFrom2))) && p.getBookingDate().before(filterDateTo2)).toList();

        }
        if (filterDateFrom2 != null && filterDateTo2 == null) {
            saleDTOS = saleDTOS.stream().filter(p -> p.getBookingDate().after(filterDateFrom2) || p.getBookingDate().after(subOneDayFromDate(filterDateFrom2))).toList();
        }
        if (filterDateFrom2 == null && filterDateTo2 != null) {
            saleDTOS = saleDTOS.stream().filter(p -> p.getBookingDate().before(filterDateTo2)).toList();
        }
    }

    private void doFilterName() {
        if (filterName != null) {
            purchaseDTOS = purchaseDTOS.stream().filter(n -> n.getVendor().getVendorName().toLowerCase().contains(filterName.toLowerCase())).toList();
        }

    }

    private void doFilterName2() {
        if (filterName2 != null) {
            saleDTOS = saleDTOS.stream().filter(n -> n.getBuyer().getName().toLowerCase().contains(filterName2.toLowerCase())).toList();
        }

    }

    private void doFilterName4() {
        if (filterName4 != null) {
            CPDTOS = CPDTOS.stream().filter(n -> n.getVendor().getVendorName().toLowerCase().contains(filterName4.toLowerCase())).toList();
        }

    }

    private void doFilterPaidOnly() {
        if (paidOnly) {
            CPDTOS = CPDTOS.stream().filter(cp->cp.getPaymentDate()!=null).toList();
        }
    }

    private void doFilterMediator() {
        if (mediator != null) {
            mediatorDTOS = mediatorDTOS.stream().filter(c -> c.getId().toLowerCase().contains(mediator.toLowerCase())).toList();
        }

    }

    public List<PurchaseDTO> getPurchaseDTOS() {
        purchaseDTOS = new ArrayList<>(purchaseService.getAllPurchases());
        doFilterName();
        doFilterDate();
        return new ArrayList<>(purchaseDTOS);
    }

    public ArrayList<SaleDTO> getSaleDTOS() {
        saleDTOS = saleService.getAllSales();
        doFilterName2();
        doFilterDate2();
        return new ArrayList<>(saleDTOS);

    }

    public ArrayList<CompletedPurchaseDTO> getCPDTOS() {
        CPDTOS = CPService.getAllCompletedPurchases();
        doFilterName4();
        doFilterDate4();
        doFilterPaidOnly();
        return new ArrayList<>(CPDTOS);
    }
    public ArrayList<MediatorDisplay> getMediatorDisplays() {
        var tmp = new ArrayList<MediatorDisplay>();
        mediatorDTOS = mediatorService.getAllMediators();
        doFilterMediator();
        generateDisplays();
        return new ArrayList<>(mediatorDisplays);
    }

    private ArrayList<PurchaseDTO> getPurchasedProductListDateFiltered(List<PurchaseDTO> lst) {
        if (filterDateFrom3 != null && filterDateTo3 != null) {

            lst = lst.stream().filter(p -> (p.getReceiptDate().after(filterDateFrom3) || p.getReceiptDate().after(subOneDayFromDate(filterDateFrom3)) ) && p.getReceiptDate().before(filterDateTo3)).toList();

        }
        if (filterDateFrom3 != null && filterDateTo3 == null) {
            lst = lst.stream().filter(p -> p.getReceiptDate().after(filterDateFrom3) ||p.getReceiptDate().after(subOneDayFromDate(filterDateFrom3))).toList();
        }
        if (filterDateFrom3 == null && filterDateTo3 != null) {
            lst = lst.stream().filter(p -> p.getReceiptDate().before(filterDateTo3)).toList();
        }


        return new ArrayList<>(lst);
    }

    public void generateDisplays() {
        mediatorDisplays = new ArrayList<>();
        mediatorDTOS.forEach(m -> {
            var disp = new MediatorDisplay();
            var tmpsv = purchaseService.getAllPurchases();
            tmpsv = getPurchasedProductListDateFiltered(tmpsv);
            var tmps = tmpsv.stream().filter(p -> p.getVendor().getMediator().getId().equals(m.getId())).flatMap(g -> g.getProductList().stream()).toList();

            disp.setOne(tmps.stream().filter(c -> c.getProduct().getId().equals("I.OSZTÁLYÚ")).mapToInt(PurchasedProductDTO::getQuantity2).sum());
            disp.setTwo(tmps.stream().filter(c -> c.getProduct().getId().equals("II.OSZTÁLYÚ")).mapToInt(PurchasedProductDTO::getQuantity2).sum());
            disp.setThree(tmps.stream().filter(c -> c.getProduct().getId().equals("III.OSZTÁLYÚ")).mapToInt(PurchasedProductDTO::getQuantity2).sum());
            disp.setFour(tmps.stream().filter(c -> c.getProduct().getId().equals("IV.OSZTÁLYÚ")).mapToInt(PurchasedProductDTO::getQuantity2).sum());
            disp.setFive(tmps.stream().filter(c -> c.getProduct().getId().equals("IPARI")).mapToInt(PurchasedProductDTO::getQuantity2).sum());
            disp.setSix(tmps.stream().filter(c -> c.getProduct().getId().equals("GYÖKÉR")).mapToInt(PurchasedProductDTO::getQuantity2).sum());
            disp.setMediatorName(m.getId());
            mediatorDisplays.add(disp);
        });
    }


}
