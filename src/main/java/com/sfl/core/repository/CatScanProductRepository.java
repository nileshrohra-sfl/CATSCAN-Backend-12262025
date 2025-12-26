package com.sfl.core.repository;

import com.sfl.core.domain.CatScanProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CatScanProductRepository extends JpaRepository<CatScanProduct, Long>, JpaSpecificationExecutor<CatScanProduct> {

    @Modifying
    @Query("update CatScanProduct cp set cp.deleted = :delete where cp.id = :id")
    int updateDeleteStatus(boolean delete, long id);

}
