package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.UnitDTO;
import hu.torma.deliveryapplication.entity.Unit;
import hu.torma.deliveryapplication.repository.UnitRepository;
import hu.torma.deliveryapplication.service.UnitService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UnitServiceImpl implements UnitService {
    @Autowired
    UnitRepository repo;
    ModelMapper mapper = new ModelMapper();

    @Override
    public List<UnitDTO> getAllUnits() {
        return new ArrayList<UnitDTO>(
                repo.findAll().stream().map(
                        c -> mapper.map(c, UnitDTO.class)
                ).toList()
        );
    }

    @Override
    public UnitDTO getUnit(UnitDTO UnitDTO) {
        return mapper.map(repo.findById(UnitDTO.getId()), UnitDTO.class);
    }

    @Override
    @Transactional
    public UnitDTO saveUnit(UnitDTO UnitDTO) {
        return mapper.map(repo.save(mapper.map(UnitDTO, Unit.class)), UnitDTO.class);
    }

    @Override
    @Transactional
    public void deleteUnit(UnitDTO UnitDTO) {
        repo.deleteById(UnitDTO.getId());
    }

    @Override
    public UnitDTO getUnitByName(String s) {
        return mapper.map(repo.findById(s), UnitDTO.class);
    }

    @Override
    public UnitDTO getUnitById(String s) {
        return mapper.map(repo.findById(s).get(), UnitDTO.class);
    }
}
