package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, String> {
}