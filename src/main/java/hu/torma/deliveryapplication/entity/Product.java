package hu.torma.deliveryapplication.entity;

import lombok.Data;

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
    private Long price;

    @Column(name = "compensational")
    private long compPercent;

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
}