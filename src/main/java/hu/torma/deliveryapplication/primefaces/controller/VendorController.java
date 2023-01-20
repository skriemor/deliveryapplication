package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.VendorDTO;
import hu.torma.deliveryapplication.service.VendorService;
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
@Controller("vendorController")
public class VendorController implements Serializable {

    private List<SortMeta> sortBy;
    @Autowired
    VendorService vService;

    private String label;
    private VendorDTO dto;
    private List<VendorDTO> dtoList;

    @PostConstruct
    public void init() {
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder()
                .field("tax_id")
                .order(SortOrder.ASCENDING)
                .build());
    }

    @PostConstruct
    public void getAllVendors() {
        this.dtoList = vService.getAllVendors();
        this.setLabel("Hozzáadás");
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

    public void deleteVendor(VendorDTO _dto) {
        vService.deleteVendor(_dto);
        this.getAllVendors();
    }
    public void editVendor(VendorDTO _dto) {
        this.setLabel("Módosítás");
        BeanUtils.copyProperties(_dto, this.getDto());
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
}
