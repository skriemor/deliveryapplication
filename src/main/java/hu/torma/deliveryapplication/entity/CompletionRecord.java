package hu.torma.deliveryapplication.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "records")
public class CompletionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

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

    @Column(name = "price", nullable = true)
    private int price;

    @Column(name = "purchase_id")
    private Long purchaseId;

    @ManyToOne
    @JoinColumn(name = "completed_id", referencedColumnName = "id")
    private CompletedPurchase completedPurchase;


}
