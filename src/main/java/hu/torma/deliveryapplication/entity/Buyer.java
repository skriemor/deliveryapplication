package hu.torma.deliveryapplication.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "buyer")
public class Buyer {
    @Id
    @Column(name = "id", nullable = false)
    private String accountNum;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "paper", nullable = false)
    private String paper;
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "address", nullable = false)
    private String address;

}