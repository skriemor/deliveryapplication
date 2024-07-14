package hu.torma.deliveryapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseForDateComparisonDTO {
    private Integer id;
    private Date receiptDate;
}
