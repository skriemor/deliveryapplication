package hu.torma.deliveryapplication.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class PurchaseSelectorMinimalDTO {
    private Integer id;
    private VendorDTO vendor;
    private Double remainingPrice;
    private Double totalPrice;
}
