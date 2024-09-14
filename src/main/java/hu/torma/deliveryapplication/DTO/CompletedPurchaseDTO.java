package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.CompletedPurchase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletedPurchaseDTO implements Serializable {
    private List<CompletionRecordDTO> records;
    Integer id;
    Integer serial;
    VendorDTO vendor;
    String newSerial;
    Date receiptDate;
    SiteDTO site;
    String notes;
    Double totalPrice;
    String paymentMethod;
    Date paymentDate;

    int one;
    int two;
    int three;
    int four;
    int five;
    int six;

    public CompletedPurchaseDTO(int one, int two, int three, int four, int five, int six) {
        this.newSerial = "Összesen";
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
        this.six = six;
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
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletedPurchaseDTO that = (CompletedPurchaseDTO) o;
        return one == that.one && two == that.two && three == that.three && four == that.four && five == that.five && six == that.six && Objects.equals(id, that.id) && Objects.equals(vendor, that.vendor) && Objects.equals(receiptDate, that.receiptDate) && Objects.equals(site, that.site) && Objects.equals(notes, that.notes) && Objects.equals(totalPrice, that.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vendor, one, two, three, four, five, six, receiptDate, site, notes, totalPrice);
    }


    public String getFormattedTotalPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(this.totalPrice).replaceAll(",", " ");
    }

    public String getIntedTotalPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(this.totalPrice).replaceAll(",", "");

    }

    public int getOne() {
        return this.records == null ? 0 : this.records.stream().mapToInt(CompletionRecordDTO::getOne).sum();
    }

    public int getTwo() {
        return this.records == null ? 0 : this.records.stream().mapToInt(CompletionRecordDTO::getTwo).sum();
    }

    public int getThree() {
        return this.records == null ? 0 : this.records.stream().mapToInt(CompletionRecordDTO::getThree).sum();
    }

    public int getFour() {
        return this.records == null ? 0 : this.records.stream().mapToInt(CompletionRecordDTO::getFour).sum();
    }

    public int getFive() {
        return this.records == null ? 0 : this.records.stream().mapToInt(CompletionRecordDTO::getFive).sum();
    }

    public int getSix() {
        return this.records == null ? 0 : this.records.stream().mapToInt(CompletionRecordDTO::getSix).sum();
    }

    public CompletedPurchase toEntity(boolean includeVendor, boolean includeRecords) {
        CompletedPurchase entity = new CompletedPurchase();
        entity.setId(this.id);
        entity.setOne(this.one);
        entity.setTwo(this.two);
        entity.setThree(this.three);
        entity.setFour(this.four);
        entity.setFive(this.five);
        entity.setSix(this.six);
        entity.setSerial(this.serial);
        entity.setNewSerial(this.newSerial);
        entity.setReceiptDate(this.receiptDate);
        entity.setPaymentDate(this.paymentDate);
        entity.setNotes(this.notes);
        entity.setPaymentMethod(this.paymentMethod);
        entity.setTotalPrice(this.totalPrice);

        if (includeVendor && this.vendor != null) {
            entity.setVendor(this.vendor.toEntity(false));
        }

        if (this.site != null) {
            entity.setSite(this.site.toEntity());
        }

        if (includeRecords && this.records != null) {
            entity.setRecords(this.records.stream()
                    .map(record -> record.toEntity(false, false)) // Avoid recursion
                    .collect(Collectors.toList()));
        }

        return entity;
    }
}
