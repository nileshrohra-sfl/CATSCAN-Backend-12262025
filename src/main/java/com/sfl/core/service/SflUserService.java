package com.sfl.core.service;

import com.sfl.core.domain.SflUser;
import com.sfl.core.service.dto.AdminPasswordChangeDTO;
import com.sfl.core.service.dto.OtpBasicInfoDTO;
import com.sfl.core.service.dto.SflUserDTO;
import com.sfl.core.service.dto.SflUserExtendDTO;
import com.sfl.core.web.rest.system.filter.expressions.Query;
import com.sfl.core.web.rest.vm.CustomMessageVM;
import com.sfl.core.web.rest.vm.ManagedUserVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sfl.core.domain.SflUser}.
 */
public interface SflUserService {

    /**
     * Save a sflUser.
     *
     * @param sflUserDTO the entity to save.
     * @return the persisted entity.
     */
    SflUserDTO save(SflUserDTO sflUserDTO);

    /**
     * Get all the sflUsers.
     *
     * @return the list of entities.
     */
    List<SflUserDTO> findAll();

    /*for filter
        method*/
    Page<SflUserDTO> filter(Query query, Pageable pageable);

    /**
     * Get the "id" sflUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SflUserDTO> findOne(Long id);

    /**
     * Delete the "id" sflUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    SflUser registerUser(ManagedUserVM managedUserVM, String password);

    SflUser createUser(SflUserDTO sflUserDTO);

    void updateUser(String firstName, String lastName, String email,String imageUrl);

    Optional<SflUserDTO> updateUser(SflUserDTO sflUserDTO);

    void deleteUser(String login);

    void changePassword(String currentClearTextPassword, String newPassword);

    Optional<SflUser> getUserWithAuthoritiesByLogin(String login);

    Optional<SflUser> getUserWithAuthorities(Long id);

    Optional<SflUser> getUserWithAuthorities();

    List<String> getAuthorities();

    Optional<SflUser> activateRegistration(String key);

    Optional<SflUser> completePasswordReset(String newPassword, String key);

    Optional<SflUser> requestPasswordReset(String mail);

    boolean doesResetKeyExist(String key);

    /**
     * Get all the sflUsers.
     *
     * @return the page of entities.
     */
    Page<SflUserExtendDTO> findAllSflUsers(Boolean status, String search, Pageable pageable);

    /**
     * find sflUser by email or phoneNumber.
     *
     * @param otpBasicInfoDTO the entity to save.
     * @return Optional<SflUser>
     */
    Optional<SflUser> findByEmailOrPhoneNumber(OtpBasicInfoDTO otpBasicInfoDTO);

    /**
     * check user profile complete or not.
     *
     * @param sflUser the entity to save.
     */
    boolean checkUserProfileComplete(SflUser sflUser);

    /**
     * update sflUsers.
     * @param managedUserVM the entity to save.
     * @param sflUser
     * @return
     */
    SflUser updateSflUser(ManagedUserVM managedUserVM, SflUser sflUser);

    /**
     * method to fetch current user
     *
     * @return Optional<SflUser>
     */
    Optional<SflUser> getCurrentLoggedInUser();

    CustomMessageVM changeUserPasswordByAdmin(AdminPasswordChangeDTO adminPasswordChangeDTO);

    SflUser getCurrentUserByPlatform(String platform, Long userId);

    Optional<SflUser> findByPhoneNumberAndCountryCode(String number, String countryDialCode);

}

