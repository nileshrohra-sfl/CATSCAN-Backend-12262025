package com.sfl.core.web.rest;

import com.sfl.core.CatScanApp;
import com.sfl.core.domain.SflUser;
import com.sfl.core.repository.SflUserRepository;
import com.sfl.core.service.SflUserService;
import com.sfl.core.service.dto.SflUserDTO;
import com.sfl.core.service.mapper.SflUserMapper;
import com.sfl.core.web.rest.customhandler.CustomExceptionHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sfl.core.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SflUserResource} REST controller.
 */
@SpringBootTest(classes = CatScanApp.class)
@Disabled
public class SflUserResourceIT {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_PHONE_NUMBER = 1L;
    private static final Long UPDATED_PHONE_NUMBER = 2L;

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVATION_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVATION_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    @Autowired
    private SflUserRepository sflUserRepository;

    @Autowired
    private SflUserMapper sflUserMapper;

    @Autowired
    private SflUserService sflUserService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private CustomExceptionHandler exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSflUserMockMvc;

    private SflUser sflUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SflUserResource sflUserResource = new SflUserResource(sflUserService);
        this.restSflUserMockMvc = MockMvcBuilders.standaloneSetup(sflUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SflUser createEntity(EntityManager em) {
        SflUser sflUser = new SflUser()
            .userName(DEFAULT_USER_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .password(DEFAULT_PASSWORD)
            .imageUrl(DEFAULT_IMAGE_URL)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .activationKey(DEFAULT_ACTIVATION_KEY)
            .status(DEFAULT_STATUS);
        return sflUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SflUser createUpdatedEntity(EntityManager em) {
        return new SflUser()
            .userName(UPDATED_USER_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .password(UPDATED_PASSWORD)
            .imageUrl(UPDATED_IMAGE_URL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        sflUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createSflUser() throws Exception {
        int databaseSizeBeforeCreate = sflUserRepository.findAll().size();

        // Create the SflUser
        SflUserDTO sflUserDTO = sflUserMapper.toDto(sflUser);
        restSflUserMockMvc.perform(post("/api/sfl-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sflUserDTO)))
            .andExpect(status().isCreated());

        // Validate the SflUser in the database
        List<SflUser> sflUserList = sflUserRepository.findAll();
        assertThat(sflUserList).hasSize(databaseSizeBeforeCreate + 1);
        SflUser testSflUser = sflUserList.get(sflUserList.size() - 1);
        assertThat(testSflUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testSflUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSflUser.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testSflUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testSflUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    public void createSflUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sflUserRepository.findAll().size();

        // Create the SflUser with an existing ID
        sflUser.setId(1L);
        SflUserDTO sflUserDTO = sflUserMapper.toDto(sflUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSflUserMockMvc.perform(post("/api/sfl-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sflUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SflUser in the database
        List<SflUser> sflUserList = sflUserRepository.findAll();
        assertThat(sflUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSflUsers() throws Exception {
        // Initialize the database
        sflUserRepository.saveAndFlush(sflUser);

        // Get all the sflUserList
        restSflUserMockMvc.perform(get("/api/sfl-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sflUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].activationKey").value(hasItem(DEFAULT_ACTIVATION_KEY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getSflUser() throws Exception {
        // Initialize the database
        sflUserRepository.saveAndFlush(sflUser);

        // Get the sflUser
        restSflUserMockMvc.perform(get("/api/sfl-users/{id}", sflUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sflUser.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.intValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.activationKey").value(DEFAULT_ACTIVATION_KEY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSflUser() throws Exception {
        // Get the sflUser
        restSflUserMockMvc.perform(get("/api/sfl-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSflUser() throws Exception {
        // Initialize the database
        sflUserRepository.saveAndFlush(sflUser);

        int databaseSizeBeforeUpdate = sflUserRepository.findAll().size();

        // Update the sflUser
        SflUser updatedSflUser = sflUserRepository.findById(sflUser.getId()).get();
        // Disconnect from session so that the updates on updatedSflUser are not directly saved in db
        em.detach(updatedSflUser);
        updatedSflUser
            .userName(UPDATED_USER_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .password(UPDATED_PASSWORD)
            .imageUrl(UPDATED_IMAGE_URL)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .activationKey(UPDATED_ACTIVATION_KEY)
            .status(UPDATED_STATUS);
        SflUserDTO sflUserDTO = sflUserMapper.toDto(updatedSflUser);

        restSflUserMockMvc.perform(put("/api/sfl-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sflUserDTO)))
            .andExpect(status().isOk());

        // Validate the SflUser in the database
        List<SflUser> sflUserList = sflUserRepository.findAll();
        assertThat(sflUserList).hasSize(databaseSizeBeforeUpdate);
        SflUser testSflUser = sflUserList.get(sflUserList.size() - 1);
        assertThat(testSflUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testSflUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSflUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testSflUser.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingSflUser() throws Exception {
        int databaseSizeBeforeUpdate = sflUserRepository.findAll().size();

        // Create the SflUser
        SflUserDTO sflUserDTO = sflUserMapper.toDto(sflUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSflUserMockMvc.perform(put("/api/sfl-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sflUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SflUser in the database
        List<SflUser> sflUserList = sflUserRepository.findAll();
        assertThat(sflUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSflUser() throws Exception {
        // Initialize the database
        sflUserRepository.saveAndFlush(sflUser);

        int databaseSizeBeforeDelete = sflUserRepository.findAll().size();

        // Delete the sflUser
        restSflUserMockMvc.perform(delete("/api/sfl-users/{id}", sflUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SflUser> sflUserList = sflUserRepository.findAll();
        assertThat(sflUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
