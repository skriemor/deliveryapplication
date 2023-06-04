package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Mediator;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link Mediator} entity
 */
@Data
public class MediatorDTO implements Serializable {
    String id;
    List<VendorDTO> buyers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediatorDTO that = (MediatorDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}