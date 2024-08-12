package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.DTO.UnitDTO;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "unit")
public class Unit  {
    @Id
    @Column(name = "unit_id", nullable = false)
    private String id;

    @Column(name = "unit_name")
    private String unitName;


    public UnitDTO toDTO() {
        UnitDTO dto = new UnitDTO();
        dto.setId(this.id);
        dto.setUnitName(this.unitName);
        return dto;
    }
}