package hu.torma.deliveryapplication.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "purchase", /*fetch = FetchType.EAGER,*/ cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchasedProduct> productList;

    @ManyToOne
    @JoinColumn(name = "vendor_name", nullable = false, referencedColumnName = "tax_id")
    private Vendor vendor;



    @Column(name = "receipt_date")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date receiptDate;

    @ManyToOne
    @JoinColumn(name = "site", nullable = true, referencedColumnName = "site_id")
    private Site site;

    @Column(name = "notes")
    private String notes;

    @Column(name = "receipt_id")
    private String receiptId;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "remaining_price")
    private Double remainingPrice;

    @Column(name = "booking_date", nullable = true)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date bookedDate;

}