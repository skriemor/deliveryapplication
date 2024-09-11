package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.StorageSnapshotDTO;
import hu.torma.deliveryapplication.entity.StorageSnapshot;
import hu.torma.deliveryapplication.repository.StorageSnapshotRepository;
import hu.torma.deliveryapplication.service.SnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class SnapshotServiceImpl implements SnapshotService {
    @Autowired
    StorageSnapshotRepository repo;

    @Override
    public List<StorageSnapshotDTO> getAllSnapshots() {
        return repo.findAll().stream().map(
                StorageSnapshot::toDTO
        ).toList();
    }


    @Transactional
    @Override
    public boolean saveSnapshot(StorageSnapshotDTO dto1) {
        repo.save(dto1.toEntity());
        return true;
    }

    @Transactional
    @Override
    public boolean deleteSnapshot(StorageSnapshotDTO dto1) {
        repo.delete(dto1.toEntity());
        return true;
    }
}
