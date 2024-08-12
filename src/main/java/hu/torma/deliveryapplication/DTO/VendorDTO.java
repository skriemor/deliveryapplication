package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Vendor;
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

    public Vendor toEntity() {
        Vendor entity = new Vendor();
        entity.setTaxId(this.taxId);
        entity.setTaxNumber(this.taxNumber);
        entity.setVendorName(this.vendorName);
        entity.setBirthName(this.birthName);
        entity.setNameOfMother(this.nameOfMother);
        entity.setBirthPlace(this.birthPlace);
        entity.setBirthDate(this.birthDate);
        entity.setAccountNumber(this.accountNumber);
        entity.setCity(this.city);
        entity.setPostalCode(this.postalCode);
        entity.setAddress(this.address);
        entity.setQualification(this.qualification);
        entity.setTaj(this.taj);
        entity.setActivity(this.activity);
        entity.setFileNumber(this.fileNumber);
        entity.setFelir(this.felir);
        entity.setGgn(this.ggn);
        entity.setPhone(this.phone);
        entity.setContract(this.contract);
        if (this.mediator != null) {
            entity.setMediator(this.mediator.toEntity());
        }
        return entity;
    }

}
