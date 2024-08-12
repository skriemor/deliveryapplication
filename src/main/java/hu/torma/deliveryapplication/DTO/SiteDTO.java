package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.entity.Site;
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

    public Site toEntity() {
        Site entity = new Site();
        entity.setId(this.id);
        entity.setSiteName(this.siteName);
        return entity;
    }

}
