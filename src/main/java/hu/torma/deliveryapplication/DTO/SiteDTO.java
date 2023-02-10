package hu.torma.deliveryapplication.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class SiteDTO implements Serializable {

    private Long id;

    private String siteName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteDTO siteDTO = (SiteDTO) o;
        return Objects.equals(id, siteDTO.id) && Objects.equals(siteName, siteDTO.siteName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, siteName);
    }
}
