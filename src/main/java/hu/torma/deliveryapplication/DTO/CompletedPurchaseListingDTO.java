package hu.torma.deliveryapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletedPurchaseListingDTO {
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
    private List<RecordForDateComparisonDTO> records;

    int one;
    int two;
    int three;
    int four;
    int five;
    int six;
    int totalPrice;

    public String getFormattedTotalPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(this.totalPrice).replaceAll(",", " ");
    }

    public String getIntedTotalPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(this.totalPrice).replaceAll(",", "");
    }

}
