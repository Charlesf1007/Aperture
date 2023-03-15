package com.capstone.Aperture.repository;

import com.capstone.Aperture.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
