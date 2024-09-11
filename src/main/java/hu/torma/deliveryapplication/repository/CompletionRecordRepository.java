package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.DTO.MediatorDisplay;
import hu.torma.deliveryapplication.entity.CompletionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompletionRecordRepository extends JpaRepository<CompletionRecord, Integer> {

    List<CompletionRecord> findAllByPurchaseId(Integer l);

    @Query(value = "SELECT r FROM CompletionRecord r WHERE r.purchase.id = ?1 AND r.completedPurchase.id <> ?2")
    List<CompletionRecord> findAllByPurchaseIdExclusive(Integer id1, Integer id2);

    @Modifying
    @Query(value = "UPDATE purchase p  SET p.remaining_price = (SELECT SUM(r.price) FROM records r WHERE r.purchase_id = ?1) WHERE p.id = ?1", nativeQuery = true)
    Integer updateRemainingPriceById(Integer id);

    boolean existsByPurchaseId(Integer id);
}