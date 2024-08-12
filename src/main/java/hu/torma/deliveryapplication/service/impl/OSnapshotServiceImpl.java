package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.OfficialStorageSnapshotDTO;
import hu.torma.deliveryapplication.entity.OfficialStorageSnapshot;
import hu.torma.deliveryapplication.repository.OfficialStorageSnapshotRepository;
import hu.torma.deliveryapplication.service.OSnapshotService;
import org.modelmapper.ModelMapper;
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
        return repo.findAll().stream().map(snapshot ->
                mapper.map(snapshot, OfficialStorageSnapshotDTO.class)
        ).toList();
    }


    @Transactional
    @Override
    public boolean saveSnapshot(OfficialStorageSnapshotDTO dto1) {
        var f = mapper.map(dto1, OfficialStorageSnapshot.class);
        repo.save(f);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteSnapshot(OfficialStorageSnapshotDTO dto1) {
        var f = mapper.map(dto1, OfficialStorageSnapshot.class);
        repo.delete(f);
        return true;
    }

    @Override
    public OfficialStorageSnapshotDTO getSnapshotById(Long id) {
        return mapper.map(repo.findById(id), OfficialStorageSnapshotDTO.class);
    }

    @Override
    public OfficialStorageSnapshotDTO getAnySnapshot() {
        return mapper.map(repo.findFirst().orElse(new OfficialStorageSnapshot()), OfficialStorageSnapshotDTO.class);
    }
}
