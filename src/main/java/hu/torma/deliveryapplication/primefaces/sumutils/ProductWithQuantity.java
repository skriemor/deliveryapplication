package hu.torma.deliveryapplication.primefaces.sumutils;



public class ProductWithQuantity {

    String product;
    Integer quantity;

    public ProductWithQuantity(String product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
