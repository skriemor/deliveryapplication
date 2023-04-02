package hu.torma.deliveryapplication.service;

import hu.torma.deliveryapplication.DTO.BuyerDTO;

import java.util.List;

public interface BuyerService {
    List<BuyerDTO> getAllBuyers();

    BuyerDTO getBuyer(BuyerDTO vendorDTO);

    BuyerDTO saveBuyer(BuyerDTO vendorDTO);

    void deleteBuyer(BuyerDTO vendorDTO);

    BuyerDTO getBuyerById(String s);
}
