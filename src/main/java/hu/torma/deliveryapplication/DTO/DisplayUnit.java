package hu.torma.deliveryapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisplayUnit {
    private int quantity;
    private String productName;
    public DisplayUnit(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }
}