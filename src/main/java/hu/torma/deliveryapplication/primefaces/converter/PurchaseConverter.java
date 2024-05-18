package hu.torma.deliveryapplication.primefaces.converter;

import hu.torma.deliveryapplication.DTO.PurchaseDTO;
import hu.torma.deliveryapplication.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.logging.Logger;

@Service("purchaseConverter")
public class PurchaseConverter implements Converter {
    Logger log = Logger.getLogger("PurchaseCLog");
    @Autowired
    private PurchaseService service;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s.contains("(")){
            var id = Integer.valueOf(s.substring(0, s.indexOf(" (")));
            return service.getPurchaseById(id);
        }
        var g = service.getAllPurchases();
        if (g !=null && g.size()>0) return service.getAllPurchases().get(0);
        return new PurchaseDTO();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof PurchaseDTO) {
            PurchaseDTO purchase = (PurchaseDTO) o;
            return purchase.getId() + " (" + purchase.getRemainingPrice().intValue() + " HUF)";
        } else {
            if (o==null) return "Válasszon";
            String error =
                    "The object is not an instance of PurchaseDTO";
            throw new ClassCastException(error);
        }
    }
}
