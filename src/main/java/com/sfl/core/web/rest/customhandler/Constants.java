package com.sfl.core.web.rest.customhandler;

public class Constants {

    private Constants() {
    }

    // Common code would be in format 1xxx
    public static final int UNAUTHORIZED = 1001;
    public static final int NULL_POINTER_EXCEPTION_CODE = 1002;
    public static final int DATA_ACCESS_EXCEPTION_CODE = 1003;
    public static final int SQL_EXCEPTION_CODE = 1004;
    public static final int SECURITY_USER_NOT_FOUND_EXCEPTION_CODE = 1005;
    public static final int METHOD_ARGUMENT_NOT_VALID_EXCEPTION_CODE = 1006;
    public static final int CONCURRENCY_FAILURE_EXCEPTION_CODE = 1007;
    public static final int INTERNAL_SERVER_ERROR_EXCEPTION_CODE = 1008;
    public static final int AUTHENTICATION_EXCEPTION_CODE = 1009;

    public static final int BAD_REQUEST_EXCEPTION = 1010;

    // Account Resource related code would be in format 2xxx
    public static final int ACCOUNT_RESOURCE_ACTIVATION_KEY_EXCEPTION_CODE = 2001;
    public static final int ACCOUNT_RESOURCE_EMAIL_ALREADY_EXIST_EXCEPTION_CODE = 2002;
    public static final int ACCOUNT_RESOURCE_USER_NOT_FOUND_EXCEPTION_CODE = 2003;
    public static final int ACCOUNT_RESOURCE_INCORRECT_CREDENTIALS_EXCEPTION_CODE = 2004;
    public static final int ACCOUNT_RESOURCE_RESET_KEY_EXCEPTION_CODE = 2005;
    public static final int ACCOUNT_RESOURCE_EMAIL_NOT_FOUND_EXCEPTION_CODE = 2006;
    public static final int ACCOUNT_RESOURCE_USER_NOT_LOGGED_IN_EXCEPTION_CODE = 2007;
    public static final int LOGIN_FAILED_CODE = 2008;
    public static final int SIGN_UP_FAILED_CODE = 2009;
    public static final int USER_IS_ACTIVE_STATUS_CODE = 2010;
    public static final int USER_IS_INACTIVE_STATUS_CODE = 2011;
    public static final int NEW_USER_REGISTER_STATUS_CODE = 2012;
    public static final int VALID_OTP_STATUS_CODE = 2013;
    public static final int ACCOUNT_RESOURCE_INCORRECT_OLD_PASSWORD_EXCEPTION_CODE = 2014;


    // SFL User Resource related code would be in format 3xxx
    public static final int USER_RESOURCE_NEW_USER_CANNOT_HAVE_ID_EXCEPTION_CODE = 3001;
    public static final int USER_RESOURCE_EMAIL_ALREADY_EXIST_EXCEPTION_CODE = 3002;
    public static final int USER_RESOURCE_USER_CANNOT_UPDATE_WITHOUT_ID_EXCEPTION_CODE = 3003;
    public static final int USER_RESOURCE_USER_NAME_ALREADY_EXIST_EXCEPTION_CODE = 3004;
    public static final int USER_CAN_NOT_BE_REGISTERED_EXCEPTION_CODE = 3005;
    public static final int USER_ALREADY_EXIST_EXCEPTION_CODE = 3006;
    public static final int USER_NOT_FOUND_EXCEPTION_CODE = 3007;
    public static final int PHONE_NUMBER_ALREADY_EXISTS_CODE = 3008;

    // Custom OTP CODE
    public static final int INVALID_OTP_ERROR_CODE = 4001;

    // Database related code would be in format 5xxx
    public static final int DATABASE_VERSION_INVALID_EXCEPTION_CODE = 5001;

    // Favorite list related code would be in format 6xxx
    public static final int FAVORITE_NOT_FOUND_EXCEPTION_CODE = 6001;

    // Custom OTP MESSAGE
    public static final String INVALID_OTP_ERROR_MESSAGE = "OTP is invalid";

    // sso login related message
    public static final String SSO_LOGIN_FAILED_MESSAGE = "Login failed. please try again.";
    public static final String ERR_MESSAGE_INVALID_JWT = "Authentication failed, Invalid Jwt.";

    //SSO Resource related code
    public static final int SSO_LOGIN_FAILED_CODE = 4001;

    public static final String ACTIVATION_KEY_EXCEPTION_MESSAGE = "No user was found for this activation key";
    public static final String RESET_KEY_EXCEPTION_MESSAGE = "No user was found for this reset key";
    public static final String EMAIL_ALREADY_EXIST_EXCEPTION_MESSAGE = "Email already exist";

    public static final String PHONE_ALREADY_EXIST_EXCEPTION_MESSAGE = "Phone Number already exist";

    public static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User not found";
    public static final String INCORRECT_CREDENTIALS_EXCEPTION_MESSAGE = "Incorrect Password";
    public static final String INCORRECT_OLD_PASSWORD_EXCEPTION_MESSAGE = "Old Password is wrong";
    public static final String EMAIL_NOT_FOUND_EXCEPTION_MESSAGE = "Email not found";
    public static final String USER_NOT_LOGGED_IN_EXCEPTION_MESSAGE = "User not logged in";
    public static final String NEW_USER_CANNOT_HAVE_ID_EXCEPTION_MESSAGE = "A new sflUser cannot already have an ID";
    public static final String USER_CANNOT_UPDATE_WITHOUT_ID_EXCEPTION_MESSAGE = "User cannot be updated without ID";
    public static final String USER_NAME_ALREADY_EXIST_EXCEPTION_MESSAGE = "Username already exist";
    public static final String USER_CAN_NOT_BE_REGISTERED_EXCEPTION_MESSAGE = "User can not be registered.please try again.";
    public static final String USER_ALREADY_EXIST_EXCEPTION_MESSAGE = "User already exist";
    public static final String SUCCESSFULLY_PASSWORD_CHANGE_EMAIL_SENT_MESSAGE = "Forget password email sent Successfully!";
    public static final String SUCCESSFULLY_REGISTRATION_MESSAGE = "Your registration is successfully done!";
    public static final String LOGIN_FAILED_MESSAGE = "Login failed. please try again.";
    public static final String SIGN_UP_FAILED_MESSAGE ="Sign-up failed. please try again.";
    public static final String OTP_SENT = "OTP sent!";
    public static final String VALID_OTP = "Valid OTP!";
    public static final String PROFILE_UPDATED_SUCCESSFULLY = "Profile Updated Successfully.";
    public static final String PHONE_NUMBER_ALREADY_EXISTS = "Phone number already exists!";
    public static final String AUTHENTICATION_EXCEPTION_MESSAGE = "You are un-authorized or your session has expired. Please login.";
    public static final String SUCCESSFULLY_PASSWORD_CHANGED_BY_ADMIN_EMAIL_SENT_MESSAGE = "New password is changed, sent email Successfully !";
    public static final String BEARER = "Bearer ";
    public static final String EMAIL_ALREADY_EXISTS = "Email address already exists!";
    public static final String FAVORITE_NOT_FOUND_EXCEPTION_MESSAGE = "Favorite product not found";
    public static final String DATABASE_VERSION_INVALID_EXCEPTION_MESSAGE = "Database version is invalid";

}
