package com.re.cinemabookingapp.repository;

import com.re.cinemabookingapp.entity.Product;
import com.re.cinemabookingapp.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatusOrderByTypeAscNameAsc(ProductStatus status);
}
