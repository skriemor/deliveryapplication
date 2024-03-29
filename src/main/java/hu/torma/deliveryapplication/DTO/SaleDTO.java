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
    private Boolean letai;
    private Boolean globalgap;

    public String getGlobalAsString() {
        if (globalgap == null) return "-";
        return globalgap?"+":"-";
    }
    public String getLetaiAsString() {
        if (letai == null) return "-";
        return letai?"+":"-";
    }
    public String getFormattedPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(price).replaceAll(","," ");
    }
    public String getIntedPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(price).replaceAll(",","");
    }

    public Integer getOne() {
        return this.productList.stream().filter(pp->pp.getProduct().getId().equals("I.OSZTÁLYÚ")).mapToInt(PurchasedProductDTO::getQuantity).sum();
    }
    public Integer getTwo() {
        return this.productList.stream().filter(pp->pp.getProduct().getId().equals("II.OSZTÁLYÚ")).mapToInt(PurchasedProductDTO::getQuantity).sum();
    }
    public Integer getThree() {
        return this.productList.stream().filter(pp->pp.getProduct().getId().equals("III.OSZTÁLYÚ")).mapToInt(PurchasedProductDTO::getQuantity).sum();
    }
    public Integer getFour() {
        return this.productList.stream().filter(pp->pp.getProduct().getId().equals("IV.OSZTÁLYÚ")).mapToInt(PurchasedProductDTO::getQuantity).sum();
    }
    public Integer getFive() {
        return this.productList.stream().filter(pp->pp.getProduct().getId().equals("GYÖKÉR")).mapToInt(PurchasedProductDTO::getQuantity).sum();
    }
    public Integer getSix() {
        return this.productList.stream().filter(pp->pp.getProduct().getId().equals("IPARI")).mapToInt(PurchasedProductDTO::getQuantity).sum();
    }
}