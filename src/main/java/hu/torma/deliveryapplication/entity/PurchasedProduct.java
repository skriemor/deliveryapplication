package hu.torma.deliveryapplication.entity;

import hu.torma.deliveryapplication.DTO.DisplayUnit;
import hu.torma.deliveryapplication.primefaces.sumutils.SaleSumPojo;
import lombok.Data;

import javax.persistence.*;

@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = "get_real_storage_date",
                query = """
SELECT
PP.P_ID as product,
SUM(
     case when PP.SALE_ID IS NULL THEN PP.QUANTITY2
     ELSE -PP.QUANTITY end
) as quantity
FROM PURCHASED_PRODUCT PP

left join sale s on s.id = pp.sale_id
left join purchase p on p.id = pp.purchase_id
where (case when pp.sale_id is not null then
( 
     (:date1 is not null and :date2 is not null and s.receipt_date between :date1  and :date2)
                    or (:date1 is not null and :date2 is null and  s.receipt_date >= :date1 )
                    or (:date2 is not null and :date1 is null and  s.receipt_date <  :date2 )
                    or (:date1 is null and  :date2 is null)

)
else 
(
    (:date1 is not null and :date2 is not null and p.receipt_date between :date1  and :date2)
                    or (:date1 is not null and :date2 is null and  p.receipt_date >= :date1 )
                    or (:date2 is not null and :date1 is null and  p.receipt_date <  :date2 )
                    or (:date1 is null and  :date2 is null)
)
  
  end)


GROUP BY product
ORDER BY CASE
WHEN pp.p_id like 'I.O%' then 1
WHEN pp.p_id like 'II.O%' then 2
WHEN pp.p_id like 'III.O%' then 3
WHEN pp.p_id like 'IV.O%' then 4
WHEN pp.p_id like 'GYÖK%' then 5
WHEN pp.p_id like 'IP%' then 6
END ASC

""",
                resultSetMapping = "display_unit_mapping"),




        @NamedNativeQuery(name = "get_real_storage",
                query = """
SELECT 
PP.P_ID as product,
SUM(
     case when PP.SALE_ID IS NULL THEN PP.QUANTITY2
     ELSE -PP.QUANTITY end
) as quantity
FROM PURCHASED_PRODUCT PP
GROUP BY product
ORDER BY CASE
WHEN pp.p_id like 'I.O%' then 1
WHEN pp.p_id like 'II.O%' then 2
WHEN pp.p_id like 'III.O%' then 3
WHEN pp.p_id like 'IV.O%' then 4
WHEN pp.p_id like 'GYÖK%' then 5
WHEN pp.p_id like 'IP%' then 6
END ASC
""",
        resultSetMapping = "display_unit_mapping"),



        @NamedNativeQuery(name = "get_fictional_storage",
        query = """
select 
 sum(r.one) - (case when oneSum is null then 0 else oneSum end) as one,
 sum(r.two) - (case when twoSum is null then 0 else twoSum end) as two,
 sum(r.three) -(case when threeSum is null then 0 else threeSum end) as three,
 sum(r.four) - (case when fourSum is null then 0 else fourSum end)as four,
 sum(r.five) - (case when fiveSum is null then 0 else fiveSum end)as five,
 sum(r.six) - (case when sixSum is null then 0 else sixSum end)as six

from records r
cross join (
select 
sum(case when pp.p_id like 'I.OSZTÁLYÚ' then pp.quantity end) as oneSum,
sum(case when pp.p_id like 'II.OSZTÁLYÚ' then pp.quantity end) as twoSum,
sum(case when pp.p_id like 'III.OSZTÁLYÚ' then pp.quantity end) as threeSum,
sum(case when pp.p_id like 'IV.OSZTÁLYÚ' then pp.quantity end) as fourSum,
sum(case when pp.p_id like 'GYÖKÉR' then pp.quantity end) as fiveSum,
sum(case when pp.p_id like 'IPARI' then pp.quantity end) as sixSum

from purchased_product pp
join sale s on s.id = pp.sale_id
join buyer b on b.id = s.buyer_name
where pp.sale_id is not null
and b.paper like '%Igen%'
)
""",
        resultSetMapping = "salesum_mapping"),


        @NamedNativeQuery(name = "get_fictional_storage_date",
                query = """
select
 sum(r.one) - (case when oneSum is null then 0 else oneSum end) as one,
 sum(r.two) - (case when twoSum is null then 0 else twoSum end) as two,
 sum(r.three) -(case when threeSum is null then 0 else threeSum end) as three,
 sum(r.four) - (case when fourSum is null then 0 else fourSum end)as four,
 sum(r.five) - (case when fiveSum is null then 0 else fiveSum end)as five,
 sum(r.six) - (case when sixSum is null then 0 else sixSum end)as six
 
 from records r
 cross join (
 select
 sum(case when pp.p_id like 'I.OSZTÁLYÚ' then pp.quantity end) as oneSum,
 sum(case when pp.p_id like 'II.OSZTÁLYÚ' then pp.quantity end) as twoSum,
 sum(case when pp.p_id like 'III.OSZTÁLYÚ' then pp.quantity end) as threeSum,
 sum(case when pp.p_id like 'IV.OSZTÁLYÚ' then pp.quantity end) as fourSum,
 sum(case when pp.p_id like 'GYÖKÉR' then pp.quantity end) as fiveSum,
 sum(case when pp.p_id like 'IPARI' then pp.quantity end) as sixSum
 
 from purchased_product pp
 join sale s on s.id = pp.sale_id
 join buyer b on b.id = s.buyer_name
 where pp.sale_id is not null
 and b.paper like '%Igen%'
 and (
 (:date1 is not null and :date2 is not null and s.receipt_date between :date1  and :date2)
                or (:date1 is not null and :date2 is null and  s.receipt_date >= :date1 )
                or (:date2 is not null and :date1 is null and  s.receipt_date <  :date2 )
                or (:date1 is null and  :date2 is null)
 )
 )
 left join completed_purchase cp on cp.id = r.completed_id
 where (
  (:date1 is not null and :date2 is not null and cp.receipt_date between :date1  and :date2)
                or (:date1 is not null and :date2 is null and  cp.receipt_date >= :date1 )
                or (:date2 is not null and :date1 is null and  cp.receipt_date <  :date2 )
                or (:date1 is null and  :date2 is null)
 )
""",
                resultSetMapping = "salesum_mapping")


})
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "display_unit_mapping",
                classes = @ConstructorResult(
                        targetClass = DisplayUnit.class,
                        columns = {
                                @ColumnResult(name = "product", type = String.class),
                                @ColumnResult(name = "quantity", type = Integer.class)
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "salesum_mapping",
                classes = @ConstructorResult(
                        targetClass = SaleSumPojo.class,
                        columns = {
                                @ColumnResult(name = "one", type = Integer.class),
                                @ColumnResult(name = "two", type = Integer.class),
                                @ColumnResult(name = "three", type = Integer.class),
                                @ColumnResult(name = "four", type = Integer.class),
                                @ColumnResult(name = "five", type = Integer.class),
                                @ColumnResult(name = "six", type = Integer.class)
                        }
                )
        )
})
@Data
@Table(name = "purchased_product")
public class PurchasedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer Id;

    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "quantity2")
    private Integer quantity2;

    @Column(name = "correction_percent")
    private Integer corrPercent;


    @Column(name = "total_price")
    private Integer totalPrice;

    @ManyToOne
    @JoinColumn(name = "p_id", nullable = false, referencedColumnName = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "purchase_id", referencedColumnName = "id")
    private Purchase purchase;


    @ManyToOne
    @JoinColumn(name = "sale_id", referencedColumnName = "id")
    private Sale sale;


}