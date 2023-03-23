package com.capstone.Aperture.view;

import com.capstone.Aperture.entity.Product;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;



public class EditorForm extends FormLayout {

    //bean validation binder takes in validation annotations and relating it to product specifies the type of object
    Binder<Product> binder = new BeanValidationBinder<>(Product.class);
    TextField name = new TextField("name");
    TextArea description = new TextArea("description");
    IntegerField quantity = new IntegerField("quantity");
    IntegerField sold = new IntegerField("sold");

    Checkbox restock = new Checkbox("restock");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");
    private Product product;

    public EditorForm(){ //constructor
        addClassName("product-form");
        binder.bindInstanceFields(this);
        quantity.setStepButtonsVisible(true); //adds the incrementor to the quantity field
        sold.setStepButtonsVisible(true); //same as quantity but for sold

        add(name, description, description, quantity, sold, restock, createButtonLayout());
    }

    public void setProduct(Product product){
        this.product = product;
        binder.readBean(product);
    }

    private Component createButtonLayout(){ //lays out the buttons and adds listeners to them
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        //click listeners for when button is pressed
        save.addClickListener(event->validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, product)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER); //keyboard shortcut so instead of clicking save can just enter
        cancel.addClickShortcut(Key.ESCAPE); //same as above but escape

        return new HorizontalLayout(save, delete, cancel); //horizontal makes the buttons in a row
    }

    private void validateAndSave() { //uses try catch to save product
        try{
            binder.writeBean(product);
            fireEvent(new SaveEvent(this, product));
        } catch (ValidationException e){
            e.printStackTrace();
        }
    }


    public static abstract class ProductFormEvent extends ComponentEvent<EditorForm> {
        /*
            extending the Component event from Vaadin

            inspired from
            https://vaadin.com/docs/latest/tutorial/forms-and-validation
         */
        private Product product; //product object to hold the product that is being affected

        protected ProductFormEvent(EditorForm source, Product product) { //constructor
            super(source, false);
            this.product = product;
        }


        public Product getProduct() { //getter
            return product;
        }
    }
        public static class SaveEvent extends ProductFormEvent{
            SaveEvent(EditorForm source, Product product){
                super(source, product);
            }
        }

        public static class DeleteEvent extends ProductFormEvent{ //
            DeleteEvent(EditorForm source, Product product){
                super(source, product);
            }
        }

        public static class CloseEvent extends ProductFormEvent{ //
            CloseEvent(EditorForm source){
                super(source, null);
            }
        }

        public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, //event listener to add the object to the vaadin event bus
                                                                      ComponentEventListener<T> listener){
            return getEventBus().addListener(eventType, listener);
        }
}
