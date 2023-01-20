package hu.torma.deliveryapplication.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Data
@Table(name = "vendor")
public class Vendor implements Serializable {
    @Id
    @Column(name = "tax_id", nullable = false)
    private String taxId;

    @Column(name = "tax_number")
    private String taxNumber;

    @Column(name = "vendor_name", nullable = false)
    private String vendorName;

    @Column(name = "birth_name", nullable = false)
    private String birthName;

    @Column(name = "gender", nullable = false)
    private Boolean gender;

    @Column(name = "name_of_mother", nullable = false)
    private String nameOfMother;

    @Column(name = "birth_place", nullable = false)
    private String birthPlace;

    @Column(name = "birth_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date birthDate;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "account_manager", nullable = false)
    private String accountManager;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "address")
    private String address;

    @Column(name = "qualification", nullable = false)
    private String qualification;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "taj")
    private String taj;

    @Column(name = "activity")
    private String activity;

    @Column(name = "representative")
    private String representative;

    @Column(name = "file_number")
    private String fileNumber;

    @Column(name = "felir")
    private String felir;

    @Column(name = "registry_number")
    private String registryNumber;

    @Column(name = "pensioner", nullable = false)
    private Boolean pensioner;

    @Column(name = "szja", nullable = false)
    private Boolean szja;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vendor vendor = (Vendor) o;
        return Objects.equals(taxId, vendor.taxId) && Objects.equals(taxNumber, vendor.taxNumber) && Objects.equals(vendorName, vendor.vendorName) && Objects.equals(birthName, vendor.birthName) && Objects.equals(gender, vendor.gender) && Objects.equals(nameOfMother, vendor.nameOfMother) && Objects.equals(birthPlace, vendor.birthPlace) && Objects.equals(birthDate, vendor.birthDate) && Objects.equals(accountNumber, vendor.accountNumber) && Objects.equals(accountManager, vendor.accountManager) && Objects.equals(city, vendor.city) && Objects.equals(postalCode, vendor.postalCode) && Objects.equals(address, vendor.address) && Objects.equals(qualification, vendor.qualification) && Objects.equals(countryCode, vendor.countryCode) && Objects.equals(taj, vendor.taj) && Objects.equals(activity, vendor.activity) && Objects.equals(representative, vendor.representative) && Objects.equals(fileNumber, vendor.fileNumber) && Objects.equals(felir, vendor.felir) && Objects.equals(registryNumber, vendor.registryNumber) && Objects.equals(pensioner, vendor.pensioner) && Objects.equals(szja, vendor.szja);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxId, taxNumber, vendorName, birthName, gender, nameOfMother, birthPlace, birthDate, accountNumber, accountManager, city, postalCode, address, qualification, countryCode, taj, activity, representative, fileNumber, felir, registryNumber, pensioner, szja);
    }
}