package hu.torma.deliveryapplication.primefaces.converter;

import hu.torma.deliveryapplication.DTO.SiteDTO;
import hu.torma.deliveryapplication.DTO.VendorDTO;
import hu.torma.deliveryapplication.service.SiteService;
import hu.torma.deliveryapplication.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Service("siteConverter")
public class SiteConverter implements Converter {

    @Autowired
    private SiteService service;


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return service.getSiteById(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof SiteDTO) {
            SiteDTO site = (SiteDTO) o;
            return site.getSiteName();
        } else {
            String error =
                    "The object is not an instance of VendorDTO";
            throw new ClassCastException(error);
        }
    }
}
