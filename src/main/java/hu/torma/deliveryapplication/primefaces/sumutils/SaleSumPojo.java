package hu.torma.deliveryapplication.primefaces.sumutils;

import hu.torma.deliveryapplication.DTO.PurchasedProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class SaleSumPojo   {

    Integer one,two,three,four,five,six;
    Map<String, Integer> currencyMap;
    String priceTotal;

    public SaleSumPojo(Integer one, Integer two, Integer three, Integer four, Integer five, Integer six) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
        this.six = six;
    }

    public SaleSumPojo(List<SaleDTO> list) {
        this.one = list.stream().mapToInt(SaleDTO::getOne).sum();
        this.two = list.stream().mapToInt(SaleDTO::getTwo).sum();
        this.three = list.stream().mapToInt(SaleDTO::getThree).sum();
        this.four = list.stream().mapToInt(SaleDTO::getFour).sum();
        this.five = list.stream().mapToInt(SaleDTO::getFive).sum();
        this.six = list.stream().mapToInt(SaleDTO::getSix).sum();

        this.currencyMap = list.stream().collect(
                Collectors.groupingBy(
                        SaleDTO::getCurrency,
                        Collectors.summingInt(SaleDTO::getPrice)
                )
        );

        this.priceTotal = currencyMap.keySet().stream()
                .sorted((o1, o2) -> o1.toString().equals("HUF")?-1:1)
                .map(k -> getFormattedNumber(currencyMap.get(k)) + " "  + k )
                .collect(Collectors.joining("] \n[", "[","]"));
    }
    /**
     * To be refactored
     */
    public String getFormattedNumber(int num) {
        return NumberFormat.getNumberInstance(Locale.US).format(num).replaceAll(",", " ");
    }
}
