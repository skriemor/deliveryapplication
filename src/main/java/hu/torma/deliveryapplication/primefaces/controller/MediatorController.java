package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.MediatorDTO;
import hu.torma.deliveryapplication.service.MediatorService;
import hu.torma.deliveryapplication.service.MediatorService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@SessionScope
@Controller("mediatorController")
public class MediatorController implements Serializable {

    Logger log = Logger.getLogger("Mediatorlogger");
    private List<SortMeta> sortBy;
    @Autowired
    MediatorService vService;
    @Autowired
    MediatorService mService;

    @Autowired
    PostalCodeHU postalService;
    private String label;
    private MediatorDTO dto;
    private List<MediatorDTO> dtoList;
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

    }

    @PostConstruct
    public void getAllMediators() {
        this.dtoList = vService.getAllMediators();
        this.setLabel("Hozzáadás");
    }


    public void setDto(MediatorDTO dto) {
        this.dto = dto;
    }

    public void setDtoList(List<MediatorDTO> dtoList) {
        this.dtoList = dtoList;
    }

    public void setSortBy(List<SortMeta> sortBy) {
        this.sortBy = sortBy;
    }

    public void uiSaveMediator() {

        vService.saveMediator(this.dto);
        getAllMediators();
        this.setDto(new MediatorDTO());
    }

    public void deleteMediator() {
        if (this.dto != null) {
            vService.deleteMediator(this.dto);
        }
        this.getAllMediators();
        this.dto = new MediatorDTO();
    }

    public void editMediator(SelectEvent<MediatorDTO> _dto) {
        this.setLabel("Módosítás");
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());
    }

    public void newMediator() {
        this.dto = new MediatorDTO();
        this.setLabel("Hozzáadás");
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public MediatorDTO getDto() {
        if (this.dto == null) {
            this.dto = new MediatorDTO();
        }
        return this.dto;
    }

    public String getLabel() {
        return label;
    }

    public List<SortMeta> getSortBy() {
        return sortBy;
    }

    public List<MediatorDTO> getDtoList() {
        return dtoList;
    }

    public MediatorService getvService() {
        return vService;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getDateRange() {
        return dateRange;
    }
}
