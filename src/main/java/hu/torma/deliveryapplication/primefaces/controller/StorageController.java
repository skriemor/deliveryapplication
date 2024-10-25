package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.StorageSnapshotDTO;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import hu.torma.deliveryapplication.service.PurchaseService;
import hu.torma.deliveryapplication.service.SaleService;
import hu.torma.deliveryapplication.service.SnapshotService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean("storageController")
@DependsOn("dbInit")
public class StorageController {
    @Getter
    private List<ProductWithQuantity> purchaseColumn;
    @Getter
    private List<ProductWithQuantity> saleColumn;
    @Setter
    private StorageSnapshotDTO storageSnapshotDTO;
    @Getter
    private ArrayList<StorageSnapshotDTO> snapshotDTOs;
    @Autowired
    PurchaseService purchaseService;
    @Autowired
    SnapshotService snapshotService;
    @Autowired
    SaleService saleService;

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null && facesContext.isPostback()) {
            return;
        }

        newSnapshot();
        storageSnapshotDTO.setDateFrom(Date.from(Instant.now()));
        storageSnapshotDTO.setDateTo(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
        updateCols();
        resetDatesOf(storageSnapshotDTO);
        updateDTOs();
    }

    private void resetDatesOf(StorageSnapshotDTO dto) {
        dto.setDateFrom(null);
        dto.setDateTo(null);
    }

    public void uiResetFields() {
        newSnapshot();
        storageSnapshotDTO.setDateFrom(Date.from(Instant.now()));
        storageSnapshotDTO.setDateTo(Date.from(Instant.now()));
        updateCols();
        resetDatesOf(storageSnapshotDTO);
    }

    public void newSnapshot() {
        storageSnapshotDTO = new StorageSnapshotDTO(
                0L, 0, 0, 0, 0, 0, 0, 0,
                null,
                null
        );
    }

    public StorageSnapshotDTO getStorageSnapshotDTO() {
        if (this.storageSnapshotDTO == null) {
            newSnapshot();
        }
        return this.storageSnapshotDTO;
    }

    public void updateCols() {
        updatePurchaseColumn();
        updateSaleColumn();
    }

    private void updateSaleColumn() {
        saleColumn = saleService.getSalesByDates(storageSnapshotDTO.getDateFrom(), storageSnapshotDTO.getDateTo());
        saleColumn.add(new ProductWithQuantity("sum", saleColumn.stream().mapToInt(ProductWithQuantity::getQuantity).sum()));
    }

    private void updatePurchaseColumn() {
        purchaseColumn = purchaseService.getPurchasesByDates(storageSnapshotDTO.getDateFrom(), storageSnapshotDTO.getDateTo());
        purchaseColumn.add(new ProductWithQuantity("sum", purchaseColumn.stream().mapToInt(ProductWithQuantity::getQuantity).sum()));
    }


    public void selectSnapshot(SelectEvent<StorageSnapshotDTO> storageSnapshotDTO) {
        BeanUtils.copyProperties(storageSnapshotDTO.getObject(), this.storageSnapshotDTO);
        updateCols();
    }

    private void resetDates(StorageSnapshotDTO dto) {
        dto.setDateFrom(null);
        dto.setDateTo(null);
    }

    private void updateDTOs() {
        this.snapshotDTOs = new ArrayList<>(snapshotService.getAllSnapshots());
    }

    private void calculateAndSetSum(StorageSnapshotDTO dto) {
        dto.setSum(dto.getOne()
                + dto.getTwo()
                + dto.getThree()
                + dto.getFour()
                + dto.getFive()
                + dto.getSix());
    }

    public void updateSum() {
        StorageSnapshotDTO sn = this.storageSnapshotDTO;
        calculateAndSetSum(sn);
    }

    public void saveSnapshot() {
        if (storageSnapshotDTO.getDateFrom() == null
                || storageSnapshotDTO.getDateTo() == null) {
            return;
        }
        calculateAndSetSum(storageSnapshotDTO);
        snapshotService.saveSnapshot(storageSnapshotDTO);
        updateDTOs();
        uiResetFields();
    }

    public void deleteSnapshot() {
        snapshotService.deleteSnapshot(storageSnapshotDTO);
        updateDTOs();
        uiResetFields();
    }
}
