package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Purchase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode
@ToString
public class SiteDTO implements Serializable {
    private Long id;
    private String siteName;
}
