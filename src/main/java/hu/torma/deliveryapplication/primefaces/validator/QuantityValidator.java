package hu.torma.deliveryapplication.primefaces.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("quantityValidator")
public class QuantityValidator implements Validator {


    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        try {
            int enteredQuantity = Integer.parseInt(value.toString());
            int maxQuantity = (int) component.getAttributes().get("maxQuantity");

            ((UIInput) component).setSubmittedValue(value);

            if (enteredQuantity > maxQuantity) {
                FacesMessage message = new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Hiba",
                        "A beírt mennyiség (" + enteredQuantity + ") nem lehet nagyobb, mint a maximális mennyiség (" + maxQuantity + ")!"
                );

                context.addMessage(component.getClientId(context), message);
                context.validationFailed();
                component.getAttributes().put("styleClass", "input-error");
            }
        } catch (NumberFormatException e) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Hiba",
                    "Érvénytelen számformátum!"
            );

            context.addMessage(component.getClientId(context), message);
            context.validationFailed();
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Hiba",
                    "Érvénytelen bemenet!"
            );

            context.addMessage(component.getClientId(context), message);
            context.validationFailed();
        }
    }
}
