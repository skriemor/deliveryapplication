package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.DTO.DisplayUnit;
import hu.torma.deliveryapplication.DTO.MediatorDTO;
import hu.torma.deliveryapplication.primefaces.sumutils.MediatorData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "mediator")
@NamedNativeQuery(name = "get_mediator_data",
        query = """
select m.id as mediatorName, pp.p_id as productName,sum(pp.quantity2) as quantity,sum( pp.total_price) as totalPrice
from purchased_product pp
join purchase p on p.id = pp.purchase_id
join vendor v on p.vendor_name = v.tax_id
join mediator m on v.mediator_id = m.id

where (
            p.receipt_date is null
                or (?1 is not null and ?2 is not null and p.receipt_date between ?1  and ?2)
                or (?1 is not null and ?2 is null and  p.receipt_date >= ?1 )
                or (?2 is not null and ?1 is null and  p.receipt_date <  ?2 )
                or (?1 is null and  ?2 is null)
        )
group by m.id, p_id
ORDER BY m.id, CASE
WHEN pp.p_id like 'I.O%' then 1
WHEN pp.p_id like 'II.O%' then 2
WHEN pp.p_id like 'III.O%' then 3
WHEN pp.p_id like 'IV.O%' then 4
WHEN pp.p_id like 'GYÖK%' then 5
WHEN pp.p_id like 'IP%' then 6
END ASC
""",
        resultSetMapping = "mediator_mapping")
@SqlResultSetMapping(
        name = "mediator_mapping",
        classes = @ConstructorResult(
                targetClass = MediatorData.class,
                columns = {
                        @ColumnResult(name = "mediatorName", type = String.class),
                        @ColumnResult(name = "productName", type = String.class),
                        @ColumnResult(name = "quantity", type = Integer.class),
                        @ColumnResult(name = "totalPrice", type = Integer.class)
                }
        )
)
public class Mediator {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "mediator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vendor> buyers;

    public MediatorDTO toDTO() {
        MediatorDTO dto = new MediatorDTO();
        dto.setId(this.id);

        if (Hibernate.isInitialized(this.buyers)) {
            dto.setBuyers(this.buyers.stream()
                    .filter(buyer -> Hibernate.isInitialized(buyer) && !(buyer instanceof HibernateProxy))
                    .map(Vendor::toDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}