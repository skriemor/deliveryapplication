package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Buyer;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Buyer} entity
 */
@Data
public class BuyerDTO implements Serializable {
    private String accountNum;
    private String name;
    private String paper;
    private String country;
    private String address;

}