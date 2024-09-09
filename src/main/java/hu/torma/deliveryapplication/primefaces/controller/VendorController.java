package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.VendorDTO;
import hu.torma.deliveryapplication.service.MediatorService;
import hu.torma.deliveryapplication.service.VendorService;
import hu.torma.deliveryapplication.utility.dateutil.DateConverter;
import hu.torma.deliveryapplication.utility.postal.PostalCodeHU;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@SessionScope
@Controller("vendorController")
public class VendorController implements Serializable {

    @Getter @Autowired
    VendorService vService;
    @Autowired
    MediatorService mService;
    Logger log = Logger.getLogger("Vendorlogger");
    @Setter @Getter private List<SortMeta> sortBy;

    @Autowired
    PostalCodeHU postalService;
    @Getter @Setter private String label;
    @Setter private VendorDTO dto;
    @Setter @Getter private List<VendorDTO> dtoList;
    @Getter @Setter private String dateRange;

    @Setter @Getter private String mediator;

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
        this.dtoList = vService.getAllVendorsWithMediators();
        this.setLabel("Hozzáadás");
    }

    public void autoCity() {
        if (this.dto != null && this.dto.getPostalCode() != null) {
            if (this.dto.getPostalCode().chars().anyMatch(Character::isLetter)) {
                return;
            }
            String city = postalService.getCityByPostal(Integer.valueOf(this.dto.getPostalCode()));
            this.dto.setCity(city);
        }
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
        return DateConverter.toDottedDate(dt);
    }

    public void newVendor() {
        this.dto = new VendorDTO();
        this.setLabel("Hozzáadás");
    }

    public VendorDTO getDto() {
        if (this.dto == null) {
            this.dto = new VendorDTO();
        }
        return this.dto;
    }


    public String getPopupMessage() {
        if (dto == null || dto.getTaxId() == null) return "Töltse ki megfelelően!";
        var b = vService.getVendorById(dto.getTaxId());

        return b == null ? "Biztosan hozzáadja?" : "Ez az adóazonosító jel már szerepel az adatbázisban, folytatja?";
    }
}
