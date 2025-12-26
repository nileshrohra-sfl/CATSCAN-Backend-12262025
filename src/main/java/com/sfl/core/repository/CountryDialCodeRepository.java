package com.sfl.core.repository;

import com.sfl.core.domain.CountryDialCode;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the CountryDialCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryDialCodeRepository extends JpaRepository<CountryDialCode, Long> {

    @Query("SELECT countryDialCode FROM CountryDialCode countryDialCode ORDER BY case when countryDialCode.countryName = :countryName then -1 else 1 end")
    List<CountryDialCode> findAll(@Param("countryName") String countryName);

    CountryDialCode findByDialCode(String countryDialCode);
}
