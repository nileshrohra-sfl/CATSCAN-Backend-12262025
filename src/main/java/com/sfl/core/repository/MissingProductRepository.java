package com.sfl.core.repository;

import com.sfl.core.domain.MissingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MissingProductRepository extends JpaRepository<MissingProduct, Long>, JpaSpecificationExecutor<MissingProduct> {
}
