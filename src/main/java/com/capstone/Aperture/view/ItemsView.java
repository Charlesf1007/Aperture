package com.capstone.Aperture.view;

import com.capstone.Aperture.entity.Product;
import com.capstone.Aperture.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.validation.constraints.NotEmpty;

import java.util.Collections;

@Route(value = "")
@PageTitle("Products")
public class ItemsView extends VerticalLayout {

    Grid<Product> grid = new Grid<>(Product.class);
    TextField searchBar = new TextField();
    EditorForm form;
    private CrmService service;

    public ItemsView(CrmService service){
        this.service = service;
        addClassName("list-view");
        setSizeFull();

        configureGrid();
        configureForm();
        add(GetControls(), getContent());

        updateRecord();
        closeEditor();
    }

    private void closeEditor() {
        form.setProduct(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateRecord() {
        grid.setItems(service.findAllProducts(searchBar.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();

        return content;
    }

    private Component GetControls() {
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

    private void addProduct() {
        grid.asSingleSelect().clear();
        editProduct(new Product());
    }

    private void configureGrid() {
        grid.addClassName("product-grid");
        grid.setSizeFull();
        grid.setColumns("name", "description","quantity","sold","restock");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editProduct(e.getValue()));
        }

    private void editProduct(Product product) {
        if(product == null){
            closeEditor();
        } else{
            form.setProduct(product);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void configureForm() {
        form = new EditorForm();
        form.setWidth("25em");

        form.addListener(EditorForm.SaveEvent.class, this::saveProduct);
        form.addListener(EditorForm.DeleteEvent.class, this::deleteProduct);
        form.addListener(EditorForm.DeleteEvent.class, e->closeEditor());

    }

    private void saveProduct(EditorForm.SaveEvent event){
        service.saveProduct(event.getProduct());
        updateRecord();
        closeEditor();
    }

    private void deleteProduct(EditorForm.DeleteEvent event){
        service.deleteProduct(event.getProduct());
        updateRecord();
        closeEditor();
    }
}
