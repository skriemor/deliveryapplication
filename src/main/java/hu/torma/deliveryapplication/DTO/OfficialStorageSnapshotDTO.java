package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.OfficialStorageSnapshot;
import hu.torma.deliveryapplication.utility.dateutil.DateConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficialStorageSnapshotDTO implements Serializable {
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
    private OfficialStorageSnapshotDTO previous;

    public void setOne(Integer one) {
        if (one == null) {
            this.one = 0;
        } else {
            this.one = one;
        }
    }

    public void setTwo(Integer two) {
        if (two == null) {
            this.two = 0;
        } else {
            this.two = two;
        }
    }

    public void setThree(Integer three) {
        if (three == null) {
            this.three = 0;
        } else {
            this.three = three;
        }
    }

    public void setFour(Integer four) {
        if (four == null) {
            this.four = 0;
        } else {
            this.four = four;
        }
    }

    public void setFive(Integer five) {
        if (five == null) {
            this.five = 0;
        } else {
            this.five = five;
        }
    }

    public void setSix(Integer six) {
        if (six == null) {
            this.six = 0;
        } else {
            this.six = six;
        }
    }
    public String getAsSelectOneString() {
        return this.getId()
                + " ("
                + DateConverter.toDottedDate(this.getDateFrom())
                + " - "
                + DateConverter.toDottedDate(this.getDateTo())
                + ")";
    }

    @Override
    public String toString() {
        return "OfficialStorageSnapshotDTO{" +
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
                ", previous=" + previous +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfficialStorageSnapshotDTO that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(one, that.one)
                && Objects.equals(two, that.two)
                && Objects.equals(three, that.three)
                && Objects.equals(four, that.four)
                && Objects.equals(five, that.five)
                && Objects.equals(six, that.six)
                && Objects.equals(sum, that.sum)
                && compareDateStackSafe(dateFrom, that.dateFrom)
                && compareDateStackSafe(dateTo, that.dateTo)
                && comparePreviouses(previous, that.previous);
    }

    private boolean comparePreviouses(OfficialStorageSnapshotDTO one, OfficialStorageSnapshotDTO two) {
        if (one == null && two == null) {
            return true;
        } else if (one == null || two == null) {
            return false;
        } else if (one.getId() == null && two.getId() == null) {
            return true;
        } else if (one.getId() == null || two.getId() == null) {
            return false;
        } else if (one.getId().equals(two.getId())) {
            return true;
        }
        return false;
    }
    private boolean compareDateStackSafe(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return true;
        } else if (date1 == null || date2 == null) {
            return false;
        } else if (date1.getTime() == date2.getTime()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, one, two, three, four, five, six, sum, dateFrom, dateTo, previous);
    }

    public OfficialStorageSnapshot toEntity(boolean includePrevious) {
        OfficialStorageSnapshot entity = new OfficialStorageSnapshot();
        entity.setId(this.id);
        entity.setOne(this.one);
        entity.setTwo(this.two);
        entity.setThree(this.three);
        entity.setFour(this.four);
        entity.setFive(this.five);
        entity.setSix(this.six);
        entity.setSum(this.sum);
        entity.setDateFrom(this.dateFrom);
        entity.setDateTo(this.dateTo);

        if (includePrevious && this.previous != null) {
            entity.setPrevious(this.previous.toEntity(false)); // Avoid recursion
        }

        return entity;
    }

}