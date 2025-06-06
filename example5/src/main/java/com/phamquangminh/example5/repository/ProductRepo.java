package com.phamquangminh.example5.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phamquangminh.example5.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Page<Product> findByProductNameLike(String keyword, Pageable pageDetails);

    Page<Product> findByCategoryCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByProductNameContaining(String keyword, Pageable pageable);
}
