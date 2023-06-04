package hu.torma.deliveryapplication.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "sale")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "sale", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchasedProduct> productList;

    @ManyToOne
    @JoinColumn(name = "buyer_name", nullable = false, referencedColumnName = "id")
    private Buyer buyer;

    @Column(nullable = false, name = "currency")
    private String currency;

    @Column(nullable = false, name = "price")
    private Double price;

    @Column(name = "booking_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date bookingDate;

    @Column(name = "deadline_date", nullable = true)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date deadLine;

    @Column(name = "completion_date", nullable = true)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date completionDate;

    @Column(name = "receipt_date", nullable = true)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date receiptDate;

    @Column(nullable = true, name = "receipt_id")
    private String receiptId;

}