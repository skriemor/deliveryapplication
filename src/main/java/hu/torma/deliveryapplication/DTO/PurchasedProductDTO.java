package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Purchase;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class PurchasedProductDTO implements Serializable {
    private Integer id;

    private Double unitPrice;

    private Integer quantity;

    private Double corrPercent;

    private Double corrPerUnit;

    private Double corrFt;

    private Integer totalPrice;

    private Purchase purchase;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchasedProductDTO that = (PurchasedProductDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(quantity, that.quantity) && Objects.equals(corrPercent, that.corrPercent) && Objects.equals(corrPerUnit, that.corrPerUnit) && Objects.equals(corrFt, that.corrFt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unitPrice, quantity, corrPercent, corrPerUnit, corrFt);
    }
}
