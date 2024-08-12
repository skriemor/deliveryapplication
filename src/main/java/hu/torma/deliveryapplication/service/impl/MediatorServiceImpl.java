package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.MediatorDTO;
import hu.torma.deliveryapplication.entity.Mediator;
import hu.torma.deliveryapplication.primefaces.sumutils.MediatorData;
import hu.torma.deliveryapplication.repository.MediatorRepository;
import hu.torma.deliveryapplication.service.MediatorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MediatorServiceImpl implements MediatorService {
    @Autowired
    MediatorRepository repo;
    @Override
    public List<MediatorDTO> getAllMediators() {
        return new ArrayList<MediatorDTO>(
                repo.findAll().stream().map(
                        c -> mapper.map(c, MediatorDTO.class)
                ).toList()
        );
    }

    @Override
    public MediatorDTO getMediator(MediatorDTO mediatorDTO) {
        return mapper.map(repo.findById(mediatorDTO.getId()), MediatorDTO.class);
    }

    @Override
    @Transactional
    public MediatorDTO saveMediator(MediatorDTO mediatorDTO) {
        return mapper.map(repo.save(mapper.map(mediatorDTO, Mediator.class)), MediatorDTO.class);
    }

    @Override
    @Transactional
    public void deleteMediator(MediatorDTO mediatorDTO) {
        repo.deleteById(mediatorDTO.getId());
    }

    @Override
    public MediatorDTO getMediatorById(String s) {
        return mapper.map(repo.findById(s), MediatorDTO.class);
    }

    @Override
    public List<MediatorData> getMediatorData(Date date1, Date date2) {
        return repo.getMediatorData(date1, date2);
    }
}
