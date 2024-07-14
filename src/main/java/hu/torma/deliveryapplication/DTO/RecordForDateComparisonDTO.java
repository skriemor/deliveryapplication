package hu.torma.deliveryapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordForDateComparisonDTO {
    private Integer id;
    private PurchaseForDateComparisonDTO purchase;
}
