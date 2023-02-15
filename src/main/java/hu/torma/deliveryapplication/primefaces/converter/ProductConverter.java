package hu.torma.deliveryapplication.primefaces.converter;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
@Service("productConverter")
public class ProductConverter implements Converter {
    @Autowired
    private ProductService service;


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return service.getProductById(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof ProductDTO) {
            ProductDTO product = (ProductDTO) o;
            return product.getId();
        } else {
            String error =
                    "The object is not an instance of VendorDTO";
            throw new ClassCastException(error);
        }
    }
}
