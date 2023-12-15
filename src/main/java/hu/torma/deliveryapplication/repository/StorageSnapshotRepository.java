package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.StorageSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageSnapshotRepository extends JpaRepository<StorageSnapshot, Long> {

}