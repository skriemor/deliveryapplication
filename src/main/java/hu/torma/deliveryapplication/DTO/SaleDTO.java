package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Sale;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    private String accountNumber;
    private Double price;
}