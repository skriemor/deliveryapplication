package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.CompletionRecordDTO;

import java.util.List;

public interface CompletionRecordService {
    List<CompletionRecordDTO> getAllCompletionRecords();

    CompletionRecordDTO getCompletionRecord(CompletionRecordDTO CompletionRecordDTO);

    CompletionRecordDTO saveCompletionRecord(CompletionRecordDTO CompletionRecordDTO);

    void deleteCompletionRecord(CompletionRecordDTO CompletionRecordDTO);
}