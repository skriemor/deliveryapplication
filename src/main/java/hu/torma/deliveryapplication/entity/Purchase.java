package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.primefaces.sumutils.ProductWithQuantity;
import lombok.Data;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "purchase")
@NamedNativeQueries(
        @NamedNativeQuery(name = "supply_products_with_quantity", query = """
                select pp.p_id as product, sum(quantity2) as quantity
                                from purchased_product pp
                                join purchase p on p.id = pp.purchase_id
                                where purchase_id is not null
                                and (
                                (:date1 is not null and :date2 is not null and p.receipt_date between :date1  and :date2)
                                                    or (:date1 is not null and :date2 is null and  p.receipt_date >= :date1 )
                                                    or (:date2 is not null and :date1 is null and  p.receipt_date <  :date2 )
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
                                """
                , resultSetMapping = "product_with_quantity_mapping")
)
@SqlResultSetMappings(
        @SqlResultSetMapping(name = "product_with_quantity_mapping",
                classes = @ConstructorResult(
                        targetClass = ProductWithQuantity.class,
                        columns = {
                                @ColumnResult(type = String.class, name = "product"),
                                @ColumnResult(type = Integer.class, name = "quantity")
                        }
                ))
)
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToMany(mappedBy = "purchase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<PurchasedProduct> productList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_name", nullable = false, referencedColumnName = "tax_id")
    private Vendor vendor;


    @Column(name = "receipt_date")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date receiptDate;

    @ManyToOne
    @JoinColumn(name = "site", referencedColumnName = "site_id")
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

    @OneToMany(mappedBy = "purchase", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    List<CompletionRecord> records;

    public PurchaseDTO toDTO(boolean includeVendor, boolean includeRecords, boolean includePurchasedProducts) {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setId(this.id);

        if (includeVendor && this.vendor != null && Hibernate.isInitialized(this.vendor) && !(this.vendor instanceof HibernateProxy)) {
            dto.setVendor(this.vendor.toDTO(true));
        }

        dto.setReceiptDate(this.receiptDate);

        if (Hibernate.isInitialized(this.site) && !(this.site instanceof HibernateProxy) && this.site != null) {
            dto.setSite(this.site.toDTO());
        }

        dto.setNotes(this.notes);
        dto.setReceiptId(this.receiptId);
        dto.setTotalPrice(this.totalPrice);
        dto.setRemainingPrice(this.remainingPrice);
        dto.setBookedDate(this.bookedDate);

        if (includeRecords && Hibernate.isInitialized(this.records) && this.records != null) {
            dto.setRecords(this.records.stream()
                    .filter(record -> Hibernate.isInitialized(record) && !(record instanceof HibernateProxy))
                    .map(record -> record.toDTO(false, true)) // Pass false to avoid looping back
                    .collect(Collectors.toList()));
        }

        if (includePurchasedProducts && Hibernate.isInitialized(this.productList) && this.productList != null) {
            dto.setProductList(this.productList.stream()
                    .filter(product -> Hibernate.isInitialized(product) && !(product instanceof HibernateProxy))
                    .map(product -> product.toDTO(true, false, true)) // Pass false to avoid looping back
                    .toList());
        }

        return dto;
    }


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Purchase purchase = (Purchase) o;
        return getId() != null && Objects.equals(getId(), purchase.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}