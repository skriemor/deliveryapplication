package hu.torma.deliveryapplication.entity;

import lombok.Data;


import javax.persistence.*;

@Entity
@Data
@Table(name = "quantity")
public class Quantity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @OneToOne
    private Product product;

}