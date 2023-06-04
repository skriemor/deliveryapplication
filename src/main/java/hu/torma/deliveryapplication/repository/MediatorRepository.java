package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Mediator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediatorRepository extends JpaRepository<Mediator, String> {
}