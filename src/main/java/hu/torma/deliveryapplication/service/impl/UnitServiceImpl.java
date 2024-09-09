package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.UnitDTO;
import hu.torma.deliveryapplication.entity.Unit;
import hu.torma.deliveryapplication.repository.UnitRepository;
import hu.torma.deliveryapplication.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UnitServiceImpl implements UnitService {
    @Autowired
    UnitRepository repo;

    @Override
    public List<UnitDTO> getAllUnits() {
        return new ArrayList<UnitDTO>(
                repo.findAll().stream().map(
                        Unit::toDTO
                ).toList()
        );
    }

    @Override
    public UnitDTO getUnit(UnitDTO dto) {
        return repo.findById(dto.getId()).map(Unit::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public UnitDTO saveUnit(UnitDTO dto) {
        return repo.save(dto.toEntity()).toDTO();
    }

    @Override
    @Transactional
    public void deleteUnit(UnitDTO UnitDTO) {
        repo.deleteById(UnitDTO.getId());
    }

    @Override
    public UnitDTO getUnitByName(String s) {
        return repo.findById(s).map(Unit::toDTO).orElse(null);
    }

    @Override
    public UnitDTO getUnitById(String s) {
        return repo.findById(s).map(Unit::toDTO).orElse(null);
    }

    @Override
    public boolean existsById(String id) {
        return repo.existsById(id);
    }
}
