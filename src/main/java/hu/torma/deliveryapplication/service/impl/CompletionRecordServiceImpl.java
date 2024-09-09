package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.CompletionRecordDTO;
import hu.torma.deliveryapplication.repository.CompletionRecordRepository;
import hu.torma.deliveryapplication.service.CompletionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompletionRecordServiceImpl implements CompletionRecordService {
    @Autowired
    private CompletionRecordRepository repo;
    @Override
    public List<CompletionRecordDTO> getAllCompletionRecords() {
        return new ArrayList<>(
                repo.findAll().stream().map(
                        cp -> cp.toDTO(true, true)
                ).toList()
        );
    }

    @Override
    public CompletionRecordDTO getCompletionRecord(CompletionRecordDTO CompletionRecordDTO) {
        return repo.findById(CompletionRecordDTO.getId()).map(cp -> cp.toDTO(true, true)).orElse(null);
    }

    @Override
    @Transactional
    public CompletionRecordDTO saveCompletionRecord(CompletionRecordDTO recordDto) {
        return repo.save(recordDto.toEntity(true, true)).toDTO(true, true);
    }

    @Override
    @Transactional
    public void deleteCompletionRecord(CompletionRecordDTO CompletionRecordDTO) {
        repo.deleteById(CompletionRecordDTO.getId());
    }

    @Override
    @Transactional
    public List<CompletionRecordDTO> findAllByPurchaseId(Integer idd) {
        return repo.findAllByPurchaseId(idd).stream().map(cp -> cp.toDTO(true, true)).collect(Collectors.toList());
    }

    @Override
    public List<CompletionRecordDTO> findAllByPurchaseIdExclusive(Integer id, Integer id2) {
        return repo.findAllByPurchaseIdExclusive(id,id2).stream().map(cp -> cp.toDTO(true, true)).collect(Collectors.toList());
    }


    @Override
    public boolean existsByPurchaseId(Integer id) {
        return repo.existsByPurchaseId(id);
    }


    @Override
    public void updateRemainingPriceById(Integer id) {
        repo.updateRemainingPriceById(id);
    }

}
