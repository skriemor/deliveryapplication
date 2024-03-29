package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.MediatorDTO;
import hu.torma.deliveryapplication.primefaces.sumutils.MediatorData;

import java.util.Date;
import java.util.List;

public interface MediatorService {
    List<MediatorDTO> getAllMediators();

    MediatorDTO getMediator(MediatorDTO vendorDTO);

    MediatorDTO saveMediator(MediatorDTO vendorDTO);

    void deleteMediator(MediatorDTO vendorDTO);

    MediatorDTO getMediatorById(String s);

    List<MediatorData> getMediatorData(Date date1, Date date2);
}
