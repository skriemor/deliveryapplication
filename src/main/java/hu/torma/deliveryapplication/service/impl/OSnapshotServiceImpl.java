package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.OfficialStorageSnapshotDTO;
import hu.torma.deliveryapplication.entity.OfficialStorageSnapshot;
import hu.torma.deliveryapplication.repository.OfficialStorageSnapshotRepository;
import hu.torma.deliveryapplication.service.OSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class OSnapshotServiceImpl implements OSnapshotService {
    @Autowired
    OfficialStorageSnapshotRepository repo;

    @Override
    public List<OfficialStorageSnapshotDTO> getAllSnapshots() {
        return repo.findAll().stream().map(
                snap -> snap.toDTO(true)
        ).toList();
    }


    @Transactional
    @Override
    public boolean saveSnapshot(OfficialStorageSnapshotDTO dto1) {
        repo.save(dto1.toEntity(true));
        return true;
    }

    @Transactional
    @Override
    public boolean deleteSnapshot(OfficialStorageSnapshotDTO dto1) {
        repo.delete(dto1.toEntity(true));
        return true;
    }

    @Override
    public OfficialStorageSnapshotDTO getSnapshotById(Long id) {
        return repo.findById(id).map(snap -> snap.toDTO(true)).orElse(null);
    }

    @Override
    public OfficialStorageSnapshotDTO getAnySnapshot() {
        return repo.findFirst().orElseGet(OfficialStorageSnapshot::new).toDTO(true);
    }
}
