package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.CompletedPurchase;
import hu.torma.deliveryapplication.entity.CompletionRecord;
import lombok.Data;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;
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
    private PurchaseDTO purchase;
    private CompletedPurchaseDTO completedPurchase;
    private Integer purchaseId;
    private Integer completedPurchaseId;

    private Double price;

    public String getFormattedPrice() {
        return NumberFormat.getNumberInstance(Locale.US).format(this.price).replaceAll(","," ");

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletionRecordDTO that = (CompletionRecordDTO) o;
        if (that.id == null || this.id == null) return false;
        return one == that.one && two == that.two && three == that.three && four == that.four && five == that.five && six == that.six && Objects.equals(id, that.id) && Objects.equals(purchase, that.purchase) && Objects.equals(completedPurchase, that.completedPurchase);
    }

    public CompletionRecord toEntity(boolean includePurchase, boolean includeCompletedPurchase) {
        CompletionRecord entity = new CompletionRecord();
        entity.setId(this.id);
        entity.setOne(this.one);
        entity.setTwo(this.two);
        entity.setThree(this.three);
        entity.setFour(this.four);
        entity.setFive(this.five);
        entity.setSix(this.six);
        entity.setPrice(this.price);

        if (includePurchase && this.purchase != null) {
            entity.setPurchase(this.purchase.toEntity(false, false, false)); // Avoid looping back by passing false
        }

        if (includeCompletedPurchase && this.completedPurchase != null) {
            entity.setCompletedPurchase(this.completedPurchase.toEntity(true, false)); // Avoid looping back by passing false
        } else if (this.completedPurchaseId != null) {
            CompletedPurchase completedPurchaseEntity = new CompletedPurchase();
            completedPurchaseEntity.setId(this.completedPurchaseId);
            entity.setCompletedPurchase(completedPurchaseEntity);
        }

        return entity;
    }
}