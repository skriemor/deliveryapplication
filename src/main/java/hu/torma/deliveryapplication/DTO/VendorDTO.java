package hu.torma.deliveryapplication.DTO;

import lombok.*;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
public class VendorDTO implements Serializable {
    private String taxId;

    private String taxNumber;

    private String vendorName;

    private String birthName;

    private String gender;

    private String nameOfMother;

    private String birthPlace;

    private Date birthDate;

    private String accountNumber;

    private String accountManager;

    private String city;

    private String postalCode;

    private String address;

    private String qualification;

    private String countryCode;

    private String taj;

    private String activity;

    private String fileNumber;

    private String felir;


}
