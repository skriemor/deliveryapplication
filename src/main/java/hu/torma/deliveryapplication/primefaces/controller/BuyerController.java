package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.BuyerDTO;
import hu.torma.deliveryapplication.service.BuyerService;
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

@SessionScope
@Controller("buyerController")
public class BuyerController implements Serializable {
    private List<SortMeta> sortBy;
    @Autowired
    BuyerService vService;

    private String label;
    private BuyerDTO dto;
    private List<BuyerDTO> dtoList;
    private String dateRange;

    @PostConstruct
    public void init() {
        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder()
                .field("accountNum")
                .order(SortOrder.ASCENDING)
                .build());
        dateRange = (LocalDate.now().getYear() - 120) + ":" + (LocalDate.now().getYear() - 14);
    }

    @PostConstruct
    public void getAllBuyers() {
        this.dtoList = vService.getAllBuyers();
        this.setLabel("Hozzáadás");
    }

    public void setDto(BuyerDTO dto) {
        this.dto = dto;
    }

    public void setDtoList(List<BuyerDTO> dtoList) {
        this.dtoList = dtoList;
    }

    public void setSortBy(List<SortMeta> sortBy) {
        this.sortBy = sortBy;
    }

    public void uiSaveBuyer() {
        vService.saveBuyer(this.dto);
        getAllBuyers();
        this.setDto(new BuyerDTO());
    }

    public void deleteBuyer() {
        if (this.dto != null) {
            vService.deleteBuyer(this.dto);
        }
        this.getAllBuyers();
        this.dto = new BuyerDTO();
    }

    public void editBuyer(SelectEvent<BuyerDTO> _dto) {
        this.setLabel("Módosítás");
        BeanUtils.copyProperties(_dto.getObject(), this.getDto());
    }

    public void newBuyer() {
        this.dto = new BuyerDTO();
        this.setLabel("Hozzáadás");
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BuyerDTO getDto() {
        if (this.dto == null) {
            this.dto = new BuyerDTO();
        }
        return this.dto;
    }

    public String getLabel() {
        return label;
    }

    public List<SortMeta> getSortBy() {
        return sortBy;
    }

    public List<BuyerDTO> getDtoList() {
        return dtoList;
    }

    public BuyerService getvService() {
        return vService;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getDateRange() {
        return dateRange;
    }
}
