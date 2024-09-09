package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.SiteDTO;
import hu.torma.deliveryapplication.entity.Site;
import hu.torma.deliveryapplication.repository.SiteRepository;
import hu.torma.deliveryapplication.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    SiteRepository repo;

    @Override
    public List<SiteDTO> getAllSites() {
        return new ArrayList<SiteDTO>(
                repo.findAll().stream().map(
                       Site::toDTO
                ).toList()
        );
    }

    @Override
    public List<Site> getAllEntities() {
        return repo.findAll();
    }

    @Override
    public SiteDTO getSite(SiteDTO SiteDTO) {
        return repo.findById(SiteDTO.getId()).map(Site::toDTO).orElse(null);
    }

    @Override
    @Transactional
    public SiteDTO saveSite(SiteDTO siteDTO) {
        return repo.save(siteDTO.toEntity()).toDTO();
    }

    @Override
    @Transactional
    public void deleteSite(SiteDTO SiteDTO) {
        repo.deleteById(SiteDTO.getId());
    }

    @Override
    public SiteDTO getSiteById(String s) {
        if (repo.findSiteBySiteName(s)==null) return null;
        return repo.findSiteBySiteName(s).toDTO();
    }
}
