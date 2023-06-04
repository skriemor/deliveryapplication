package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.MediatorDTO;

import java.util.List;

public interface MediatorService {
    List<MediatorDTO> getAllMediators();

    MediatorDTO getMediator(MediatorDTO vendorDTO);

    MediatorDTO saveMediator(MediatorDTO vendorDTO);

    void deleteMediator(MediatorDTO vendorDTO);

    MediatorDTO getMediatorById(String s);
}
