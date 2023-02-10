package hu.torma.deliveryapplication.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class UnitDTO implements Serializable {
    private String id;

    private String unitName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitDTO unitDTO = (UnitDTO) o;
        return Objects.equals(id, unitDTO.id) && Objects.equals(unitName, unitDTO.unitName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unitName);
    }
}
