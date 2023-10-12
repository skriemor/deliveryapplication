package hu.torma.deliveryapplication.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor

public class MediatorDisplay {
    public MediatorDisplay(String mediatorName, int one, int two, int three, int four, int five, int six, int price) {
        this.mediatorName = mediatorName;
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
        this.six = six;
        this.price = price;
    }

    private String mediatorName;
    private int one,two,three,four,five,six;

    private int price;


}
