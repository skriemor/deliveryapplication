package hu.torma.deliveryapplication.entity;

import javax.persistence.*;

@Entity
@Table(name = "delivery_letter")
public class DeliveryLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}