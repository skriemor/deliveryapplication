package hu.torma.deliveryapplication.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CompletedPurchaseDTO implements Serializable {
    private final Integer id;
    private final VendorDTO vendor;
    private final int one;
    private final int two;
    private final int three;
    private final int four;
    private final int five;
    private final int six;
    private final Date receiptDate;
    private final SiteDTO site;
    private final String notes;
    private final Double totalPrice;
    private final Date bookedDate;
    private final PurchaseDTO purchase;
}
