package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Site;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode
public class PurchaseDTO implements Serializable {
    private Integer id;

    private List<PurchasedProductDTO> productList;

    private VendorDTO vendor;

    private Date receiptDate;

    private SiteDTO site;

    private String notes;

    private Double totalPrice;

    private Date bookedDate;

}
