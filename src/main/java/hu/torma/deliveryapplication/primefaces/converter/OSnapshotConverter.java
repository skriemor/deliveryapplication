package hu.torma.deliveryapplication.primefaces.converter;


import hu.torma.deliveryapplication.DTO.OfficialStorageSnapshotDTO;
import hu.torma.deliveryapplication.service.OSnapshotService;
import hu.torma.deliveryapplication.utility.dateutil.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Service("oSnapShotConverter")
public class OSnapshotConverter implements Converter {

    @Autowired
    private OSnapshotService service;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s.contains("(")) {
            var id = Long.valueOf(s.substring(0, s.indexOf(" (")));
            return service.getSnapshotById(id);
        }
        return service.getAnySnapshot();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o instanceof OfficialStorageSnapshotDTO) {
            OfficialStorageSnapshotDTO snap = (OfficialStorageSnapshotDTO) o;
            return snap.getId()
                    + " ("
                    + DateConverter.toDottedDate(snap.getDateFrom())
                    + " - "
                    + DateConverter.toDottedDate(snap.getDateTo())
                    + ")";
        } else {
            return "Válasszon!";

        }
    }
}
