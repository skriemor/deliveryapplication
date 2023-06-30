package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Sale;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A DTO for the {@link Sale} entity
 */
@Data
public class SaleDTO implements Serializable {
    private Integer id;
    private List<PurchasedProductDTO> productList;
    private BuyerDTO buyer;
    private String currency;
    private Date bookingDate;
    private String receiptId;
    private int price;
    private Date deadLine;
    private Date completionDate;
    private Date receiptDate;

    public String getFormattedPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(price).replaceAll(","," ");
    }
    public String getIntedPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(price).replaceAll(",","");
    }
}