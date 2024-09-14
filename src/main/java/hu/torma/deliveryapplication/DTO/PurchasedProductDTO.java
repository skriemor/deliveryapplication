package hu.torma.deliveryapplication.DTO;

import hu.torma.deliveryapplication.entity.Product;
import hu.torma.deliveryapplication.entity.Purchase;
import hu.torma.deliveryapplication.entity.PurchasedProduct;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class PurchasedProductDTO implements Serializable {
    private Integer id;

    private Integer unitPrice;

    private Integer quantity;

    private Integer quantity2;

    private Integer corrPercent;

    private Double totalPrice;

    private PurchaseDTO purchase;

    private ProductDTO product;

    private SaleDTO sale;

    public Integer getQuantity2(){
        return this.quantity2==null?0:this.quantity2;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchasedProductDTO that = (PurchasedProductDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(quantity, that.quantity) && Objects.equals(quantity2, that.quantity2) && Objects.equals(corrPercent, that.corrPercent) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(purchase, that.purchase) && Objects.equals(product, that.product) && Objects.equals(sale, that.sale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unitPrice, quantity, quantity2, corrPercent, totalPrice, purchase, product, sale);
    }


    public PurchasedProduct toEntity(boolean includeProduct, boolean includePurchase, boolean includeSale) {
        PurchasedProduct entity = new PurchasedProduct();
        entity.setId(this.id);
        entity.setUnitPrice(this.unitPrice);
        entity.setQuantity(this.quantity);
        entity.setQuantity2(this.quantity2);
        entity.setCorrPercent(this.corrPercent);
        entity.setTotalPrice(this.totalPrice);

        if (includeProduct && this.product != null) {
            entity.setProduct(this.product.toEntity(false)); // Avoid recursion in Product
        }

        if (includePurchase && this.purchase != null) {
            entity.setPurchase(this.purchase.toEntity(false, false, false)); // Avoid recursion in Purchase
        }

        if (includeSale && this.sale != null) {
            entity.setSale(this.sale.toEntity(true, true)); // Avoid recursion in Sale
        }

        return entity;
    }


    public Integer getNetOf() {
        if (this.quantity == null || this.corrPercent == null) {
            return 0;
        }
        this.quantity2 = (int) (this.quantity * ((100 - this.corrPercent) / 100.0));
        return this.quantity2;
    }
}
