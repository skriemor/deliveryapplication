package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.entity.Site;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;

@Data
@NoArgsConstructor
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
        return Objects.equals(id, that.id)  && Objects.equals(vendor, that.vendor) && Objects.equals(receiptDate, that.receiptDate) && Objects.equals(site, that.site) && Objects.equals(notes, that.notes) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(remainingPrice, that.remainingPrice) && Objects.equals(bookedDate, that.bookedDate) && Objects.equals(receiptId, that.receiptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productList, vendor, receiptDate, site, notes, totalPrice, remainingPrice, bookedDate, receiptId);
    }

    public String getFormattedTotalPrice(){
        return NumberFormat.getNumberInstance(Locale.US).format(this.totalPrice).replaceAll(","," ");
    }

    public String getFormattedRemainingPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(this.remainingPrice).replaceAll(","," ");
    }

    public String getIntedTotalPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(this.totalPrice).replaceAll(",","");

    }
}
