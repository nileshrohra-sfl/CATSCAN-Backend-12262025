package com.sfl.core.service.impl;

import com.sfl.core.domain.Authority;
import com.sfl.core.domain.SflUser;
import com.sfl.core.domain.SflUserExtend;
import com.sfl.core.domain.SflUser_;
import com.sfl.core.repository.AuthorityRepository;
import com.sfl.core.repository.SflUserExtendRepository;
import com.sfl.core.repository.SflUserRepository;
import com.sfl.core.security.ApplicationUserDetails;
import com.sfl.core.security.AuthoritiesConstants;
import com.sfl.core.security.SecurityUtils;
import com.sfl.core.service.CountryDialCodeService;
import com.sfl.core.service.SflUserService;
import com.sfl.core.service.dto.*;
import com.sfl.core.service.mapper.SflUserExtendMapper;
import com.sfl.core.service.mapper.SflUserMapper;
import com.sfl.core.service.util.RandomUtil;
import com.sfl.core.util.StringUtil;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import com.sfl.core.web.rest.system.filter.CriteriaKeyValuePair;
import com.sfl.core.web.rest.system.filter.DataType;
import com.sfl.core.web.rest.system.filter.domain.SflUserQuery;
import com.sfl.core.web.rest.system.filter.expressions.ExpressionTreeEvaluator;
import com.sfl.core.web.rest.system.filter.expressions.ExpressionTreeNode;
import com.sfl.core.web.rest.system.filter.expressions.Query;
import com.sfl.core.web.rest.vm.CustomMessageVM;
import com.sfl.core.web.rest.vm.ManagedUserVM;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.sfl.core.repository.constants.CacheConstants.USERS_BY_EMAIL_CACHE;
import static com.sfl.core.repository.constants.CacheConstants.USERS_BY_USER_NAME_CACHE;


/**
 * Service Implementation for managing {@link SflUser}.
 */
@Service
@Transactional
public class SflUserServiceImpl implements SflUserService {

    private final Logger log = LoggerFactory.getLogger(SflUserServiceImpl.class);

    private final SflUserRepository sflUserRepository;
    private final SflUserExtendRepository sflUserExtendRepository;

    private final SflUserMapper sflUserMapper;
    private final SflUserExtendMapper sflUserExtendMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final CountryDialCodeService countryDialCodeService;

    private final MailService mailService;

    private final ExpressionTreeEvaluator<SflUser> evaluator;
    public SflUserServiceImpl(SflUserRepository sflUserRepository, SflUserExtendRepository sflUserExtendRepository, SflUserMapper sflUserMapper, SflUserExtendMapper sflUserExtendMapper, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager, CountryDialCodeService countryDialCodeService, MailService mailService, ExpressionTreeEvaluator<SflUser> evaluator) {
        this.sflUserRepository = sflUserRepository;
        this.sflUserExtendRepository = sflUserExtendRepository;
        this.sflUserMapper = sflUserMapper;
        this.sflUserExtendMapper = sflUserExtendMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.countryDialCodeService = countryDialCodeService;
        this.mailService = mailService;
        this.evaluator = evaluator;
    }

