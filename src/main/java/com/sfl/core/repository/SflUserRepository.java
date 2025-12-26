package com.sfl.core.repository;

import com.sfl.core.domain.CountryDialCode;
import com.sfl.core.domain.SflUser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.sfl.core.repository.constants.CacheConstants.USERS_BY_EMAIL_CACHE;
import static com.sfl.core.repository.constants.CacheConstants.USERS_BY_USER_NAME_CACHE;

/**
 * Spring Data  repository for the SflUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SflUserRepository extends JpaRepository<SflUser, Long> {

    @Cacheable(cacheNames = USERS_BY_USER_NAME_CACHE)
    Optional<SflUser> findOneWithAuthoritiesByUserName(String userName);

    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<SflUser> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Optional<SflUser> findOneByActivationKey(String key);

    Optional<SflUser> findOneByUserName(String userName);

    Optional<SflUser> findOneByEmailIgnoreCase(String email);

    Optional<SflUser> findOneByEmailIgnoreCaseAndStatus(String email, Boolean status);

    Optional<SflUser> findOneWithAuthoritiesById(Long id);

    Optional<SflUser> findOneByResetKey(String key);

    Optional<SflUser> findOneByPhoneNumberAndCountryDialCode(Long number, CountryDialCode countryDialCode);

    boolean existsByResetKey(String key);

    Page<SflUser> findAllByStatus(Boolean status, Pageable pageable);

    Optional<SflUser> findOneByPhoneNumber(Long number);

    SflUser findByEmailOrUserName(String email, String userName);

    boolean existsByEmailOrUserNameAndStatus(String email, String userName, boolean status);

    @Query("Select u from SflUser u where u.phoneNumber=:number and u.countryDialCode=:countryDialCode")
    Optional<SflUser> findByPhoneNumberAndCountryCode(Long number, CountryDialCode countryDialCode);

    //    Optional<SflUser> findOneByEmailOrPhoneNumberIgnoreCase(String email,String phone);
    @Query("select u from SflUser u where lower(u.email) = lower(:login) or str(u.phoneNumber) = :login")
    Optional<SflUser> findByLogin(String login);

}
