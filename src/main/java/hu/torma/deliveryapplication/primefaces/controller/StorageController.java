package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.QuantityDTO;
import hu.torma.deliveryapplication.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.util.List;

@SessionScope
@Controller("storageController")
public class StorageController {

    @Autowired
    StorageService service;
    private List<QuantityDTO> dtoList;

    @PostConstruct
    public void init() {
        getAllQuantities();
    }

    @PostConstruct
    public void getAllQuantities() {
        this.dtoList = service.getAllQuantities();
    }


    public void setDtoList(List<QuantityDTO> dtoList) {
        this.dtoList = dtoList;
    }


    public List<QuantityDTO> getDtoList() {
        return dtoList;
    }

    public StorageService getService() {
        return service;
    }
}
