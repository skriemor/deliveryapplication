package hu.torma.deliveryapplication.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class VendorDTO implements Serializable {
    private String taxId;
    private String taxNumber;
    private String vendorName;
    private String birthName;
    private String nameOfMother;
    private String birthPlace;
    private Date birthDate;
    private String accountNumber;
    private String city;
    private String postalCode;
    private String address;
    private String qualification;
    private String countryCode;
    private String taj;
    private String activity;
    private String fileNumber;
    private String felir;
    private String ggn;
    private String phone;
    private String contract;
    private MediatorDTO mediator;


    @Override
    public String toString() {
        return "VendorDTO{" +
                "taxId='" + taxId + '\'' +
                ", vendorName='" + vendorName + '\'' +
                '}';
    }
}
