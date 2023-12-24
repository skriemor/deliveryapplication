package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.OfficialStorageSnapshotDTO;

import java.util.List;


public interface OSnapshotService {
    List<OfficialStorageSnapshotDTO> getAllSnapshots();


    boolean saveSnapshot(OfficialStorageSnapshotDTO dto);

    boolean deleteSnapshot(OfficialStorageSnapshotDTO dto);

    OfficialStorageSnapshotDTO getSnapshotById(Long id);

    OfficialStorageSnapshotDTO getAnySnapshot();
}
