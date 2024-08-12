package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.DTO.CompletionRecordDTO;
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
    private int price;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_id", referencedColumnName = "id")
    private CompletedPurchase completedPurchase;


    public CompletionRecordDTO toDTO() {
        CompletionRecordDTO dto = new CompletionRecordDTO();
        dto.setId(this.id);
        dto.setOne(this.one);
        dto.setTwo(this.two);
        dto.setThree(this.three);
        dto.setFour(this.four);
        dto.setFive(this.five);
        dto.setSix(this.six);
        dto.setPrice(this.price);
        return dto;
    }
}
