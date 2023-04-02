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
    private Double quantity;

    @Column(name = "quantity2")
    private Double quantity2;

    @Column(name = "correction_percent")
    private Double corrPercent;


    @Column(name = "total_price")
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "p_id", nullable = false, referencedColumnName = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    private Purchase purchase;


    @ManyToOne
    @JoinColumn(name = "sale_id", referencedColumnName = "id")
    private Sale sale;


}