    @Override
    public SflUserDTO save(SflUserDTO sflUserDTO) {
        log.debug("Request to save SflUser : {}", sflUserDTO);
        SflUser sflUser = sflUserMapper.toEntity(sflUserDTO);
        sflUser = sflUserRepository.save(sflUser);
        return sflUserMapper.toDto(sflUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SflUserDTO> findAll() {
        log.debug("Request to get all SflUsers");
        return sflUserRepository.findAll().stream()
            .map(sflUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /*filter method
        implementation*/
    @Override
    public Page<SflUserDTO> filter(Query sflUserQuery, Pageable pageable) {
        log.debug("Request to get filter for Sfl User : {} ", sflUserQuery);
        SflUserQuery query = (SflUserQuery) sflUserQuery;
        ExpressionTreeNode left = new ExpressionTreeNode(query);
        ExpressionTreeNode right = new ExpressionTreeNode(ExpressionTreeNode.Operator.NOOP,
            new CriteriaKeyValuePair[]{new CriteriaKeyValuePair(SflUser_.STATUS, query.getStatus(),
                DataType.BOOLEAN, CriteriaKeyValuePair.Operator.EQUAL)});
        ExpressionTreeNode root = new ExpressionTreeNode(SflUser.class.getSimpleName(), ExpressionTreeNode.Operator.AND,
            left, right, query.getOrderBy());
        Page<SflUser> users = evaluator.execute(root, pageable, Boolean.TRUE, Boolean.TRUE);
        List<SflUserDTO> sflUserDTOList = users.getContent().stream().map(sflUserMapper::toDto).toList();
        return new PageImpl<>(sflUserDTOList, pageable, users.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SflUserDTO> findOne(Long id) {
        log.debug("Request to get SflUser : {}", id);
        return sflUserRepository.findById(id)
            .map(sflUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SflUser : {}", id);
        sflUserRepository.deleteById(id);
    }

    public Optional<SflUser> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return sflUserRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setStatus(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public SflUser registerUser(ManagedUserVM managedUserVM, String password) {
        sflUserRepository.findOneByUserName(managedUserVM.getUserName().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new GlobalException(Constants.USER_NAME_ALREADY_EXIST_EXCEPTION_MESSAGE,
                    Constants.USER_RESOURCE_USER_NAME_ALREADY_EXIST_EXCEPTION_CODE, HttpStatus.BAD_REQUEST);
            }
        });
        sflUserRepository.findOneByEmailIgnoreCase(managedUserVM.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new GlobalException(Constants.EMAIL_ALREADY_EXIST_EXCEPTION_MESSAGE,
                    Constants.USER_RESOURCE_EMAIL_ALREADY_EXIST_EXCEPTION_CODE,
                    HttpStatus.BAD_REQUEST);
            }
        });
        if(Objects.nonNull(managedUserVM.getPhoneNumber())) {
            sflUserRepository.findOneByPhoneNumber(managedUserVM.getPhoneNumber()).ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new GlobalException(Constants.PHONE_ALREADY_EXIST_EXCEPTION_MESSAGE,
                        Constants.USER_RESOURCE_EMAIL_ALREADY_EXIST_EXCEPTION_CODE,
                        HttpStatus.BAD_REQUEST);
                }
            });
        }
        SflUser newUser = new SflUser();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setUserName(managedUserVM.getUserName().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(managedUserVM.getFirstName());
        newUser.setLastName(managedUserVM.getLastName());
        newUser.setEmail(managedUserVM.getEmail().toLowerCase());
        newUser.setImageUrl(managedUserVM.getImageUrl());
        newUser.setPhoneNumber(managedUserVM.getPhoneNumber());
        // new user is not active
        newUser.setStatus(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findByName(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        sflUserRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(SflUser existingUser) {
        if (Boolean.TRUE.equals(existingUser.getStatus())) {
            return false;
        }
        sflUserRepository.delete(existingUser);
        sflUserRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public SflUser createUser(SflUserDTO sflUserDTO) {
        SflUser user = new SflUser();
        user.setUserName(sflUserDTO.getUserName().toLowerCase());
        user.setFirstName(sflUserDTO.getFirstName());
        user.setLastName(sflUserDTO.getLastName());
        user.setEmail(sflUserDTO.getEmail().toLowerCase());
        user.setImageUrl(sflUserDTO.getImageUrl());
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setStatus(true);
        if (sflUserDTO.getAuthorities() != null) {
            Set<Authority> authorities = sflUserDTO.getAuthorities().stream()
                .map(authorityRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        sflUserRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String imageUrl) {
        Optional<String> optionalLogin = SecurityUtils.getCurrentUserLogin();
        if (optionalLogin.isPresent()) {
            Optional<SflUser> optionalSflUser = userByEmailOrUsername( optionalLogin.get());
            optionalSflUser.ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email.toLowerCase());
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
        }
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param sflUserDTO user to update.
     * @return updated user.
     */
    public Optional<SflUserDTO> updateUser(SflUserDTO sflUserDTO) {
        return Optional.of(sflUserRepository
            .findById(sflUserDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setUserName(sflUserDTO.getUserName().toLowerCase());
                user.setFirstName(sflUserDTO.getFirstName());
                user.setLastName(sflUserDTO.getLastName());
                user.setEmail(sflUserDTO.getEmail().toLowerCase());
                user.setImageUrl(sflUserDTO.getImageUrl());
                user.setStatus(sflUserDTO.getStatus());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                sflUserDTO.getAuthorities().stream()
                    .map(authorityRepository::findByName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(SflUserDTO::new);
    }

    public void deleteUser(String login) {
        sflUserRepository.findOneByEmailIgnoreCase(login).ifPresent(user -> {
            sflUserRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        Optional<String> optionalLogin = SecurityUtils.getCurrentUserLogin();
        if (optionalLogin.isPresent()) {
            Optional<SflUser> optionalSflUser = userByEmailOrUsername( optionalLogin.get());
            optionalSflUser.ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new GlobalException(Constants.INCORRECT_OLD_PASSWORD_EXCEPTION_MESSAGE,
                        Constants.ACCOUNT_RESOURCE_INCORRECT_OLD_PASSWORD_EXCEPTION_CODE,
                        HttpStatus.BAD_REQUEST);
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
        }
    }


    @Transactional(readOnly = true)
    public Optional<SflUser> getUserWithAuthoritiesByLogin(String login) {
        return userByEmailOrUsername(login);
    }

    @Transactional(readOnly = true)
    public Optional<SflUser> getUserWithAuthorities(Long id) {
        return sflUserRepository.findOneWithAuthoritiesById(id);
    }

//    @Transactional(readOnly = true)
//    public Optional<SflUser> getUserWithAuthorities() {
//        Optional<String> optionalLogin = SecurityUtils.getCurrentUserLogin();
//        if(optionalLogin.isPresent()) {
//            String login = optionalLogin.get();
//            if(login.matches("^[^a-zA-Z]*$")){
//                return sflUserRepository.findOneByPhoneNumber(Long.parseLong(login));
//            }
//            return userByEmailOrUsername(optionalLogin.get());
//        } else {
//            return Optional.empty();
//        }
//    }

    @Transactional(readOnly = true)
    public Optional<SflUser> getUserWithAuthorities() {
        Optional<ApplicationUserDetails> optionalLogin= SecurityUtils.getCurrentUserDetail();
        if(optionalLogin.isPresent()) {
            ApplicationUserDetails login = optionalLogin.get();
            return sflUserRepository.findById(login.getId().longValue());
        } else {
            return Optional.empty();
        }
    }


    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).toList();
    }

    public Optional<SflUser> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return sflUserRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<SflUser> requestPasswordReset(String mail) {
        return sflUserRepository.findOneByEmailIgnoreCaseAndStatus(mail, true)
            .filter(SflUser::getStatus)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<SflUser> userByEmailOrUsername(String login) {
        if (new EmailValidator().isValid(login, null)) {
            return sflUserRepository.findOneByEmailIgnoreCase(login);
        } else {
            return sflUserRepository.findOneByUserName(login);
        }
    }

    public Optional<SflUser> getUserByPhoneNumber(Long number){
        return sflUserRepository.findOneByPhoneNumber(number);
    }

    private void clearUserCaches(SflUser user) {
        Objects.requireNonNull(cacheManager.getCache(USERS_BY_USER_NAME_CACHE)).evict(user.getUserName());
        Objects.requireNonNull(cacheManager.getCache(USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
    }

    public SflUser createNewUserForSsoLogin(SocialLoginDTO ssoLoginDTO) {
        SflUser user = new SflUser();
        user.setStatus(true);
        user.setUserName(ssoLoginDTO.getName());
        user.setEmail(ssoLoginDTO.getEmail());
        return createUser(user);
    }

    public Optional<SflUser> getUserByEmail(String email){
        return sflUserRepository.findOneByEmailIgnoreCase(email);
    }

    public Optional<SflUser> getUserByPhoneNumber(String number, String countryDialCode){
        return sflUserRepository.findOneByPhoneNumberAndCountryDialCode(StringUtil.getLong(number), countryDialCodeService.findByDialCode(countryDialCode));
    }

    public Optional<SflUser> findByPhoneNumberAndCountryCode(String number, String countryDialCode){
        return sflUserRepository.findByPhoneNumberAndCountryCode(StringUtil.getLong(number), countryDialCodeService.findByDialCode(countryDialCode));
    }

    public SflUser createNewUserForOtpLogin(OtpVerifyDTO otpVerifyDTO) {
        SflUser user = new SflUser();
        user.setStatus(true);
        switch (otpVerifyDTO.getChannel()){
            case EMAIL:
                user.setEmail(otpVerifyDTO.getTo());
                break;
            case SMS:
                user.setPhoneNumber(StringUtil.getLong(otpVerifyDTO.getTo()));
                user.setCountryDialCode(countryDialCodeService.findByDialCode(otpVerifyDTO.getCountryDialCode()));
                break;
            default: break;
        }
        return createUser(user);
    }

    private SflUser createUser(SflUser user){
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.getByName(AuthoritiesConstants.USER));
        user.setAuthorities(authorities);
        try {
            return sflUserExtendRepository.save(new SflUserExtend(user));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new GlobalException(Constants.USER_CAN_NOT_BE_REGISTERED_EXCEPTION_MESSAGE, Constants.USER_CAN_NOT_BE_REGISTERED_EXCEPTION_CODE, HttpStatus.BAD_REQUEST);
        }
    }

    public boolean doesResetKeyExist(String key){
        return sflUserRepository.existsByResetKey(key);
    }

    /**
     * method to fetch all sflUsers
     *
     * @param pageable the pagination information.
     * @return Page<SflUserDTO>
     */
    @Override
    public Page<SflUserExtendDTO> findAllSflUsers(Boolean status, String search, Pageable pageable) {
        log.debug("Request to get all sflUsers");
            return sflUserExtendRepository.findAllBySearchKey(search, pageable)
                .map(sflUserExtendMapper::toDto);
    }

    /**
     * find sflUser by email or phoneNumber.
     *
     * @param otpSendDTO the entity to save.
     * @return Optional<SflUser>
     */
    public Optional<SflUser> findByEmailOrPhoneNumber(OtpBasicInfoDTO otpSendDTO) {
        log.debug("Find sflUser by email or phoneNumber");
        if (new EmailValidator().isValid(otpSendDTO.getTo(), null)) {
            return sflUserRepository.findOneByEmailIgnoreCase(otpSendDTO.getTo());
        } else {
            return sflUserRepository.findOneByPhoneNumberAndCountryDialCode(StringUtil.getLong(otpSendDTO.getTo()), countryDialCodeService.findByDialCode(otpSendDTO.getCountryDialCode()));
        }
    }

    /**
     * check user profile complete or not.
     *
     * @param sflUser the entity to save.
     */
    public boolean checkUserProfileComplete(SflUser sflUser) {
        return Objects.nonNull(sflUser.getPhoneNumber()) && Objects.nonNull(sflUser.getEmail()) && !sflUser.getEmail().isEmpty();
    }

    @Override
    public SflUser updateSflUser(ManagedUserVM managedUserVM, SflUser sflUser) {
        log.debug("Request to update SflUser : {}", managedUserVM);
            if(!StringUtils.equals(sflUser.getEmail(), managedUserVM.getEmail())) {
                this.validateEmail(managedUserVM.getEmail());
            }
            if(!Objects.equals(sflUser.getPhoneNumber(),managedUserVM.getPhoneNumber())) {
                this.validatePhoneNumber(managedUserVM.getPhoneNumber());
            }
            sflUser.setFirstName(managedUserVM.getFirstName());
            sflUser.setLastName(managedUserVM.getLastName());
            sflUser.setImageUrl(managedUserVM.getImageUrl());
            sflUser.setEmail(managedUserVM.getEmail());
            if(!Objects.equals(sflUser.getCountryDialCode(),managedUserVM.getCountryDialCode()) && managedUserVM.getCountryDialCode() != null) {
                sflUser.setCountryDialCode(countryDialCodeService.findByDialCode(managedUserVM.getCountryDialCode().getDialCode()));
            }
            if(!Objects.equals(sflUser.getPhoneNumber(),managedUserVM.getPhoneNumber()) && managedUserVM.getPhoneNumber() != null) {
                sflUser.setPhoneNumber(managedUserVM.getPhoneNumber());
            }
            if (Objects.nonNull(managedUserVM.getPassword()) && !managedUserVM.getPassword().isEmpty()) {
                sflUser.setPassword(passwordEncoder.encode(managedUserVM.getPassword()));
            }
            return sflUserRepository.save(sflUser);
    }

    /**
     * method to fetch current user
     *
     * @return Optional<SflUser>
     */
    public Optional<SflUser> getCurrentLoggedInUser() {
        String login = SecurityUtils.getUserLogin();
        log.debug("Request to get current user by login:{}", login);
        return Optional.ofNullable(userByEmailOrPhoneNumber(login)
            .orElseThrow(() -> new GlobalException(Constants.USER_NOT_FOUND_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_USER_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND)));
    }

    /**
     * method to fetch current user by email or phoneNumber
     *
     * @return Optional<SflUser>
     */
    public Optional<SflUser> userByEmailOrPhoneNumber(String login) {
        if (new EmailValidator().isValid(login, null)) {
            return sflUserRepository.findOneByEmailIgnoreCase(login);
        } else {
            return sflUserRepository.findOneByPhoneNumber(StringUtil.getLong(login));
        }
    }

    /**
     * verify and update user phoneNumber.
     *
     * @param phoneNumber the entity to save.
     */
    private void validatePhoneNumber(Long phoneNumber) {
        if (Objects.nonNull(phoneNumber)) {
            sflUserRepository.findOneByPhoneNumber(phoneNumber).ifPresent(existingUser -> {
                if (!removeNonActivatedUser(existingUser)) {
                    throw new GlobalException(Constants.PHONE_NUMBER_ALREADY_EXISTS,
                        Constants.PHONE_NUMBER_ALREADY_EXISTS_CODE, HttpStatus.BAD_REQUEST);
                }
            });
        }
    }

    /**
     * verify and update user email.
     *
     * @param email the entity to save.
     */
    private void validateEmail(String email) {
       if (StringUtils.isNotBlank(email)) {
            sflUserRepository.findOneByEmailIgnoreCase(email).ifPresent(existingUser -> {
                if (!removeNonActivatedUser(existingUser)) {
                    throw new GlobalException(Constants.EMAIL_ALREADY_EXIST_EXCEPTION_MESSAGE,
                        Constants.USER_RESOURCE_EMAIL_ALREADY_EXIST_EXCEPTION_CODE, HttpStatus.BAD_REQUEST);
                }
            });
        }
    }

    /**
     * change password of sfl user by admin
     *
     * @param sflUser sfl user.
     * @param newPassword new password of sfl user
     */
    private void updatePasswordByAdmin(SflUser sflUser, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        sflUser.setPassword(encodedPassword);
        sflUserRepository.save(sflUser);
        this.clearUserCaches(sflUser);
    }

    /**
     * change password of sfluser by admin without knowing old password.
     *
     * @param adminPasswordChangeDTO having sfl-user-id and new-password.
     */
    @Override
    public CustomMessageVM changeUserPasswordByAdmin(AdminPasswordChangeDTO adminPasswordChangeDTO) {
        Optional<SflUser> user = sflUserRepository.findById(adminPasswordChangeDTO.getUserId());
        log.debug("Requested to change the password of {}", user);

        user.ifPresentOrElse(
            u -> {
                updatePasswordByAdmin(u, adminPasswordChangeDTO.getNewPassword());
                mailService.sendNewPasswordToRespectedUserByAdmin(u, adminPasswordChangeDTO.getNewPassword());
            },
            () -> {
                throw new GlobalException(Constants.USER_NOT_FOUND_EXCEPTION_MESSAGE,
                    Constants.USER_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND);
            }
        );

        return new CustomMessageVM(Constants.SUCCESSFULLY_PASSWORD_CHANGED_BY_ADMIN_EMAIL_SENT_MESSAGE, HttpStatus.OK.value());
    }

    @Override
    public SflUser getCurrentUserByPlatform(String platform, Long userId) {
        Optional<SflUser> sflUserOptional;
        if (platform.toLowerCase().equals(com.sfl.core.config.Constants.MOBILE_USER_AGENT) && Objects.nonNull(userId)) {
            sflUserOptional = sflUserRepository.findById(userId);
        } else {
            sflUserOptional = getUserWithAuthorities();
        }
        return sflUserOptional.orElseThrow(() -> new GlobalException(Constants.USER_NOT_FOUND_EXCEPTION_MESSAGE,
            Constants.ACCOUNT_RESOURCE_USER_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND));
    }


}
