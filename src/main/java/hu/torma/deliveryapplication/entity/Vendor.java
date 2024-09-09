package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.DTO.VendorDTO;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Data
@Table(name = "vendor")
@ToString
public class Vendor {
    @Id
    @Column(name = "tax_id", nullable = false)
    private String taxId;

    @Column(name = "tax_number")
    private String taxNumber;

    @Column(name = "vendor_name", nullable = false)
    private String vendorName;

    @Column(name = "birth_name")
    private String birthName;


    @Column(name = "name_of_mother")
    private String nameOfMother;

    @Column(name = "birth_place")
    private String birthPlace;

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private Date birthDate;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "address")
    private String address;

    @Column(name = "qualification")
    private String qualification;


    @Column(name = "taj")
    private String taj;

    @Column(name = "activity")
    private String activity;

    @Column(name = "file_number")
    private String fileNumber;

    @Column(name = "felir")
    private String felir;

    @Column(name = "ggn")
    private String ggn;

    @Column(name = "phone")
    private String phone;

    @Column(name = "contract")
    private String contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mediator_id")
    private Mediator mediator;

    public VendorDTO toDTO(boolean includeMediator) {
        VendorDTO dto = new VendorDTO();
        dto.setTaxId(this.taxId);
        dto.setTaxNumber(this.taxNumber);
        dto.setVendorName(this.vendorName);
        dto.setBirthName(this.birthName);
        dto.setNameOfMother(this.nameOfMother);
        dto.setBirthPlace(this.birthPlace);
        dto.setBirthDate(this.birthDate);
        dto.setAccountNumber(this.accountNumber);
        dto.setCity(this.city);
        dto.setPostalCode(this.postalCode);
        dto.setAddress(this.address);
        dto.setQualification(this.qualification);
        dto.setTaj(this.taj);
        dto.setActivity(this.activity);
        dto.setFileNumber(this.fileNumber);
        dto.setFelir(this.felir);
        dto.setGgn(this.ggn);
        dto.setPhone(this.phone);
        dto.setContract(this.contract);

        if (includeMediator && this.mediator != null && Hibernate.isInitialized(this.mediator) && !(this.mediator instanceof HibernateProxy)) {
            dto.setMediator(this.mediator.toDTO(false));
        }

        return dto;
    }

}