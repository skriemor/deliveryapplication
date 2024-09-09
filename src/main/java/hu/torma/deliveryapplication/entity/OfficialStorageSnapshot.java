package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.DTO.OfficialStorageSnapshotDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
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

    @OneToOne(targetEntity = OfficialStorageSnapshot.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_id", nullable = true, referencedColumnName = "id")
    private OfficialStorageSnapshot previous;

    public OfficialStorageSnapshotDTO toDTO(boolean includePrevious) {
        OfficialStorageSnapshotDTO dto = new OfficialStorageSnapshotDTO();
        dto.setId(this.id);
        dto.setOne(this.one);
        dto.setTwo(this.two);
        dto.setThree(this.three);
        dto.setFour(this.four);
        dto.setFive(this.five);
        dto.setSix(this.six);
        dto.setSum(this.sum);
        dto.setDateFrom(this.dateFrom);
        dto.setDateTo(this.dateTo);

        if (includePrevious && Hibernate.isInitialized(this.previous) && !(this.previous instanceof HibernateProxy) && this.previous != null) {
            dto.setPrevious(this.previous.toDTO(false)); // Avoid recursion by passing false
        }

        return dto;
    }

}
