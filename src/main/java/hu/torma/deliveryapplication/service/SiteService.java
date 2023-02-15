package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.SiteDTO;

import java.util.List;

public interface SiteService {
    List<SiteDTO> getAllSites();

    SiteDTO getSite(SiteDTO SiteDTO);

    SiteDTO saveSite(SiteDTO SiteDTO);

    void deleteSite(SiteDTO SiteDTO);

    SiteDTO getSiteById(String s);
}
