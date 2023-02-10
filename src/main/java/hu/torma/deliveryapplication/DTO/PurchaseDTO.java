package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.PurchasedProduct;
import hu.torma.deliveryapplication.entity.Site;
import hu.torma.deliveryapplication.entity.Vendor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class PurchaseDTO implements Serializable {
    private Integer id;

    private List<PurchasedProduct> productList;

    private Vendor vendor;

    private String paymentMethod;

    private Date paymentDate;

    private Date completionDate;

    private Date ticketDate;

    private Date receiptDate;

    private Site site;

    private String notes;

    private Integer totalPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseDTO that = (PurchaseDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(productList, that.productList) && Objects.equals(vendor, that.vendor) && Objects.equals(paymentMethod, that.paymentMethod) && Objects.equals(paymentDate, that.paymentDate) && Objects.equals(completionDate, that.completionDate) && Objects.equals(ticketDate, that.ticketDate) && Objects.equals(receiptDate, that.receiptDate) && Objects.equals(site, that.site) && Objects.equals(notes, that.notes) && Objects.equals(totalPrice, that.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productList, vendor, paymentMethod, paymentDate, completionDate, ticketDate, receiptDate, site, notes, totalPrice);
    }
}
