package hu.torma.deliveryapplication.entity;

import lombok.Data;


import javax.persistence.*;

@Entity
@Data
@Table(name = "quantity")
public class Quantity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @OneToOne
    private Product product;

}