package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.DTO.SiteDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "site")
@EqualsAndHashCode
@ToString
public class Site {
    @Id
    @Column(name = "site_id", nullable = false)
    private Long id;

    @Column(name = "site_name", nullable = false)
    private String siteName;

    public SiteDTO toDTO() {
        SiteDTO dto = new SiteDTO();
        dto.setId(this.id);
        dto.setSiteName(this.siteName);
        return dto;
    }
}