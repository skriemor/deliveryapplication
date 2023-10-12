package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.Mediator;
import hu.torma.deliveryapplication.primefaces.sumutils.MediatorData;
import hu.torma.deliveryapplication.primefaces.sumutils.SaleSumPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MediatorRepository extends JpaRepository<Mediator, String> {
    @Query(name = "get_mediator_data", nativeQuery = true)
    List<MediatorData> getMediatorData(
            @Param("date1") Date date1,
            @Param("date2") Date date2
    );
}