package hu.torma.deliveryapplication.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuantityDTO implements Serializable {

    private Integer id;

    private Double amount;

    private ProductDTO product;
}
