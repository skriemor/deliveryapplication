package hu.torma.deliveryapplication.primefaces.converter;

import hu.torma.deliveryapplication.DTO.BuyerDTO;
import hu.torma.deliveryapplication.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.logging.Logger;

@Service("buyerConverter")
public class BuyerConverter implements Converter {
    Logger log = Logger.getLogger("LOGGER");
    @Autowired
    private BuyerService service;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        String id = s.substring(s.indexOf("(") + 1, s.length() - 1);
        log.warning("string of buyer is " + id);
        return service.getBuyerById(id);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof BuyerDTO) {
            BuyerDTO buyer = (BuyerDTO) o;
            return buyer.getName() + "(" + buyer.getAccountNum() + ")";
        } else {
            if (o==null) return "Válasszon";
            String error =
                    "The object is not an instance of BuyerDTO";
            throw new ClassCastException(error);
        }
    }
}
