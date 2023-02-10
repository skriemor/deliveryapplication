package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.UnitDTO;

import java.util.List;

public interface UnitService {
    List<UnitDTO> getAllUnits();

    UnitDTO getUnit(UnitDTO UnitDTO);

    UnitDTO saveUnit(UnitDTO UnitDTO);

    void deleteUnit(UnitDTO UnitDTO);

    Object getUnitByName(String s);
}
