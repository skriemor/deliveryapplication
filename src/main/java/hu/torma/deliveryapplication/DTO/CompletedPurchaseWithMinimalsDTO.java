package hu.torma.deliveryapplication.DTO;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CompletedPurchaseWithMinimalsDTO {
    private List<RecordWithMinimalsDTO> records;
    private Integer id;
    private Integer serial;
    // TODO: minimalize vendor
    private VendorDTO vendor;
    private String newSerial;
    private Date receiptDate;
    private SiteDTO site;
    private String notes;
    private int totalPrice;
    private Date bookedDate;
    private String paymentMethod;
    private Date paymentDate;
    private int one;
    private int two;
    private int three;
    private int four;
    private int five;
    private int six;

    public CompletedPurchaseIdOnlyDTO toIdOnly() {
        CompletedPurchaseIdOnlyDTO copy = new CompletedPurchaseIdOnlyDTO();
        copy.setId(this.id);
        return copy;
    }
}
