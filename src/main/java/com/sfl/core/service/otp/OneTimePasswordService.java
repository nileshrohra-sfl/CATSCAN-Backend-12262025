package com.sfl.core.service.otp;

import com.sfl.core.service.dto.JwtTokenDTO;
import com.sfl.core.service.dto.OtpSendDTO;
import com.sfl.core.service.dto.OtpVerifyDTO;
import com.sfl.core.service.dto.VerifyOtpDTO;

public interface OneTimePasswordService {

    void sendOTP(OtpSendDTO otpSendDTO);

    JwtTokenDTO verifyOTP(OtpVerifyDTO otpVerifyDTO);

    VerifyOtpDTO otpVerify(OtpVerifyDTO otpVerifyDTO);
}
