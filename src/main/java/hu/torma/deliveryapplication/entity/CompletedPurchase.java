package hu.torma.deliveryapplication.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NamedNativeQuery(name = "supply_completed_purchases_with_dates", resultSetMapping = "product_with_quantity_mapping", query = """

select namese.c1 as product,
(Select
CASE
When namese.c1 like 'I.O%' THEN sum(r.one)
When namese.c1 like 'II.O%' THEN sum(r.two)
When namese.c1 like 'III.O%' THEN sum(r.three)
When namese.c1 like 'IV.O%' THEN sum(r.four)
When namese.c1 like 'GY%' THEN sum(r.five)
When namese.c1 like 'IPA%' THEN sum(r.six)
ELSE 0
END
from records r
join completed_purchase cp on cp.id = r.completed_id
where (
(:date1 is not null and :date2 is not null and cp.receipt_date between :date1  and :date2)
                                            or (:date1 is not null and :date2 is null and  cp.receipt_date >= :date1 )
                                            or (:date2 is not null and :date1 is null and  cp.receipt_date <  :date2 )
                                            or (:date1 is null and  :date2 is null)
                              
                        )


) as quantity
from (
values
('I.OSZTÁLYÚ'),
('II.OSZTÁLYÚ'),
('III.OSZTÁLYÚ'),
('IV.OSZTÁLYÚ'),
('GYÖKÉR'),
('IPARI')
) namese


""")
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

    @Column(name = "serial")
    private Integer serial;

    @Column(name = "new_serial")
    private String newSerial;

    @Column(name = "receipt_date")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date receiptDate;

    @Column(name = "payment_date")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date paymentDate;

    @ManyToOne
    @JoinColumn(name = "site", nullable = false, referencedColumnName = "site_id")
    private Site site;

    @Column(name = "notes")
    private String notes;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "total_price")
    private int totalPrice;

    @OneToMany(mappedBy = "completedPurchase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CompletionRecord> records;
}
