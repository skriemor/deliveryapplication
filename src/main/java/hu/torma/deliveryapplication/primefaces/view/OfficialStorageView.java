package hu.torma.deliveryapplication.primefaces.view;


import hu.torma.deliveryapplication.DTO.OfficialStorageSnapshotDTO;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import hu.torma.deliveryapplication.service.CompletedPurchaseService;
import hu.torma.deliveryapplication.service.OSnapshotService;
import hu.torma.deliveryapplication.service.SaleService;
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
public class OfficialStorageView {
    /**
     * The new displays for official storage
     */
    Logger logger = Logger.getLogger("Storage");
    private List<ProductWithQuantity> cPurchaseColumn;
    private List<ProductWithQuantity> saleColumn;
    private OfficialStorageSnapshotDTO snapDto;
    private OfficialStorageSnapshotDTO prevDto;
    private OfficialStorageSnapshotDTO nulledDto;
    private ArrayList<OfficialStorageSnapshotDTO> snapshotDTOs;
    @Autowired
    CompletedPurchaseService cPurchaseService;
    @Autowired
    OSnapshotService snapshotService;
    @Autowired
    SaleService saleService;

    @PostConstruct
    public void init() {
        newSnapshot();
        nulledDto = generateEmptySnapshot();
        prevDto = generateEmptySnapshot();
        snapDto.setDateFrom(Date.from(Instant.now().minus(7, ChronoUnit.DAYS)));
        snapDto.setDateTo(Date.from(Instant.now()));
        updateCols();
        updateDTOs();
    }

    private void resetDatesOf(OfficialStorageSnapshotDTO dto) {
        dto.setDateFrom(null);
        dto.setDateTo(null);
    }
    public void uiResetFields() {
        newSnapshot();
        snapDto.setDateFrom(Date.from(Instant.now()));
        snapDto.setDateTo(Date.from(Instant.now()));
        prevDto = generateEmptySnapshot();
        updateCols();
        resetDatesOf(snapDto);
    }

    public void newSnapshot() {
        snapDto = generateEmptySnapshot();
    }

    public OfficialStorageSnapshotDTO generateEmptySnapshot() {
        return new OfficialStorageSnapshotDTO(
                0L, 0, 0, 0, 0, 0, 0, 0,
                null,
                null,
                null
        );
    }

    public OfficialStorageSnapshotDTO getSnapDto() {
        if (this.snapDto == null) {
            newSnapshot();
        }
        return this.snapDto;
    }

    public OfficialStorageSnapshotDTO getPrevDto() {
        if (this.prevDto == null || this.prevDto.getOne() == null) {
            this.prevDto = generateEmptySnapshot();
        }
        return this.prevDto;
    }
    public void updateCols() {
        updatePurchaseColumn();
        updateSaleColumn();
    }

    private void updateSaleColumn() {
        saleColumn = saleService.getOfficialSalesByDates(snapDto.getDateFrom(), snapDto.getDateTo());
        saleColumn.add(new ProductWithQuantity("sum", saleColumn.stream().mapToInt(ProductWithQuantity::getQuantity).sum()));
    }

    private void updatePurchaseColumn() {
        cPurchaseColumn = cPurchaseService.getCpsByDatesAsProductWithQuantities(snapDto.getDateFrom(), snapDto.getDateTo());
        cPurchaseColumn.add(new ProductWithQuantity("sum", cPurchaseColumn.stream().mapToInt(ProductWithQuantity::getQuantity).sum()));
    }


    public void selectSnapshot(SelectEvent<OfficialStorageSnapshotDTO> snapDto) {
        BeanUtils.copyProperties(snapDto.getObject(), this.snapDto);
        if (snapDto.getObject().getPrevious() != null){
            BeanUtils.copyProperties(snapDto.getObject().getPrevious(), this.getPrevDto());
        } else {
            this.prevDto = generateEmptySnapshot();
        }
        updateCols();
    }


    private void updateDTOs() {
        this.snapshotDTOs = new ArrayList<>(snapshotService.getAllSnapshots());
    }

    private void calculateAndSetSum(OfficialStorageSnapshotDTO dto) {
        dto.setSum(dto.getOne()
                + dto.getTwo()
                + dto.getThree()
                + dto.getFour()
                + dto.getFive()
                + dto.getSix());
    }
    public void updateSum() {
        OfficialStorageSnapshotDTO sn = this.snapDto;
        calculateAndSetSum(sn);
    }

    public void saveSnapshot() {
        if (snapDto.getDateFrom() == null
                || snapDto.getDateTo() == null) {
            return;
        }
        if (prevDto != null && prevDto.getId() != null && prevDto.getId() != 0L){
            snapDto.setPrevious(prevDto);
        }
        calculateAndSetSum(snapDto);
        snapshotService.saveSnapshot(snapDto);
        updateDTOs();
        uiResetFields();
    }

    public void deleteSnapshot() {
        snapshotService.deleteSnapshot(snapDto);
        updateDTOs();
        uiResetFields();
    }
}
