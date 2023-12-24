package hu.torma.deliveryapplication.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode
@ToString
@Table(name = "official_storage_snapshot")
public class OfficialStorageSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "one", nullable = false)
    private Integer one;
    @Column(name = "two", nullable = false)
    private Integer two;
    @Column(name = "three", nullable = false)
    private Integer three;
    @Column(name = "four", nullable = false)
    private Integer four;
    @Column(name = "five", nullable = false)
    private Integer five;
    @Column(name = "six", nullable = false)
    private Integer six;
    @Column(name = "sum", nullable = false)
    private Integer sum;

    @Column(name = "date_from", nullable = false)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date dateFrom;
    @Column(name = "date_to", nullable = false)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date dateTo;

    @OneToOne(targetEntity = OfficialStorageSnapshot.class, orphanRemoval = false, optional = true)
    @JoinColumn(name = "previous_id", nullable = true, referencedColumnName = "id")
    private OfficialStorageSnapshot previous;
}
