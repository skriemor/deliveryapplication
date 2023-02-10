package hu.torma.deliveryapplication.primefaces.converter;

import hu.torma.deliveryapplication.DTO.UnitDTO;
import hu.torma.deliveryapplication.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Service("unitConverter")
public class UnitConverter implements Converter {
    @Autowired
    private UnitService service;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return service.getUnitByName(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof UnitDTO) {
            UnitDTO unit = (UnitDTO) o;
            return unit.getId();
        } else {
            String error =
                    "The object is not an instance of UnitDTO";
            throw new ClassCastException(error);
        }
    }
}
