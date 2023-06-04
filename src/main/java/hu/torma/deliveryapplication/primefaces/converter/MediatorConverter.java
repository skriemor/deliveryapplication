package hu.torma.deliveryapplication.primefaces.converter;

import hu.torma.deliveryapplication.DTO.MediatorDTO;
import hu.torma.deliveryapplication.service.MediatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Service("mediatorConverter")
public class MediatorConverter implements Converter {
    @Autowired
    private MediatorService service;


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return service.getMediatorById(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof MediatorDTO) {
            MediatorDTO mediator = (MediatorDTO) o;
            return mediator.getId();
        } else {
            if (o==null) return "Válasszon";
            String error =
                    "The object is not an instance of mediator";
            throw new ClassCastException(error);
        }
    }
}
