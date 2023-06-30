package hu.torma.deliveryapplication.DTO;

import lombok.Data;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Data
public class CompletedPurchaseDTO implements Serializable {
    Integer id;
    VendorDTO vendor;
    int one;
    int two;
    int three;
    int four;
    int five;
    int six;
    Integer serial;
    Date receiptDate;
    SiteDTO site;
    String notes;
    int totalPrice;
    Date bookedDate;

    String paymentMethod;

    Date paymentDate;
    public List<CompletionRecordDTO> records;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletedPurchaseDTO that = (CompletedPurchaseDTO) o;
        return one == that.one && two == that.two && three == that.three && four == that.four && five == that.five && six == that.six && Objects.equals(id, that.id) && Objects.equals(vendor, that.vendor) && Objects.equals(receiptDate, that.receiptDate) && Objects.equals(site, that.site) && Objects.equals(notes, that.notes) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(bookedDate, that.bookedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vendor, one, two, three, four, five, six, receiptDate, site, notes, totalPrice, bookedDate, records);
    }


    public String getFormattedTotalPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(this.totalPrice).replaceAll(","," ");
    }
    public String getIntedTotalPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(this.totalPrice).replaceAll(",","");

    }
}
