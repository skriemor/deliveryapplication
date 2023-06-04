package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.entity.Site;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class PurchaseDTO implements Serializable {
    private Integer id;

    private List<PurchasedProductDTO> productList;

    private VendorDTO vendor;

    private Date receiptDate;

    private SiteDTO site;

    private String notes;

    private Double totalPrice;

    private Double remainingPrice;

    private Date bookedDate;

    private String receiptId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseDTO that = (PurchaseDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(vendor, that.vendor) && Objects.equals(receiptDate, that.receiptDate) && Objects.equals(site, that.site) && Objects.equals(notes, that.notes) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(remainingPrice, that.remainingPrice) && Objects.equals(bookedDate, that.bookedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vendor, receiptDate, site, notes, totalPrice, remainingPrice, bookedDate);
    }
}
