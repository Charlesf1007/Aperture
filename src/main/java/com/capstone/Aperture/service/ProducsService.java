package com.capstone.Aperture.service;

import com.capstone.Aperture.entity.Product;

import java.util.List;

public interface ProducsService {

    List<Product> listProducts();

    Product findProduct(long id);
}
