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

    @Column(name = "quantity2")
    private Integer quantity2;

    @Column(name = "correction_percent")
    private Double corrPercent;

    @Column(name = "correction_per_unit")
    private Double corrPerUnit;

    @Column(name = "correction_ft")
    private Double corrFt;

    @Column(name = "total_price")
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "p_id", nullable = false, referencedColumnName = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    private Purchase purchase;


}