package hu.torma.deliveryapplication.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "purchased_product")
public class PurchasedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer Id;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "correction_percent")
    private Double corrPercent;

    @Column(name = "correction_per_unit")
    private Double corrPerUnit;

    @Column(name = "correction_ft")
    private Double corrFt;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;


    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;


}