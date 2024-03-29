package hu.torma.deliveryapplication.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
@EqualsAndHashCode
@ToString
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


    public String getFormattedBirthDate() {
        if (birthDate==null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+01"));
        return sdf.format(this.birthDate);
    }
    @Override
    public String toString() {
        return "VendorDTO{" +
                "taxId='" + taxId + '\'' +
                ", vendorName='" + vendorName + '\'' +
                '}';
    }
}
