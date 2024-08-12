package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.StorageSnapshotDTO;
import hu.torma.deliveryapplication.entity.StorageSnapshot;
import hu.torma.deliveryapplication.repository.StorageSnapshotRepository;
import hu.torma.deliveryapplication.service.SnapshotService;
import org.modelmapper.ModelMapper;
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
        return repo.findAll().stream().map(snapshot ->
                mapper.map(snapshot, StorageSnapshotDTO.class)
        ).toList();
    }



    @Transactional
    @Override
    public boolean saveSnapshot(StorageSnapshotDTO dto1) {
        var f = mapper.map(dto1,StorageSnapshot.class);
        repo.save(f);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteSnapshot(StorageSnapshotDTO dto1) {
        var f = mapper.map(dto1,StorageSnapshot.class);
        repo.delete(f);
        return true;
    }
}
