package com.sfl.core.repository;

import com.sfl.core.domain.SflUserExtend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SflUserExtend entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SflUserExtendRepository extends JpaRepository<SflUserExtend, Long>, JpaSpecificationExecutor<SflUserExtend> {

    List<SflUserExtend> findAllByIsDeleted(Boolean isDeleted);

    long countByStatusAndIsDeleted(Boolean status, Boolean isDeleted);

    @Query(
        value="SELECT user from SflUser user "
            + "where (user.email LIKE %:search% OR user.firstName LIKE %:search% OR user.lastName LIKE %:search% " +
            " OR user.phoneNumber LIKE %:search% OR user.allergens LIKE %:search% )")
    Page<SflUserExtend> findAllBySearchKey( @Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(u) > 0 FROM SflUserExtend u WHERE u.email = :email AND u.id != :id")
    boolean getCountByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);
}
