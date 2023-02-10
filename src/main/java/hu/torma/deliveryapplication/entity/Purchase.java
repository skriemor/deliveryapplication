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

    @OneToMany(mappedBy = "purchase")
    private List<PurchasedProduct> productList;

    @ManyToOne
    @JoinColumn(name = "vendor_name", nullable = false, referencedColumnName = "tax_id")
    private Vendor vendor;

    @Column(nullable = false, name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date paymentDate;

    @Column(name = "completion_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date completionDate;

    @Column(name = "ticket_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date ticketDate;

    @Column(name = "receipt_date", nullable = false)
    private Date receiptDate;

    @ManyToOne
    @JoinColumn(name = "site", nullable = false, referencedColumnName = "site_id")
    private Site site;

    @Column(name = "notes")
    private String notes;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

}