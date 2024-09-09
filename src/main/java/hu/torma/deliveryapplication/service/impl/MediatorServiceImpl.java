package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.MediatorDTO;
import hu.torma.deliveryapplication.primefaces.sumutils.MediatorData;
import hu.torma.deliveryapplication.repository.MediatorRepository;
import hu.torma.deliveryapplication.service.MediatorService;
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
                    med -> med.toDTO(true)
                ).toList()
        );
    }

    @Override
    public MediatorDTO getMediator(MediatorDTO mediatorDTO) {
        return repo.findById(mediatorDTO.getId()).map(med -> med.toDTO(true)).orElse(null);
    }

    @Override
    @Transactional
    public MediatorDTO saveMediator(MediatorDTO mediatorDTO) {
        return repo.save(mediatorDTO.toEntity(true)).toDTO(true);
    }

    @Override
    @Transactional
    public void deleteMediator(MediatorDTO mediatorDTO) {
        repo.deleteById(mediatorDTO.getId());
    }

    @Override
    public MediatorDTO getMediatorById(String s) {
        return repo.findById(s).map(med -> med.toDTO(true)).orElse(null);
    }

    @Override
    public List<MediatorData> getMediatorData(Date date1, Date date2) {
        return repo.getMediatorData(date1, date2);
    }
}
