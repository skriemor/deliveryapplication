package hu.torma.deliveryapplication.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Data
@NoArgsConstructor
public class PurchaseMinimalDTO implements Serializable {
    private Integer id;
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
        PurchaseMinimalDTO that = (PurchaseMinimalDTO) o;
        return Objects.equals(id, that.id)  && Objects.equals(vendor, that.vendor) && Objects.equals(receiptDate, that.receiptDate) && Objects.equals(site, that.site) && Objects.equals(notes, that.notes) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(remainingPrice, that.remainingPrice) && Objects.equals(bookedDate, that.bookedDate) && Objects.equals(receiptId, that.receiptId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vendor, receiptDate, site, notes, totalPrice, remainingPrice, bookedDate, receiptId);
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
