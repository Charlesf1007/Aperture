package com.capstone.Aperture.view;

import com.capstone.Aperture.entity.Product;
import com.capstone.Aperture.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "")
@PageTitle("Products")
public class ItemsView extends VerticalLayout {

    Grid<Product> grid = new Grid<>(Product.class);
    TextField searchBar = new TextField();
    EditorForm form;
    private CrmService service;


    public ItemsView(CrmService service){ //compiler which is run when instantiated which sets all the correct settings
        this.service = service;
        addClassName("list-view");
        setSizeFull();

        configureGrid();
        configureForm();
        add(GetControls(), getContent());

        updateRecord();
        closeEditor();
    }

    private void closeEditor() { //method used to close the editor form
        form.setProduct(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateRecord() { //update the grid with the full list of items
        grid.setItems(service.findAllProducts(searchBar.getValue()));
        List<Product> products = service.findAllProducts("");
        for(Product product : products){
            if(product.isRestock()){
                Notification.show(product.getName()+" needs restocking");
            }
        }
    }

    private Component getContent() { // sets how the content of the screen layed out
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid); //make the grid with all items to 2/3 of the screen
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();

        return content;
    }

    private Component GetControls() { //gets the controls for the admin (search and add item)
        searchBar.setPlaceholder("Search product");
        searchBar.setClearButtonVisible(true);
        searchBar.setValueChangeMode(ValueChangeMode.LAZY);
        searchBar.addValueChangeListener(e -> updateRecord());

        Button newProduct = new Button("add Item");
        newProduct.addClickListener(e->addProduct());

        HorizontalLayout controls = new HorizontalLayout(searchBar, newProduct);
        controls.addClassName("control");
        return controls;
    }

    private void addProduct() { //when selected to edit specific product
        grid.asSingleSelect().clear();
        editProduct(new Product());
    }

    private void configureGrid() { //layout for the grid stating headers
        grid.addClassName("product-grid");
        grid.setSizeFull();
        grid.setColumns("name", "description","quantity","sold","restock");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        //listener for single select to edit product
        grid.asSingleSelect().addValueChangeListener(e -> editProduct(e.getValue()));
        }

    private void editProduct(Product product) { //used in addProduct method used to set form to product selected
        if(product == null){
            closeEditor();
        } else{
            form.setProduct(product);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void configureForm() { //adds the listener to the buttons of the form
        form = new EditorForm();
        form.setWidth("25em");

        form.addListener(EditorForm.SaveEvent.class, this::saveProduct);
        form.addListener(EditorForm.DeleteEvent.class, this::deleteProduct);
        form.addListener(EditorForm.CloseEvent.class, e -> closeEditor());

    }

    private void saveProduct(EditorForm.SaveEvent event){ //used in configure form used to save product using
        service.saveProduct(event.getProduct());
        updateRecord();
        closeEditor();
    }

    private void deleteProduct(EditorForm.DeleteEvent event){ //deletes a record and then updates the grid and closes editor
        service.deleteProduct(event.getProduct());
        updateRecord();
        closeEditor();
    }
}
