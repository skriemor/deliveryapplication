package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.CompletionRecord;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link CompletionRecord} entity
 */
@Data
public class CompletionRecordDTO implements Serializable {
    private Integer id;
    private int one;
    private int two;
    private int three;
    private int four;
    private int five;
    private int six;
    private Integer purchaseId;
    private CompletedPurchaseDTO completedPurchase;

    private int price;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletionRecordDTO that = (CompletionRecordDTO) o;
        if (that.id ==null || this.id == null) return false;
        return one == that.one && two == that.two && three == that.three && four == that.four && five == that.five && six == that.six && Objects.equals(id, that.id) && Objects.equals(purchaseId, that.purchaseId) && Objects.equals(completedPurchase, that.completedPurchase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, one, two, three, four, five, six, purchaseId, completedPurchase);
    }
}