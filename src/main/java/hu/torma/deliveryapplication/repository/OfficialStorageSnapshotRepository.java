package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.OfficialStorageSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OfficialStorageSnapshotRepository extends JpaRepository<OfficialStorageSnapshot, Long> {
    @Query(nativeQuery = true, value = """
        SELECT o.*
        FROM official_storage_snapshot o
        LIMIT 1
            """)
    Optional<OfficialStorageSnapshot> findFirst();
}