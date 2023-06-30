package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.CompletionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompletionRecordRepository extends JpaRepository<CompletionRecord, Integer> {

    List<CompletionRecord> findAllByPurchaseId(Integer l);
}