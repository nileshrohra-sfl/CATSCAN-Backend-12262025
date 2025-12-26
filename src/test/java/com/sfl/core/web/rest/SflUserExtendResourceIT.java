package com.sfl.core.web.rest;

import com.sfl.core.CatScanApp;
import com.sfl.core.domain.SflUserExtend;
import com.sfl.core.repository.SflUserExtendRepository;
import com.sfl.core.service.SflUserExtendService;
import com.sfl.core.service.dto.SflUserExtendDTO;
import com.sfl.core.service.mapper.SflUserExtendMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SflUserExtendResource} REST controller.
 */
@SpringBootTest(classes = CatScanApp.class)
@Disabled
@AutoConfigureMockMvc
@WithMockUser
public class SflUserExtendResourceIT {

    private static final String DEFAULT_FIELD_ONE = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_ONE = "BBBBBBBBBB";

    @Autowired
    private SflUserExtendRepository sflUserExtendRepository;

    @Autowired
    private SflUserExtendMapper sflUserExtendMapper;

    @Autowired
    private SflUserExtendService sflUserExtendService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSflUserExtendMockMvc;

    private SflUserExtend sflUserExtend;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SflUserExtend createEntity(EntityManager em) {
        SflUserExtend sflUserExtend = new SflUserExtend()
            .address(DEFAULT_FIELD_ONE);
        return sflUserExtend;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SflUserExtend createUpdatedEntity(EntityManager em) {
        SflUserExtend sflUserExtend = new SflUserExtend()
            .address(UPDATED_FIELD_ONE);
        return sflUserExtend;
    }

    @BeforeEach
    public void initTest() {
        sflUserExtend = createEntity(em);
    }

    @Test
    @Transactional
    public void createSflUserExtend() throws Exception {
        int databaseSizeBeforeCreate = sflUserExtendRepository.findAll().size();

        // Create the SflUserExtend
        SflUserExtendDTO sflUserExtendDTO = sflUserExtendMapper.toDto(sflUserExtend);
        restSflUserExtendMockMvc.perform(post("/api/sfl-user-extends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sflUserExtendDTO)))
            .andExpect(status().isCreated());

        // Validate the SflUserExtend in the database
        List<SflUserExtend> sflUserExtendList = sflUserExtendRepository.findAll();
        assertThat(sflUserExtendList).hasSize(databaseSizeBeforeCreate + 1);
        SflUserExtend testSflUserExtend = sflUserExtendList.get(sflUserExtendList.size() - 1);
        assertThat(testSflUserExtend.getAddress()).isEqualTo(DEFAULT_FIELD_ONE);
    }

    @Test
    @Transactional
    public void createSflUserExtendWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sflUserExtendRepository.findAll().size();

        // Create the SflUserExtend with an existing ID
        sflUserExtend.setId(1L);
        SflUserExtendDTO sflUserExtendDTO = sflUserExtendMapper.toDto(sflUserExtend);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSflUserExtendMockMvc.perform(post("/api/sfl-user-extends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sflUserExtendDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SflUserExtend in the database
        List<SflUserExtend> sflUserExtendList = sflUserExtendRepository.findAll();
        assertThat(sflUserExtendList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSflUserExtends() throws Exception {
        // Initialize the database
        sflUserExtendRepository.saveAndFlush(sflUserExtend);

        // Get all the sflUserExtendList
        restSflUserExtendMockMvc.perform(get("/api/sfl-user-extends?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sflUserExtend.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_FIELD_ONE)));
    }

    @Test
    @Transactional
    public void getSflUserExtend() throws Exception {
        // Initialize the database
        sflUserExtendRepository.saveAndFlush(sflUserExtend);

        // Get the sflUserExtend
        restSflUserExtendMockMvc.perform(get("/api/sfl-user-extends/{id}", sflUserExtend.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sflUserExtend.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_FIELD_ONE));
    }

    @Test
    @Transactional
    public void getNonExistingSflUserExtend() throws Exception {
        // Get the sflUserExtend
        restSflUserExtendMockMvc.perform(get("/api/sfl-user-extends/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSflUserExtend() throws Exception {
        // Initialize the database
        sflUserExtendRepository.saveAndFlush(sflUserExtend);

        int databaseSizeBeforeUpdate = sflUserExtendRepository.findAll().size();

        // Update the sflUserExtend
        SflUserExtend updatedSflUserExtend = sflUserExtendRepository.findById(sflUserExtend.getId()).get();
        // Disconnect from session so that the updates on updatedSflUserExtend are not directly saved in db
        em.detach(updatedSflUserExtend);
        updatedSflUserExtend
            .address(UPDATED_FIELD_ONE);
        SflUserExtendDTO sflUserExtendDTO = sflUserExtendMapper.toDto(updatedSflUserExtend);

        restSflUserExtendMockMvc.perform(put("/api/sfl-user-extends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sflUserExtendDTO)))
            .andExpect(status().isOk());

        // Validate the SflUserExtend in the database
        List<SflUserExtend> sflUserExtendList = sflUserExtendRepository.findAll();
        assertThat(sflUserExtendList).hasSize(databaseSizeBeforeUpdate);
        SflUserExtend testSflUserExtend = sflUserExtendList.get(sflUserExtendList.size() - 1);
        assertThat(testSflUserExtend.getAddress()).isEqualTo(UPDATED_FIELD_ONE);
    }

    @Test
    @Transactional
    public void updateNonExistingSflUserExtend() throws Exception {
        int databaseSizeBeforeUpdate = sflUserExtendRepository.findAll().size();

        // Create the SflUserExtend
        SflUserExtendDTO sflUserExtendDTO = sflUserExtendMapper.toDto(sflUserExtend);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSflUserExtendMockMvc.perform(put("/api/sfl-user-extends")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sflUserExtendDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SflUserExtend in the database
        List<SflUserExtend> sflUserExtendList = sflUserExtendRepository.findAll();
        assertThat(sflUserExtendList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSflUserExtend() throws Exception {
        // Initialize the database
        sflUserExtendRepository.saveAndFlush(sflUserExtend);

        int databaseSizeBeforeDelete = sflUserExtendRepository.findAll().size();

        // Delete the sflUserExtend
        restSflUserExtendMockMvc.perform(delete("/api/sfl-user-extends/{id}", sflUserExtend.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SflUserExtend> sflUserExtendList = sflUserExtendRepository.findAll();
        assertThat(sflUserExtendList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
