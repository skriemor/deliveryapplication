package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.StorageSnapshotDTO;

import java.util.List;


public interface SnapshotService {
    List<StorageSnapshotDTO> getAllSnapshots();


    boolean saveSnapshot(StorageSnapshotDTO dto);

    boolean deleteSnapshot(StorageSnapshotDTO dto);
}
