package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {
}