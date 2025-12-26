package com.sfl.core.repository;


import com.sfl.core.domain.FavoriteProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {

    Page<FavoriteProduct> findAllByUserId(Long userId, Pageable pageable);

    @Query("select fp.prodId from FavoriteProduct fp where fp.user.id = :userId")
    List<Long> findProdIdsByUserId(@Param("userId") Long userId);

}

