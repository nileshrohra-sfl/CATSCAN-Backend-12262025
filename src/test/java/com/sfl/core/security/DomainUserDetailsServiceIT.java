package com.sfl.core.security;

import com.sfl.core.CatScanApp;

import com.sfl.core.domain.SflUser;
import com.sfl.core.repository.SflUserRepository;
import com.sfl.core.service.exception.UserNotActivatedException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Integrations tests for {@link DomainUserDetailsService}.
 */
@SpringBootTest(classes = CatScanApp.class)
@Disabled
@Transactional
public class DomainUserDetailsServiceIT {

    private static final String USER_ONE_LOGIN = "test-user-one";
    private static final String USER_ONE_EMAIL = "test-user-one@localhost";
    private static final String USER_TWO_LOGIN = "test-user-two";
    private static final String USER_TWO_EMAIL = "test-user-two@localhost";
    private static final String USER_THREE_LOGIN = "test-user-three";
    private static final String USER_THREE_EMAIL = "test-user-three@localhost";

    @Autowired
    private SflUserRepository userRepository;

    @Autowired
    private UserDetailsService domainUserDetailsService;

    private SflUser userOne;
    private SflUser userTwo;
    private SflUser userThree;

    @BeforeEach
    public void init() {
        userOne = new SflUser();
        userOne.setUserName(USER_ONE_LOGIN);
        userOne.setPassword(RandomStringUtils.random(60));
        userOne.setStatus(true);
        userOne.setEmail(USER_ONE_EMAIL);
        userOne.setFirstName("userOne");
        userOne.setLastName("doe");
        userRepository.save(userOne);

        userTwo = new SflUser();
        userTwo.setUserName(USER_TWO_LOGIN);
        userTwo.setPassword(RandomStringUtils.random(60));
        userTwo.setStatus(true);
        userTwo.setEmail(USER_TWO_EMAIL);
        userTwo.setFirstName("userTwo");
        userTwo.setLastName("doe");
        userRepository.save(userTwo);

        userThree = new SflUser();
        userThree.setUserName(USER_THREE_LOGIN);
        userThree.setPassword(RandomStringUtils.random(60));
        userThree.setStatus(false);
        userThree.setEmail(USER_THREE_EMAIL);
        userThree.setFirstName("userThree");
        userThree.setLastName("doe");
        userRepository.save(userThree);
    }

    @Test
    public void assertThatUserCanBeFoundByLogin() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_ONE_LOGIN);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_LOGIN);
    }

    @Test
    public void assertThatUserCanBeFoundByLoginIgnoreCase() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_ONE_LOGIN.toUpperCase(Locale.ENGLISH));
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_LOGIN);
    }

    @Test
    public void assertThatUserCanBeFoundByEmail() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_TWO_EMAIL);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(USER_TWO_EMAIL);
    }

    @Test
    public void assertThatUserCanBeFoundByEmailIgnoreCase() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_TWO_EMAIL.toUpperCase(Locale.ENGLISH));
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(USER_TWO_EMAIL);
    }

    @Test
    public void assertThatEmailIsPrioritizedOverLogin() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_ONE_EMAIL);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_EMAIL);
    }

    @Test
    public void assertThatUserNotActivatedExceptionIsThrownForNotActivatedUsers() {
        assertThatExceptionOfType(UserNotActivatedException.class).isThrownBy(
            () -> domainUserDetailsService.loadUserByUsername(USER_THREE_LOGIN));
    }

}
