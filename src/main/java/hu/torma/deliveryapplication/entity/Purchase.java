package hu.torma.deliveryapplication.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "purchase")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "purchase", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PurchasedProduct> productList;

    @ManyToOne
    @JoinColumn(name = "vendor_name", nullable = false, referencedColumnName = "tax_id")
    private Vendor vendor;

    @Column(nullable = false, name = "payment_method")
    private String paymentMethod;

    @Column(name = "completion_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date completionDate;

    @Column(name = "ticket_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date ticketDate;

    @Column(name = "receipt_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date receiptDate;

    @Column(name = "receipt_number", nullable = false)
    private String receiptNum;
    @ManyToOne
    @JoinColumn(name = "site", nullable = false, referencedColumnName = "site_id")
    private Site site;

    @Column(name = "notes")
    private String notes;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "booking_date", nullable = true)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date bookedDate;

}