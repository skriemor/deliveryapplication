package hu.torma.deliveryapplication.DTO;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageSnapshotDTO implements Serializable {
    private Long id;
    private Integer one;
    private Integer two;
    private Integer three;
    private Integer four;
    private Integer five;
    private Integer six;
    private Integer sum;
    private Date dateFrom;
    private Date dateTo;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageSnapshotDTO that = (StorageSnapshotDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(one, that.one) && Objects.equals(two, that.two) && Objects.equals(three, that.three) && Objects.equals(four, that.four) && Objects.equals(five, that.five) && Objects.equals(six, that.six) && Objects.equals(sum, that.sum) && Objects.equals(dateFrom, that.dateFrom) && Objects.equals(dateTo, that.dateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, one, two, three, four, five, six, sum, dateFrom, dateTo);
    }

    @Override
    public String toString() {
        return "StorageSnapshotDTO{" +
                "id=" + id +
                ", one=" + one +
                ", two=" + two +
                ", three=" + three +
                ", four=" + four +
                ", five=" + five +
                ", six=" + six +
                ", sum=" + sum +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                '}';
    }
}