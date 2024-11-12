package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.OfficialStorageSnapshotDTO;
import hu.torma.deliveryapplication.entity.OfficialStorageSnapshot;

import java.util.List;
import java.util.Optional;


public interface OSnapshotService {
    List<OfficialStorageSnapshotDTO> getAllSnapshots();

    OfficialStorageSnapshot saveSnapshot(OfficialStorageSnapshotDTO dto);

    boolean deleteSnapshot(OfficialStorageSnapshotDTO dto);

    OfficialStorageSnapshotDTO getSnapshotById(Long id);

    OfficialStorageSnapshotDTO getAnySnapshot();

    Optional<OfficialStorageSnapshotDTO> getById(long id);
}
