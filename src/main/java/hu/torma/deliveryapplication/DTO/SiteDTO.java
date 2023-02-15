package hu.torma.deliveryapplication.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Objects;

@Data
@EqualsAndHashCode
public class SiteDTO implements Serializable {

    private Long id;

    private String siteName;


}
