package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Unit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
public class ProductDTO implements Serializable {
    private String id;

    private UnitDTO firstUnit;

    private UnitDTO secondUnit;

    private Double price;

    private Double compPercent;

    private String tariffnum;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return compPercent == that.compPercent && Objects.equals(id, that.id) && Objects.equals(firstUnit, that.firstUnit) && Objects.equals(secondUnit, that.secondUnit) && Objects.equals(price, that.price) && Objects.equals(tariffnum, that.tariffnum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstUnit, secondUnit, price, compPercent, tariffnum);
    }
}
