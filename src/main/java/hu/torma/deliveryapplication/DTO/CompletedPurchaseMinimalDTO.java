package hu.torma.deliveryapplication.DTO;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CompletedPurchaseMinimalDTO implements Serializable {
    private Integer id;
    private VendorDTO vendor;
    private SiteDTO site;
    private Integer serial;
    private String newSerial;
    private String paymentMethod;
    private String notes;
    private Date receiptDate;
    private Date bookedDate;
    private Date paymentDate;

    int one;
    int two;
    int three;
    int four;
    int five;
    int six;
    int totalPrice;

    public CompletedPurchaseMinimalDTO(int one, int two, int three, int four, int five, int six) {
        this.newSerial = "Összesen";
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
        this.six = six;
    }

    public String getFormattedTotalPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(this.totalPrice).replaceAll(",", " ");
    }

    public String getIntedTotalPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(this.totalPrice).replaceAll(",", "");
    }

    @Override
    public String toString() {
        return "CompletedPurchaseDTO{" +
                "id=" + id +
                ", vendor=" + vendor +
                ", serial=" + serial +
                ", newSerial='" + newSerial + '\'' +
                ", receiptDate=" + receiptDate +
                ", site=" + site +
                ", notes='" + notes + '\'' +
                ", totalPrice=" + totalPrice +
                ", bookedDate=" + bookedDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletedPurchaseMinimalDTO that = (CompletedPurchaseMinimalDTO) o;
        return one == that.one && two == that.two && three == that.three && four == that.four && five == that.five && six == that.six && Objects.equals(id, that.id) && Objects.equals(vendor, that.vendor) && Objects.equals(receiptDate, that.receiptDate) && Objects.equals(site, that.site) && Objects.equals(notes, that.notes) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(bookedDate, that.bookedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vendor, one, two, three, four, five, six, receiptDate, site, notes, totalPrice, bookedDate);
    }
}
