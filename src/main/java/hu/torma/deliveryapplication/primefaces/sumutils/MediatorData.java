package hu.torma.deliveryapplication.primefaces.sumutils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MediatorData {
    private String mediatorName, productName;
    private Integer quantity, totalPrice;

    public MediatorData(String mediatorName, String productName, Integer quantity, Integer totalPrice) {
        this.mediatorName = mediatorName;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
