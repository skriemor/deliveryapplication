package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.SiteDTO;
import hu.torma.deliveryapplication.entity.Site;

import java.util.List;

public interface SiteService {
    List<SiteDTO> getAllSites();

    List<Site> getAllEntities();

    SiteDTO getSite(SiteDTO SiteDTO);

    SiteDTO saveSite(SiteDTO SiteDTO);

    void deleteSite(SiteDTO SiteDTO);

    SiteDTO getSiteById(String s);
}
