package hu.torma.deliveryapplication.primefaces.controller;

import hu.torma.deliveryapplication.DTO.DisplayUnit;
import hu.torma.deliveryapplication.entity.PurchasedProduct;
import hu.torma.deliveryapplication.primefaces.sumutils.SaleSumPojo;
import hu.torma.deliveryapplication.service.StorageService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

@SessionScope
@Getter
@Setter
@Controller("storageController")
public class StorageController {

    Date filterDateFrom, filterDateTo;
    @Autowired
    StorageService service;
    List<DisplayUnit> storageList;
    List<DisplayUnit> storageListDates;
    ArrayList<Statement> statementsDates;
    SaleSumPojo saleSumPojoDates;

    ArrayList<Statement> statements;
    SaleSumPojo saleSumPojo;
    @PostConstruct
    public void init() {
        getStorageList();
    }
    public ArrayList<Statement> getStorageListWithDates(){
        storageListDates = service.getDisplayUnitsWithDates(filterDateFrom,filterDateTo);
        saleSumPojoDates = service.getFictionalStorageAsObjectDates(filterDateFrom,filterDateTo);
        statementsDates = new ArrayList<>();
        statementsDates.addAll(Arrays.asList(
                new Statement(storageListDates.get(0).getProductName(),storageListDates.get(0).getQuantity(),saleSumPojoDates.getOne()),
                new Statement(storageListDates.get(1).getProductName(),storageListDates.get(1).getQuantity(),saleSumPojoDates.getTwo()),
                new Statement(storageListDates.get(2).getProductName(),storageListDates.get(2).getQuantity(),saleSumPojoDates.getThree()),
                new Statement(storageListDates.get(3).getProductName(),storageListDates.get(3).getQuantity(),saleSumPojoDates.getFour()),
                new Statement(storageListDates.get(4).getProductName(),storageListDates.get(4).getQuantity(),saleSumPojoDates.getFive()),
                new Statement(storageListDates.get(5).getProductName(),storageListDates.get(5).getQuantity(),saleSumPojoDates.getSix())
        ));
        return statementsDates;
    }
    public ArrayList<Statement> getStorageList(){
        storageList = service.getDisplayUnits();
        saleSumPojo = service.getFictionalStorageAsObject();
        statements = new ArrayList<>();
        statements.addAll(Arrays.asList(
                new Statement(storageList.get(0).getProductName(),storageList.get(0).getQuantity(),saleSumPojo.getOne()),
                new Statement(storageList.get(1).getProductName(),storageList.get(1).getQuantity(),saleSumPojo.getTwo()),
                new Statement(storageList.get(2).getProductName(),storageList.get(2).getQuantity(),saleSumPojo.getThree()),
                new Statement(storageList.get(3).getProductName(),storageList.get(3).getQuantity(),saleSumPojo.getFour()),
                new Statement(storageList.get(4).getProductName(),storageList.get(4).getQuantity(),saleSumPojo.getFive()),
                new Statement(storageList.get(5).getProductName(),storageList.get(5).getQuantity(),saleSumPojo.getSix())
        ));
        return statements;
    }


    public String toDottedDate(java.util.Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+01"));

        return dt==null?"0000.01.01":sdf.format(dt);
    }
    public StorageService getService() {
        return service;
    }

    @Getter
    public class Statement {
        String product;
        Integer quantit1, quantity2;

        public Integer getQuantit1() {
            return quantit1==null?0:quantit1;
        }

        public Integer getQuantity2() {
            return quantity2==null?0:quantity2;
        }

        public Statement(String product, Integer quantit1, Integer quantity2) {
            this.product = product==null?"":product;
            this.quantit1 = quantit1==null?0:quantit1;
            this.quantity2 = quantity2==null?0:quantity2;
        }
    }
}
