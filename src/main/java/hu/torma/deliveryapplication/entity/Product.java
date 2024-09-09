package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_id", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_unit_id", referencedColumnName = "unit_id", nullable = false)
    private Unit firstUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_unit_id", referencedColumnName = "unit_id", nullable = false)
    private Unit secondUnit;

    @Column(name = "price")
    private Integer price;

    @Column(name = "compensational")
    private Integer compPercent;

    @Column(name = "tariffnumber")
    private String tariffnum;

    public ProductDTO toDTO(boolean includeUnits) {
        ProductDTO dto = new ProductDTO();
        dto.setId(this.id);
        dto.setPrice(this.price);
        dto.setCompPercent(this.compPercent);
        dto.setTariffnum(this.tariffnum);

        if (includeUnits && Hibernate.isInitialized(this.firstUnit) && !(this.firstUnit instanceof HibernateProxy) && this.firstUnit != null) {
            dto.setFirstUnit(this.firstUnit.toDTO()); // Pass false to avoid looping, if necessary
        }

        if (includeUnits && Hibernate.isInitialized(this.secondUnit) && !(this.secondUnit instanceof HibernateProxy) && this.secondUnit != null) {
            dto.setSecondUnit(this.secondUnit.toDTO()); // Pass false to avoid looping, if necessary
        }

        return dto;
    }


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}