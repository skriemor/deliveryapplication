package hu.torma.deliveryapplication.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "mediator")
public class Mediator {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "mediator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vendor> buyers;

}