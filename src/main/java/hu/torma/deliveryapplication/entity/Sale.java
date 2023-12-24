package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "sale")
@NamedNativeQueries( {
        @NamedNativeQuery(name = "supply_products_with_quantity_sale", query = """
        select pp.p_id as product, sum(quantity) as quantity
                        from purchased_product pp
                        join sale s on s.id = pp.sale_id
                        where pp.sale_id is not null
                        and (
                        (:date1 is not null and :date2 is not null and s.receipt_date between :date1  and :date2)
                                            or (:date1 is not null and :date2 is null and  s.receipt_date >= :date1 )
                                            or (:date2 is not null and :date1 is null and  s.receipt_date <  :date2 )
                                            or (:date1 is null and  :date2 is null)
                              
                        )
                        
                        
                        group by pp.p_id
                        ORDER BY CASE
                        WHEN pp.p_id like 'I.O%' then 1
                        WHEN pp.p_id like 'II.O%' then 2
                        WHEN pp.p_id like 'III.O%' then 3
                        WHEN pp.p_id like 'IV.O%' then 4
                        WHEN pp.p_id like 'GYÖK%' then 5
                        WHEN pp.p_id like 'IP%' then 6
                        END ASC
                        """, resultSetMapping = "product_with_quantity_mapping"),


        @NamedNativeQuery(name = "supply_official_products_with_quantity_sale", query = """
        select pp.p_id as product, sum(quantity) as quantity
                        from purchased_product pp
                        join sale s on s.id = pp.sale_id
                        join buyer b on b.id = s.buyer_name
                        where pp.sale_id is not null
                        and (b.paper like '%Igen%')
                        and (
                        (:date1 is not null and :date2 is not null and s.receipt_date between :date1  and :date2)
                                            or (:date1 is not null and :date2 is null and  s.receipt_date >= :date1 )
                                            or (:date2 is not null and :date1 is null and  s.receipt_date <  :date2 )
                                            or (:date1 is null and  :date2 is null)
                              
                        )
                        
                        
                        group by pp.p_id
                        ORDER BY CASE
                        WHEN pp.p_id like 'I.O%' then 1
                        WHEN pp.p_id like 'II.O%' then 2
                        WHEN pp.p_id like 'III.O%' then 3
                        WHEN pp.p_id like 'IV.O%' then 4
                        WHEN pp.p_id like 'GYÖK%' then 5
                        WHEN pp.p_id like 'IP%' then 6
                        END ASC
                        """, resultSetMapping = "product_with_quantity_mapping")
})
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
    private int price;

    @Column(nullable = true, name = "letai")
    private Boolean letai;

    @Column(nullable = true, name = "globalgap")
    private Boolean globalgap;

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