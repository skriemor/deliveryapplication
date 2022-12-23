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

    @Column(name = "tax_number", nullable = false)
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

    @Column(name = "account_manage", nullable = false)
    private String accountManager;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "qualification", nullable = false)
    private String qualification;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "taj", nullable = false)
    private String taj;

    @Column(name = "activity", nullable = false)
    private String activity;

    @Column(name = "representative", nullable = false)
    private String representative;

    @Column(name = "file_number", nullable = false)
    private String fileNumber;

    @Column(name = "felir", nullable = false)
    private String felir;

    @Column(name = "registry_number", nullable = false)
    private String registryNumber;

    @Column(name = "pensioner", nullable = false)
    private Boolean pensioner;

    @Column(name = "szja", nullable = false)
    private String szja;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Vendor vendor = (Vendor) o;
        return taxId != null && Objects.equals(taxId, vendor.taxId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}