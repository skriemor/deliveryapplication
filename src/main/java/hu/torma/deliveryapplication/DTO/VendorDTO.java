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

    private Boolean gender;

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

    private String representative;

    private String fileNumber;

    private String felir;

    private String registryNumber;

    private Boolean pensioner;

    private String szja;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendorDTO vendorDTO = (VendorDTO) o;
        return Objects.equals(taxId, vendorDTO.taxId) && Objects.equals(taxNumber, vendorDTO.taxNumber) && Objects.equals(vendorName, vendorDTO.vendorName) && Objects.equals(birthName, vendorDTO.birthName) && Objects.equals(gender, vendorDTO.gender) && Objects.equals(nameOfMother, vendorDTO.nameOfMother) && Objects.equals(birthPlace, vendorDTO.birthPlace) && Objects.equals(birthDate, vendorDTO.birthDate) && Objects.equals(accountNumber, vendorDTO.accountNumber) && Objects.equals(accountManager, vendorDTO.accountManager) && Objects.equals(city, vendorDTO.city) && Objects.equals(postalCode, vendorDTO.postalCode) && Objects.equals(address, vendorDTO.address) && Objects.equals(qualification, vendorDTO.qualification) && Objects.equals(countryCode, vendorDTO.countryCode) && Objects.equals(taj, vendorDTO.taj) && Objects.equals(activity, vendorDTO.activity) && Objects.equals(representative, vendorDTO.representative) && Objects.equals(fileNumber, vendorDTO.fileNumber) && Objects.equals(felir, vendorDTO.felir) && Objects.equals(registryNumber, vendorDTO.registryNumber) && Objects.equals(pensioner, vendorDTO.pensioner) && Objects.equals(szja, vendorDTO.szja);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxId, taxNumber, vendorName, birthName, gender, nameOfMother, birthPlace, birthDate, accountNumber, accountManager, city, postalCode, address, qualification, countryCode, taj, activity, representative, fileNumber, felir, registryNumber, pensioner, szja);
    }
}
