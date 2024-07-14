package hu.torma.deliveryapplication.DTO;

import lombok.Data;

@Data
public class RecordWithMinimalsDTO {
    private Integer id;
    private int one;
    private int two;
    private int three;
    private int four;
    private int five;
    private int six;
    private int price;
    private PurchaseSelectorMinimalDTO purchase;
    private CompletedPurchaseIdOnlyDTO completedPurchase;
}
