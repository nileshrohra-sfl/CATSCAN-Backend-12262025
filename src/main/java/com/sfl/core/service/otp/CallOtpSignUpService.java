package com.sfl.core.service.otp;

import com.sfl.core.config.otp.OtpLoginProperties;
import com.sfl.core.security.jwt.TokenProvider;
import com.sfl.core.service.impl.SflUserServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/** Right now to send an OTP on call and on message has the same implementation so that's why I have extend SmsOtpSignUpService class here.
 *  I have created new class for call to decouple call and sms module so in future if there is need to change code either in sms
 *  or in call we can easily make those changes.
 */
//@Service(value = "callSignUp")
//@Transactional
//public class CallOtpSignUpService  extends SmsOtpSignUpService{
//    public CallOtpSignUpService(SflUserServiceImpl sflUserService, SendOtpService otpService, TokenProvider tokenProvider, OtpLoginProperties otpLoginProperties) {
//        super(sflUserService, otpService, tokenProvider, otpLoginProperties);
//    }
//}
