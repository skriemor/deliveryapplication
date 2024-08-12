package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import lombok.Data;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "first_unit_id", referencedColumnName = "unit_id", nullable = false)
    private Unit firstUnit;

    @ManyToOne
    @JoinColumn(name = "second_unit_id", referencedColumnName = "unit_id", nullable = false)
    private Unit secondUnit;

    @Column(name = "price")
    private Integer price;

    @Column(name = "compensational")
    private Integer compPercent;

    @Column(name = "tariffnumber")
    private String tariffnum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return compPercent == product.compPercent && Objects.equals(id, product.id) && Objects.equals(firstUnit, product.firstUnit) && Objects.equals(secondUnit, product.secondUnit) && Objects.equals(price, product.price) && Objects.equals(tariffnum, product.tariffnum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstUnit, secondUnit, price, compPercent, tariffnum);
    }

    public ProductDTO toDTO() {
        ProductDTO dto = new ProductDTO();
        dto.setId(this.id);
        dto.setPrice(this.price);
        dto.setCompPercent(this.compPercent);
        dto.setTariffnum(this.tariffnum);

        if (Hibernate.isInitialized(this.firstUnit) && !(this.firstUnit instanceof HibernateProxy)) {
            dto.setFirstUnit(this.firstUnit.toDTO());
        }

        if (Hibernate.isInitialized(this.secondUnit) && !(this.secondUnit instanceof HibernateProxy)) {
            dto.setSecondUnit(this.secondUnit.toDTO());
        }

        return dto;
    }
}