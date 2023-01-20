package hu.torma.deliveryapplication.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "site")
public class Site implements Serializable {
    @Id
    @Column(name = "site_id", nullable = false)
    private Long id;

    @Column(name = "site_name", nullable = false)
    private String siteName;

}