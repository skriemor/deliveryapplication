package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.DTO.BuyerDTO;
import lombok.Data;

import javax.persistence.*;

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

    public BuyerDTO toDTO() {
        BuyerDTO dto = new BuyerDTO();
        dto.setAccountNum(this.accountNum);
        dto.setName(this.name);
        dto.setPaper(this.paper);
        dto.setCountry(this.country);
        dto.setAddress(this.address);
        return dto;
    }
}