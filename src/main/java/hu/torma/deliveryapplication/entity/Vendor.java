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
public class Vendor {
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
    private String gender;

    @Column(name = "name_of_mother", nullable = false)
    private String nameOfMother;

    @Column(name = "birth_place", nullable = false)
    private String birthPlace;

    @Column(name = "birth_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date birthDate;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_manager")
    private String accountManager;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "address")
    private String address;

    @Column(name = "qualification", nullable = false)
    private String qualification;


    @Column(name = "taj")
    private String taj;

    @Column(name = "activity")
    private String activity;

    @Column(name = "file_number")
    private String fileNumber;

    @Column(name = "felir")
    private String felir;

}