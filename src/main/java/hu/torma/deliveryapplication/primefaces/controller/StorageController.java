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
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
@Controller
public class StorageController {
    /**
     * The new displays for storage
     */
    Logger logger = Logger.getLogger("Storage");
    private List<ProductWithQuantity> purchaseColumn;
    private List<ProductWithQuantity> saleColumn;
    private StorageSnapshotDTO storageSnapshotDTO;
    private ArrayList<StorageSnapshotDTO> snapshotDTOs;
    @Autowired
    PurchaseService purchaseService;
    @Autowired
    SnapshotService snapshotService;
    @Autowired
    SaleService saleService;

    @PostConstruct
    public void init() {
        newSnapshot();
        storageSnapshotDTO.setDateFrom(Date.from(Instant.now().minus(7, ChronoUnit.DAYS)));
        storageSnapshotDTO.setDateTo(Date.from(Instant.now()));
        updateCols();
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

    public void updateSum() {
        StorageSnapshotDTO sn = this.storageSnapshotDTO;
        sn.setSum(
                sn.getOne()
                        + sn.getTwo()
                        + sn.getThree()
                        + sn.getFour()
                        + sn.getFive()
                        + sn.getSix()
        );
    }

    public void saveSnapshot() {
        if (storageSnapshotDTO.getDateFrom() == null
                || storageSnapshotDTO.getDateTo() == null) {
            return;
        }
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
