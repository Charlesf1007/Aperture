package com.capstone.Aperture.service;

import com.capstone.Aperture.entity.Product;
import com.capstone.Aperture.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmService {


    private ProductRepository productRepository;

    public CrmService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAllProducts(String searchText){ //returns all the products depending on the search text
        if(searchText == null || searchText.isEmpty()){
            return productRepository.findAll();
        } else{
            return productRepository.search(searchText);
        }
    }


    public void deleteProduct(Product product){
        productRepository.delete(product);
    } //deletes product
    public void saveProduct(Product product){ //does validation then saves the product
        if(product == null){
            System.err.println("product is null");
            return;
        }

        productRepository.save(product);
    }
}
