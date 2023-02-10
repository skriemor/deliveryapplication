package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.UnitDTO;
import hu.torma.deliveryapplication.DTO.VendorDTO;
import hu.torma.deliveryapplication.service.UnitService;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScope
@Controller("unitController")
public class UnitController implements Serializable {

    private List<SortMeta> sortBy;
    @Autowired
    UnitService service;

    private String label;
    private UnitDTO dto;
    private List<UnitDTO> dtoList;

    @PostConstruct
    public void init() {
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder()
                .field("id")
                .order(SortOrder.ASCENDING)
                .build());
    }

    @PostConstruct
    public void getAllUnits() {
        this.dtoList = service.getAllUnits();
        this.setLabel("Hozzáadás");
    }

    public void setDto(UnitDTO dto) {
        this.dto = dto;
    }

    public void setDtoList(List<UnitDTO> dtoList) {
        this.dtoList = dtoList;
    }

    public void setSortBy(List<SortMeta> sortBy) {
        this.sortBy = sortBy;
    }

    public void uiSaveUnit() {
        service.saveUnit(this.dto);
        getAllUnits();
        this.setDto(new UnitDTO());
    }

    public void deleteUnit() {
        if (this.dto != null) {
            service.deleteUnit(this.dto);
        }
        this.getAllUnits();
        this.dto = new UnitDTO();
    }

    public void editUnit(SelectEvent<UnitDTO> _dto) {
        this.setLabel("Módosítás");
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());
    }

    public void newUnit() {
        this.dto = new UnitDTO();
        this.setLabel("Hozzáadás");
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public UnitDTO getDto() {
        if (this.dto == null) {
            this.dto = new UnitDTO();
        }
        return this.dto;
    }

    public String getLabel() {
        return label;
    }

    public List<SortMeta> getSortBy() {
        return sortBy;
    }

    public List<UnitDTO> getDtoList() {
        return dtoList;
    }

    public UnitService getService() {
        return service;
    }
}
