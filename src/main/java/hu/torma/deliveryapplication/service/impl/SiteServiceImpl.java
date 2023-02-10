package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.SiteDTO;
import hu.torma.deliveryapplication.entity.Site;
import hu.torma.deliveryapplication.repository.SiteRepository;
import hu.torma.deliveryapplication.service.SiteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    SiteRepository repo;
    ModelMapper mapper = new ModelMapper();

    @Override
    public List<SiteDTO> getAllSites() {
        return new ArrayList<SiteDTO>(
                repo.findAll().stream().map(
                        c -> mapper.map(c, SiteDTO.class)
                ).toList()
        );
    }

    @Override
    public SiteDTO getSite(SiteDTO SiteDTO) {
        return mapper.map(repo.findById(SiteDTO.getId()), SiteDTO.class);
    }

    @Override
    @Transactional
    public SiteDTO saveSite(SiteDTO SiteDTO) {
        return mapper.map(repo.save(mapper.map(SiteDTO, Site.class)), SiteDTO.class);
    }

    @Override
    @Transactional
    public void deleteSite(SiteDTO SiteDTO) {
        repo.deleteById(SiteDTO.getId());
    }
}
