package com.capstone.Aperture.repository;

import com.capstone.Aperture.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /*
    heavly inspired from the
    https://vaadin.com/docs/latest/tutorial/database-access
    uses Java Persistence Query Language to query the database to find a list of products
    that contain a string in their name
     */
    @Query("select p from Product p " +
            "where lower(p.name) like lower(concat('%', :searchTerm, '%')) ")
    List<Product> search(@Param("searchTerm") String searchTerm);

}
