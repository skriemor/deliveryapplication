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
    private Integer unitPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "quantity2")
    private Integer quantity2;

    @Column(name = "actual_quantity")
    private Integer actual;

    @Column(name = "correction_percent")
    private Integer corrPercent;


    @Column(name = "total_price")
    private Integer totalPrice;

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