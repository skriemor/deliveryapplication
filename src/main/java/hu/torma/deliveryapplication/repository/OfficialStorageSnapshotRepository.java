package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.OfficialStorageSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OfficialStorageSnapshotRepository extends JpaRepository<OfficialStorageSnapshot, Long> {
    @Query(nativeQuery = true, value = """
        SELECT o.*
        FROM official_storage_snapshot o
        LIMIT 1
            """)
    Optional<OfficialStorageSnapshot> findFirst();

    @Query(value = "SELECT oss from OfficialStorageSnapshot oss left join fetch oss.previous ")
    List<OfficialStorageSnapshot> findAllFetchAll();

    @Query(value = "select os from OfficialStorageSnapshot  os left join fetch os.previous where os.id = :id")
    Optional<OfficialStorageSnapshot> findByIdFetchAll(@Param("id") final long id);
}