package com.sfl.core.service.helper;

import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.Date;

@Service
@Transactional
public class AppleLoginHelperService {

    private final Logger log = LoggerFactory.getLogger(AppleLoginHelperService.class);

    /* Generate a private key for token verification from your end with your credentials for apple id login
     * */
    public String generateClientSecret(String appleAppId, String appleKeyId, String appleTeamId, String audienceUrl) {
        log.debug("Request to generate JWT token for apple");
        PrivateKey pKey = generatePrivateKey();
        return Jwts.builder()
            .setHeaderParam(JwsHeader.KEY_ID, appleKeyId)
            .setIssuer(appleTeamId)
            .setAudience(audienceUrl)
            .setSubject(appleAppId)
            .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .signWith(pKey, SignatureAlgorithm.ES256)
            .compact();
    }

    /*  Method to generate private key from certificate you created.
     * Here I have added cert at resource/apple folder.
     * So if you have added somewhere else, just replace it with your path of cert
     */
    private PrivateKey generatePrivateKey() {
        log.debug("Request to generate private key");
        try {
            File file = ResourceUtils.getFile("classpath:apple/apple_cert_dev.p8");
            final PEMParser pemParser = getPemParser(file);
            final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            final PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
            final PrivateKey pKey = converter.getPrivateKey(object);
            pemParser.close();
            return pKey;
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new GlobalException(Constants.SSO_LOGIN_FAILED_MESSAGE, Constants.SSO_LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);
        }
    }

    private PEMParser getPemParser(File file) {
        try {
            return new PEMParser(new FileReader(file));
        } catch (FileNotFoundException e) {
            log.error("Error while creating a client secret.");
            log.error(e.getMessage());
            throw new GlobalException(Constants.SSO_LOGIN_FAILED_MESSAGE, Constants.SSO_LOGIN_FAILED_CODE, HttpStatus.BAD_REQUEST);
        }
    }
}
