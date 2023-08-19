package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.entity.Site;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;

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
        return Objects.equals(id, that.id) && Objects.equals(productList, that.productList) && Objects.equals(vendor, that.vendor) && Objects.equals(receiptDate, that.receiptDate) && Objects.equals(site, that.site) && Objects.equals(notes, that.notes) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(remainingPrice, that.remainingPrice) && Objects.equals(bookedDate, that.bookedDate) && Objects.equals(receiptId, that.receiptId);
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

    @Override
    public String toString() {
        return "PurchaseDTO{" +
                "id=" + id +
                ", productList=" + productList +
                ", vendor=" + vendor +
                ", receiptDate=" + receiptDate +
                ", site=" + site +
                ", notes='" + notes + '\'' +
                ", totalPrice=" + totalPrice +
                ", remainingPrice=" + remainingPrice +
                ", bookedDate=" + bookedDate +
                ", receiptId='" + receiptId + '\'' +
                '}';
    }
}
