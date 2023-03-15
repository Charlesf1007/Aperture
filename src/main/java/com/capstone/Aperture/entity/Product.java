package com.capstone.Aperture.entity;

import javax.persistence.Entity;

@Entity
public class Product {

    private Long id;

    private String name;

    private String description;

    private boolean restock;

    private int quantity;

    private int sold;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRestock() {
        return restock;
    }

    public void setRestock(boolean restock) {
        this.restock = restock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}
