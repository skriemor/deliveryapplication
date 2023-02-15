package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Product;
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

    private PurchaseDTO purchase;

    private ProductDTO product;

}
