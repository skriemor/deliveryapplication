package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.DisplayUnit;
import hu.torma.deliveryapplication.service.StorageService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SessionScope
@Getter
@Setter
@Controller("storageController")
public class StorageController {

    @Autowired
    StorageService service;
    ArrayList<DisplayUnit> storageList;

    @PostConstruct
    public void init() {

    }
    public ArrayList<DisplayUnit> getStorageList(){
        storageList = service.getDisplayUnits();
        return new ArrayList<>(storageList);
    }


    public String toDottedDate(java.util.Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+01"));

        return dt==null?"0000.01.01":sdf.format(dt);
    }
    public StorageService getService() {
        return service;
    }
}
