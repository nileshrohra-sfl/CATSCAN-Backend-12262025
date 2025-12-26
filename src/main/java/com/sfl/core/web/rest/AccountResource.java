package com.sfl.core.web.rest;


import com.sfl.core.domain.SflUser;
import com.sfl.core.repository.SflUserRepository;
import com.sfl.core.security.SecurityUtils;
import com.sfl.core.security.jwt.TokenProvider;
import com.sfl.core.service.SflUserExtendService;
import com.sfl.core.service.SflUserService;
import com.sfl.core.service.dto.*;
import com.sfl.core.service.impl.MailService;
import com.sfl.core.service.otp.OtpLoginService;
import com.sfl.core.service.otp.OtpSignUpService;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import com.sfl.core.web.rest.vm.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * REST controller for managing the current user's account.
 */

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "javabaseproject")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final SflUserRepository sflUserRepository;

    private final SflUserService sflUserService;

    private final MailService mailService;

    private final OtpLoginService otpLoginService;

    private final OtpSignUpService otpSignUpService;

    private final TokenProvider tokenProvider;

    private final SflUserExtendService sflUserExtendService;

    public AccountResource(SflUserRepository sflUserRepository, SflUserService sflUserService, MailService mailService, OtpLoginService otpLoginService, OtpSignUpService otpSignUpService, TokenProvider tokenProvider, SflUserExtendService sflUserExtendService) {
        this.sflUserRepository = sflUserRepository;
        this.sflUserService = sflUserService;
        this.mailService = mailService;
        this.otpLoginService = otpLoginService;
        this.otpSignUpService = otpSignUpService;
        this.tokenProvider = tokenProvider;
        this.sflUserExtendService = sflUserExtendService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws GlobalException  {@code 400 (Bad Request)} if the password is incorrect.
     * @throws GlobalException {@code 400 (Bad Request)} if the email is already used.
     * @throws GlobalException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomMessageVM registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new GlobalException(Constants.INCORRECT_CREDENTIALS_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_INCORRECT_CREDENTIALS_EXCEPTION_CODE, HttpStatus.BAD_REQUEST);
        }
        SflUser sflUser = sflUserService.registerUser(managedUserVM, managedUserVM.getPassword());
        mailService.sendActivationEmail(sflUser);
        return new CustomMessageVM(Constants.SUCCESSFULLY_REGISTRATION_MESSAGE, HttpStatus.OK.value());
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws GlobalException {@code 404 (Not Found)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<SflUser> user = sflUserService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new GlobalException(Constants.ACTIVATION_KEY_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_ACTIVATION_KEY_EXCEPTION_CODE, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws GlobalException {@code 404 (Not Found)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public SflUserExtendDTO getAccount() {
        Optional<SflUser> sflUserOptional = sflUserService.getUserWithAuthorities();
        if (sflUserOptional.isPresent()) {
            SflUser sflUser = sflUserOptional.get();
            String dialCode = sflUser.getCountryDialCode().getDialCode();
            SflUserExtendDTO user = new SflUserExtendDTO(sflUser);
            Optional<SflUserExtendDTO> extendUsers = sflUserExtendService.findOne(user.getId());
            extendUsers.ifPresent(extendUser -> {
                if (Objects.nonNull(extendUser.getAllergens())) {
                    user.setAllergens(extendUser.getAllergens());
                }
            });
            user.setPhoneWithCountryCode(dialCode+user.getPhoneNumber());
            return user;
        }
        throw new GlobalException(Constants.USER_NOT_FOUND_EXCEPTION_MESSAGE,
            Constants.ACCOUNT_RESOURCE_USER_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND);
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws GlobalException {@code 404 (Not Found)} if the user couldn't be returned.
     */
    @GetMapping("/current-user-account")
    public SflUserDTO getCurrentUserAccount(HttpServletRequest request) {
        Optional<SflUser> sflUserOptional = sflUserService.getUserWithAuthorities();
        return new SflUserDTO(sflUserOptional.orElseThrow(() -> new GlobalException(Constants.USER_NOT_FOUND_EXCEPTION_MESSAGE,
            Constants.ACCOUNT_RESOURCE_USER_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND)));
    }
    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws GlobalException {@code 400 (Bad Request)} if the email is already used.
     * @throws GlobalException {@code 404 (Not Found)} if the user wasn't found.
     * @throws GlobalException {@code 403 (Forbidden)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody SflUserDTO userDTO) {
        Optional<String> userLoginOptional = SecurityUtils.getCurrentUserLogin();
        String userLogin = userLoginOptional.orElseThrow(() -> new GlobalException(Constants.USER_NOT_LOGGED_IN_EXCEPTION_MESSAGE,
            Constants.ACCOUNT_RESOURCE_USER_NOT_LOGGED_IN_EXCEPTION_CODE, HttpStatus.FORBIDDEN));

        Optional<SflUser> existingUser = sflUserRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getEmail().equalsIgnoreCase(userLogin))) {
            throw new GlobalException(Constants.EMAIL_ALREADY_EXIST_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_EMAIL_ALREADY_EXIST_EXCEPTION_CODE, HttpStatus.BAD_REQUEST);
        }
        Optional<SflUser> user = sflUserRepository.findOneByEmailIgnoreCase(userLogin);
        if (user.isEmpty()) {
            throw new GlobalException(Constants.USER_NOT_FOUND_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_USER_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND);
        }
        sflUserService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getImageUrl());
    }


    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws GlobalException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new GlobalException(Constants.INCORRECT_CREDENTIALS_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_INCORRECT_CREDENTIALS_EXCEPTION_CODE, HttpStatus.BAD_REQUEST);
        }
        sflUserService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param passwordResetDTO the DTO which will contain email of the user.
     * @throws GlobalException {@code 404 (Not Found)} if the email address is not registered.
     */
    @PostMapping(path = "/account/reset-password/init")
    public CustomMessageVM requestPasswordReset(@RequestBody PasswordResetDTO passwordResetDTO) {
        Optional<SflUser> sflUserOptional = sflUserService.requestPasswordReset(passwordResetDTO.getEmail());
        mailService.sendPasswordResetMail(sflUserOptional.orElseThrow(() ->
            new GlobalException(Constants.EMAIL_NOT_FOUND_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_EMAIL_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND)));
        return new CustomMessageVM(Constants.SUCCESSFULLY_PASSWORD_CHANGE_EMAIL_SENT_MESSAGE, HttpStatus.OK.value());
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     * <p>
     * //     * @param keyAndPassword the generated key and the new password.
     *
     * @throws GlobalException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws GlobalException {@code 404 (Not Found)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public ResponseEntity<Void> finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new GlobalException(Constants.INCORRECT_CREDENTIALS_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_INCORRECT_CREDENTIALS_EXCEPTION_CODE, HttpStatus.BAD_REQUEST);
        }
        Optional<SflUser> user =
            sflUserService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (user.isEmpty()) {
            throw new GlobalException(Constants.RESET_KEY_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_RESET_KEY_EXCEPTION_CODE, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().build();
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }

    /** API is used to check user exist or not and if user not exist then send an otp to email, sms or on call to user.
     * {@code POST  /account/register/init} : Send an OTP to the user on given channel.
     *
     * @param otpSendDTO the DTO which will contain channel which could be SMS, EMAIL, or CALL and user email or phone number.
     * @throws GlobalException {@code 400 (Bad Request)} if user is already exist.
     */
    @PostMapping("/account/register/init")
    public void createAccount(@Valid @RequestBody OtpSendDTO otpSendDTO) {
        log.debug("Rest request to initialize user registration");
        otpSignUpService.sendOTP(otpSendDTO);
    }

    /** API is used to Verify the user provided OTP which was sent on email, sms or on call and if user provided otp if it's
     *  correct then create new user and return jwt token.
     * {@code POST  /account/register/finish} : Verify an OTP that was sent to user.
     *
     * @param otpVerifyDTO the DTO which will contain OTp.
     * @throws GlobalException {@code 400 (Bad Request)} if user provided OTP is invalid.
     * @return the JWT token.
     */
    @PostMapping("/account/register/finish")
    public JwtTokenDTO createNewUser(@Valid @RequestBody OtpVerifyDTO otpVerifyDTO) {
        log.debug("Rest request to complete user registration");
        return otpSignUpService.verifyOTP(otpVerifyDTO);
    }

    /** API is used to check user exist or not and if user exist then send an otp to email, sms or on call to user.
     * {@code POST  /account/login/init} : Send an OTP to the user on given channel.
     *
     * @param otpSendDTO the DTO which will contain channel which could be SMS, EMAIL, or CALL and user email or phone number.
     * @throws GlobalException {@code 404 (Not Found)} if user  not exist.
     */
    @PostMapping("/account/login/init")
    public SendOtpResponseDTO login(@RequestBody OtpSendDTO otpSendDTO) {
        log.debug("Rest request to initialize user login");
        otpLoginService.sendOTP(otpSendDTO);
        return new SendOtpResponseDTO("OTP sent successfully to your mobile number: "+otpSendDTO.getTo());
    }

    /** API is used to Verify the user provided OTP which was sent on email, sms or on call and if user provided otp if it's
     *  correct then return jwt token.
     * {@code POST  /account/login/finish} : Verify an OTP that was sent to user.
     *
     * @param otpVerifyDTO the DTO which will contain OTp.
     * @throws GlobalException {@code 400 (Bad Request)} if user provided OTP is invalid.
     * @return the JWT token if OTP is matched............................................
     */
    @PostMapping("/account/login/finish")
    public JwtTokenDTO authenticateUser(@RequestBody OtpVerifyDTO otpVerifyDTO) {
        log.debug("Rest request to complete user login");
        return otpLoginService.verifyOTP(otpVerifyDTO);
    }

    /**
     * API is used to check user exist or not and if user not exist then send an otp to email, sms or on call to user.
     * {@code POST  /account/register/init} : Send an OTP to the user on given channel.
     *
     * @param otpSendDTO the DTO which will contain channel which could be SMS, EMAIL, or CALL and user email or phone number.
     * @throws GlobalException {@code 400 (Bad Request)} if user is already exist.
     */
    @PostMapping("/register/init")
    public RegisterCustomMessageVM createUserAccount(@Valid @RequestBody OtpSendDTO otpSendDTO) {
        log.debug("Rest request to initialize user registration");
        Optional<SflUser> sflUserOptional = sflUserService.findByEmailOrPhoneNumber(otpSendDTO);
        if (sflUserOptional.isPresent()) {
            if (sflUserOptional.get().getStatus().equals(false)) {
                otpLoginService.sendOTP(otpSendDTO);
                return new RegisterCustomMessageVM(Constants.OTP_SENT, HttpStatus.OK.value(), Constants.USER_IS_INACTIVE_STATUS_CODE, sflUserOptional.get().getStatus(), sflUserOptional.get().getId());
            } else {
                otpLoginService.sendOTP(otpSendDTO);
                return new RegisterCustomMessageVM(Constants.OTP_SENT, HttpStatus.OK.value(), Constants.USER_IS_ACTIVE_STATUS_CODE, sflUserOptional.get().getStatus(), sflUserOptional.get().getId());
            }
        } else {
            otpSignUpService.sendOTP(otpSendDTO);
            return new RegisterCustomMessageVM(Constants.OTP_SENT, HttpStatus.OK.value(), Constants.NEW_USER_REGISTER_STATUS_CODE, false, null);
        }
    }

    /**
     * API is used to Verify the user provided OTP which was sent on email, sms or on call and if user provided otp if it's
     * correct then create new user and return jwt token.
     * {@code POST  /account/register/finish} : Verify an OTP that was sent to user.
     *
     * @param otpVerifyDTO the DTO which will contain OTp.
     * @return the JWT token.
     * @throws GlobalException {@code 400 (Bad Request)} if user provided OTP is invalid.
     */
    @PostMapping("/register/finish")
    public VerifyCustomMessageVM verifyAndRegisterUser(@Valid @RequestBody OtpVerifyDTO otpVerifyDTO) {
        log.debug("Rest request to complete user registration");
        Optional<SflUser> sflUserOptional = sflUserService.findByEmailOrPhoneNumber(otpVerifyDTO);
        if (sflUserOptional.isPresent()) {
            // if user is present then it will be able to login
            VerifyOtpDTO verifyOtpDTO = otpLoginService.otpVerify(otpVerifyDTO);
            boolean isProfileCompleted = sflUserService.checkUserProfileComplete(sflUserOptional.get());
            return new VerifyCustomMessageVM(sflUserOptional.get().getId(),Constants.VALID_OTP, verifyOtpDTO.getAccessToken(), verifyOtpDTO.getRefreshToken(), isProfileCompleted, Constants.VALID_OTP_STATUS_CODE);
        }
        // if user is not present then it will create new user
        VerifyOtpDTO verifyOtpDTO = otpSignUpService.otpVerify(otpVerifyDTO);
        return new VerifyCustomMessageVM(verifyOtpDTO.getId(), Constants.VALID_OTP, verifyOtpDTO.getAccessToken(), verifyOtpDTO.getRefreshToken(), false, Constants.VALID_OTP_STATUS_CODE);
    }

    /**
     * {@code PUT  /account} : Updates an existing sflUser.
     *
     * @param managedUserVM the sflUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sflUserDTO,
     * or with status {@code 400 (Bad Request)} if the sflUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sflUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account")
    public CustomMessageVM updateAccount(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
        log.debug("REST request to update SflUserExtend : {}", managedUserVM);
        Optional<SflUser> userOptional = sflUserService.getCurrentLoggedInUser();

        return userOptional
            .map(user -> {
                sflUserService.updateSflUser(managedUserVM, user);
                JwtTokenDTO tokens = tokenProvider.generateMobileJwtToken(user);
                return new CustomMessageVM(Constants.PROFILE_UPDATED_SUCCESSFULLY, tokens.getAccessToken(), tokens.getRefreshToken());
            })
            .orElseThrow(() -> new GlobalException(Constants.USER_NOT_FOUND_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_USER_NOT_FOUND_EXCEPTION_CODE, HttpStatus.NOT_FOUND));
    }


    /**
     * {@code GET  /account/reset-password/finish-page} : get the boolean value based on key.
     *
     * @return the boolean value based on key.
     */
    @GetMapping("/account/reset-password/finish-page")
    public String resetPasswordPage(@RequestParam(name = "key") String key, HttpServletResponse response) {
        log.debug("Request to account reset");
        if (sflUserService.doesResetKeyExist(key)) {
            return "password-reset";
        } else {
            return "invalid-reset-key";
        }
    }


    /**
     * {@code POST   /admin/account/change-password} : Change the password of the user by admin.
     * <p>
     * //     * @param AdminChangePasswordDTO the user-id and the new password.
     *
     * @throws GlobalException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws GlobalException {@code 404 (Not Found)} if the user not found.
     */

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/account/change-password")
    public CustomMessageVM changePasswordByAdmin(@RequestBody AdminPasswordChangeDTO adminPasswordChangeDTO){
        if (!checkPasswordLength(adminPasswordChangeDTO.getNewPassword())) {
            throw new GlobalException(Constants.INCORRECT_CREDENTIALS_EXCEPTION_MESSAGE,
                Constants.ACCOUNT_RESOURCE_INCORRECT_CREDENTIALS_EXCEPTION_CODE, HttpStatus.BAD_REQUEST);
        }
        return sflUserService.changeUserPasswordByAdmin(adminPasswordChangeDTO);
    }
}
