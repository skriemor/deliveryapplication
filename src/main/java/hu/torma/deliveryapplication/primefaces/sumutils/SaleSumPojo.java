package hu.torma.deliveryapplication.primefaces.sumutils;

import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
public class SaleSumPojo   {
    int one,two,three,four,five,six;
    int priceTotal;

    public SaleSumPojo(List<SaleDTO> list) {
        this.one = list.stream().mapToInt(SaleDTO::getOne).sum();
        this.two = list.stream().mapToInt(SaleDTO::getTwo).sum();
        this.three = list.stream().mapToInt(SaleDTO::getThree).sum();
        this.four = list.stream().mapToInt(SaleDTO::getFour).sum();
        this.five = list.stream().mapToInt(SaleDTO::getFive).sum();
        this.six = list.stream().mapToInt(SaleDTO::getSix).sum();
        this.priceTotal = list.stream().mapToInt(SaleDTO::getPrice).sum();
    }

}
