package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.SiteDTO;
import hu.torma.deliveryapplication.DTO.VendorDTO;
import hu.torma.deliveryapplication.service.SiteService;
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
@Controller("siteController")
public class SiteController implements Serializable {
    private List<SortMeta> sortBy;
    @Autowired
    SiteService service;

    private String label;
    private SiteDTO dto;
    private List<SiteDTO> dtoList;

    @PostConstruct
    public void init() {
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder()
                .field("id")
                .order(SortOrder.ASCENDING)
                .build());
    }

    @PostConstruct
    public void getAllSites() {
        this.dtoList = service.getAllSites();
        this.setLabel("Hozzáadás");
    }

    public void setDto(SiteDTO dto) {
        this.dto = dto;
    }

    public void setDtoList(List<SiteDTO> dtoList) {
        this.dtoList = dtoList;
    }

    public void setSortBy(List<SortMeta> sortBy) {
        this.sortBy = sortBy;
    }

    public void uiSaveSite() {
        service.saveSite(this.dto);
        getAllSites();
        this.setDto(new SiteDTO());
    }

    public void deleteSite() {
        if (this.dto != null) {
            service.deleteSite(this.dto);
        }
        this.getAllSites();
        this.dto = new SiteDTO();
    }

    public void editSite(SelectEvent<SiteDTO> _dto) {
        this.setLabel("Módosítás");
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());
    }

    public void newSite() {
        this.dto = new SiteDTO();
        this.setLabel("Hozzáadás");
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public SiteDTO getDto() {
        if (this.dto == null) {
            this.dto = new SiteDTO();
        }
        return this.dto;
    }

    public String getLabel() {
        return label;
    }

    public List<SortMeta> getSortBy() {
        return sortBy;
    }

    public List<SiteDTO> getDtoList() {
        return dtoList;
    }

    public SiteService getService() {
        return service;
    }
}
