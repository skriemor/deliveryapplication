package hu.torma.deliveryapplication.primefaces.converter;

import hu.torma.deliveryapplication.DTO.VendorDTO;
import hu.torma.deliveryapplication.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.logging.Logger;

@Service("vendorConverter")
public class VendorConverter implements Converter {
    Logger log = Logger.getLogger("LOGGER");
    @Autowired
    private VendorService service;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        String id = s.substring(s.indexOf("(") + 1, s.length() - 1);
        log.warning("string of vendor is " + id);
        return service.getVendorById(id);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof VendorDTO) {
            VendorDTO vendor = (VendorDTO) o;
            return vendor.getVendorName() + "(" + vendor.getTaxId() + ")";
        } else {
            String error =
                    "The object is not an instance of VendorDTO";
            throw new ClassCastException(error);
        }
    }
}
