package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.MediatorDTO;
import hu.torma.deliveryapplication.DTO.VendorDTO;
import hu.torma.deliveryapplication.service.MediatorService;
import hu.torma.deliveryapplication.service.VendorService;
import hu.torma.deliveryapplication.utility.postal.PostalCodeHU;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

@SessionScope
@Controller("vendorController")
public class VendorController implements Serializable {

    Logger log = Logger.getLogger("Vendorlogger");
    private List<SortMeta> sortBy;
    @Autowired
    VendorService vService;
    @Autowired
    MediatorService mService;

    @Autowired
    PostalCodeHU postalService;
    private String label;
    private VendorDTO dto;
    private List<VendorDTO> dtoList;
    private String dateRange;

    public String getMediator() {
        return mediator;
    }

    public void setMediator(String mediator) {
        this.mediator = mediator;
    }

    private String mediator;

    @PostConstruct
    public void init() {
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder()
                .field("taxId")
                .order(SortOrder.ASCENDING)
                .build());
        dateRange = (LocalDate.now().getYear() - 120) + ":" + (LocalDate.now().getYear() - 14);
    }

    @PostConstruct
    public void getAllVendors() {
        this.dtoList = vService.getAllVendors();
        this.setLabel("Hozzáadás");
    }

    public void autoCity() {
        log.info("Autocitied");
        if (this.dto != null && this.dto.getPostalCode()!=null) {
            String city = postalService.getCityByPostal(Integer.valueOf(this.dto.getPostalCode()));
            log.info("City name: "+city);
            this.dto.setCity(city);

        }

    }

    public void setDto(VendorDTO dto) {
        this.dto = dto;
    }

    public void setDtoList(List<VendorDTO> dtoList) {
        this.dtoList = dtoList;
    }

    public void setSortBy(List<SortMeta> sortBy) {
        this.sortBy = sortBy;
    }

    public void uiSaveVendor() {

        vService.saveVendor(this.dto);
        getAllVendors();
        this.setDto(new VendorDTO());
    }

    public void deleteVendor() {
        if (this.dto != null) {
            vService.deleteVendor(this.dto);
        }
        this.getAllVendors();
        this.dto = new VendorDTO();
    }

    public void editVendor(SelectEvent<VendorDTO> _dto) {
        this.setLabel("Módosítás");
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());
    }

    public String toDottedDate(java.util.Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+01"));

        return dt==null?"0000.01.01":sdf.format(dt);
    }
    public void newVendor() {
        this.dto = new VendorDTO();
        this.setLabel("Hozzáadás");
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public VendorDTO getDto() {
        if (this.dto == null) {
            this.dto = new VendorDTO();
        }
        return this.dto;
    }

    public String getLabel() {
        return label;
    }

    public List<SortMeta> getSortBy() {
        return sortBy;
    }

    public List<VendorDTO> getDtoList() {
        return dtoList;
    }

    public VendorService getvService() {
        return vService;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getDateRange() {
        return dateRange;
    }

    public String getFileNumber() {
        return dto.getFileNumber();
    }
    public void setFileNumber(String filen) {
        dto.setFileNumber(filen);
    }

}
