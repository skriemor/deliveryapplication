package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.CompletionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletionRecordRepository extends JpaRepository<CompletionRecord, Long> {
}