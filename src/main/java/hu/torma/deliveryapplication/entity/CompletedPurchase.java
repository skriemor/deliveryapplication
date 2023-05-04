package hu.torma.deliveryapplication.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "completed_purchase")
public class CompletedPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vendor_name", nullable = false, referencedColumnName = "tax_id")
    private Vendor vendor;

    @Column(name = "one")
    private int one;

    @Column(name = "two")
    private int two;

    @Column(name = "three")
    private int three;

    @Column(name = "four")
    private int four;
    @Column(name = "five")
    private int five;

    @Column(name = "six")
    private int six;

    @Column(name = "receipt_date")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date receiptDate;

    @ManyToOne
    @JoinColumn(name = "site", nullable = false, referencedColumnName = "site_id")
    private Site site;

    @Column(name = "notes")
    private String notes;

    @Column(name = "total_price")
    private Double totalPrice;


    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

}
