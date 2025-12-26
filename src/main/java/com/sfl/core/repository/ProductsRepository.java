package com.sfl.core.repository;

import com.sfl.core.domain.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * A Repository for {@link Products}
 */
public interface ProductsRepository extends JpaRepository<Products, Long> {

    @Query("SELECT prd FROM Products prd " +
        "WHERE prd.name LIKE CONCAT('%',:keyword,'%') " +
        "OR prd.description LIKE CONCAT('%',:keyword,'%')")
    List<Products> findBySearchKeyWord(String keyword);
}
