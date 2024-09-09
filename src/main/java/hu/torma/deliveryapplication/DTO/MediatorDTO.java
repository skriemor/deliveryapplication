package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Mediator;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public Mediator toEntity(boolean includeBuyers) {
        Mediator entity = new Mediator();
        entity.setId(this.id);

        if (includeBuyers && this.buyers != null) {
            entity.setBuyers(this.buyers.stream()
                    .map(buyer -> buyer.toEntity(false)) // Avoid recursion in Vendor
                    .collect(Collectors.toList()));
        }

        return entity;
    }
}