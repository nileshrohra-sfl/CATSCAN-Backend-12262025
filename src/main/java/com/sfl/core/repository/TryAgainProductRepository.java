package com.sfl.core.repository;

import com.sfl.core.domain.TryAgainProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TryAgainProductRepository extends JpaRepository<TryAgainProduct, Long>, JpaSpecificationExecutor<TryAgainProduct> {

    Page<TryAgainProduct> findAll(Pageable pageable);
}
