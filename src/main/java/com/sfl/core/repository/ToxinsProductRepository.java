package com.sfl.core.repository;

import com.sfl.core.domain.ToxinsProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ToxinsProductRepository extends JpaRepository<ToxinsProduct, Long>, JpaSpecificationExecutor<ToxinsProduct> {

    @Modifying
    @Query("update ToxinsProduct tp set tp.deleted = :delete where tp.id = :id")
    int updateDeleteStatus(boolean delete, long id);

}
