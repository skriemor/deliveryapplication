package hu.torma.deliveryapplication.primefaces.sumutils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class PurchaseSumObj {

    Integer one,two,three,four,five,six;
    Double totalP, remP;

    public PurchaseSumObj(int one, int two, int three, int four, int five, int six, Double totalP, Double remP) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
        this.six = six;
        this.totalP = totalP;
        this.remP = remP;
    }

    public Integer getOne() {
        return one==null?0:one;
    }

    public Integer getTwo() {
        return two==null?0:two;
    }

    public Integer getThree() {
        return three==null?0:three;
    }

    public Integer getFour() {
        return four==null?0:four;
    }

    public Integer getFive() {
        return five==null?0:five;
    }

    public Integer getSix() {
        return six==null?0:six;
    }

    public Double getTotalP() {
        return totalP==null?0.0:totalP;
    }

    public Double getRemP() {
        return remP==null?0.0:remP;
    }
}
