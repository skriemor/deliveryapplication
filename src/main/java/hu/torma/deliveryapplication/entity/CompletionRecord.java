package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.DTO.CompletionRecordDTO;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "records")
public class CompletionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "one", nullable = false)
    private int one;
    @Column(name = "two", nullable = false)
    private int two;
    @Column(name = "three", nullable = false)
    private int three;
    @Column(name = "four", nullable = false)
    private int four;
    @Column(name = "five", nullable = false)
    private int five;
    @Column(name = "six", nullable = false)
    private int six;

    @Column(name = "price")
    private Double price;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_id", referencedColumnName = "id")
    private CompletedPurchase completedPurchase;



    public CompletionRecordDTO toDTO(boolean includePurchase, boolean includeCompletedPurchase) {
        CompletionRecordDTO dto = new CompletionRecordDTO();
        dto.setId(this.id);
        dto.setOne(this.one);
        dto.setTwo(this.two);
        dto.setThree(this.three);
        dto.setFour(this.four);
        dto.setFive(this.five);
        dto.setSix(this.six);
        dto.setPrice(this.price);

        if (this.completedPurchase != null && Hibernate.isInitialized(this.completedPurchase) && !(this.completedPurchase instanceof HibernateProxy)) {
            dto.setCompletedPurchaseId(this.completedPurchase.getId());
        }

        if (this.purchase != null && Hibernate.isInitialized(this.purchase) && !(this.purchase instanceof HibernateProxy)) {
            dto.setPurchaseId(this.purchase.getId());
        }

        if (includePurchase && Hibernate.isInitialized(this.purchase) && !(this.purchase instanceof HibernateProxy) && this.purchase != null) {
            dto.setPurchase(this.purchase.toDTO(false, false, true)); // Pass false to avoid recursion
        }

        if (includeCompletedPurchase && this.completedPurchase != null && Hibernate.isInitialized(this.completedPurchase) && !(this.completedPurchase instanceof HibernateProxy)) {
            dto.setCompletedPurchase(this.completedPurchase.toDTO(false)); // Pass false to avoid recursion
        }

        return dto;
    }

}
