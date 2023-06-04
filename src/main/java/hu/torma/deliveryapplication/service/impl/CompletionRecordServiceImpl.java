package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.CompletionRecordDTO;
import hu.torma.deliveryapplication.entity.CompletionRecord;
import hu.torma.deliveryapplication.repository.CompletionRecordRepository;
import hu.torma.deliveryapplication.service.CompletionRecordService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompletionRecordServiceImpl implements CompletionRecordService {
    @Autowired
    private CompletionRecordRepository repo;
    ModelMapper mapper = new ModelMapper();

    @Override
    public List<CompletionRecordDTO> getAllCompletionRecords() {
        return new ArrayList<CompletionRecordDTO>(
                repo.findAll().stream().map(
                        c -> mapper.map(c, CompletionRecordDTO.class)
                ).toList()
        );
    }

    @Override
    public CompletionRecordDTO getCompletionRecord(CompletionRecordDTO CompletionRecordDTO) {
        return mapper.map(repo.findById(CompletionRecordDTO.getId()), CompletionRecordDTO.class);
    }

    @Override
    @Transactional
    public CompletionRecordDTO saveCompletionRecord(CompletionRecordDTO CompletionRecordDTO) {
        return mapper.map(repo.save(mapper.map(CompletionRecordDTO, CompletionRecord.class)), CompletionRecordDTO.class);
    }

    @Override
    @Transactional
    public void deleteCompletionRecord(CompletionRecordDTO CompletionRecordDTO) {
        repo.deleteById(CompletionRecordDTO.getId());
    }

}
