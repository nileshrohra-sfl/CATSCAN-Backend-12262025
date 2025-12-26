package com.sfl.core.web.rest;

import com.sfl.core.security.SecurityUtils;
import com.sfl.core.service.ExportData;
import com.sfl.core.service.SflUserExtendService;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.UserDeviceDetailsDTO;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.service.dto.SflUserExtendDTO;

import com.sfl.core.web.rest.errors.GlobalException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sfl.core.domain.SflUserExtend}.
 */
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "javabaseproject")
public class SflUserExtendResource {

    private final Logger log = LoggerFactory.getLogger(SflUserExtendResource.class);

    private static final String ENTITY_NAME = "sflUserExtend";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SflUserExtendService sflUserExtendService;

    private final ExportData exportData;

    public SflUserExtendResource(SflUserExtendService sflUserExtendService, ExportData exportData) {
        this.sflUserExtendService = sflUserExtendService;
        this.exportData = exportData;
    }

    /**
     * {@code POST  /sfl-user-extends} : Create a new sflUserExtend.
     *
     * @param sflUserExtendDTO the sflUserExtendDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sflUserExtendDTO, or with status {@code 400 (Bad Request)} if the sflUserExtend has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sfl-user-extends")
    public ResponseEntity<SflUserExtendDTO> createSflUserExtend(@RequestBody SflUserExtendDTO sflUserExtendDTO) throws URISyntaxException {
        log.debug("REST request to save SflUserExtend : {}", sflUserExtendDTO);
        if (sflUserExtendDTO.getId() != null) {
            throw new GlobalException(Constants.USER_CANNOT_UPDATE_WITHOUT_ID_EXCEPTION_MESSAGE,
                Constants.USER_RESOURCE_USER_CANNOT_UPDATE_WITHOUT_ID_EXCEPTION_CODE,
                HttpStatus.BAD_REQUEST);
        }
        SflUserExtendDTO result = sflUserExtendService.save(sflUserExtendDTO);
        return ResponseEntity.created(new URI("/api/sfl-user-extends/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sfl-user-extends} : Updates an existing sflUserExtend.
     *
     * @param sflUserExtendDTO the sflUserExtendDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sflUserExtendDTO,
     * or with status {@code 400 (Bad Request)} if the sflUserExtendDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sflUserExtendDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sfl-user-extends")
    public ResponseEntity<SflUserExtendDTO> updateSflUserExtend(@RequestBody SflUserExtendDTO sflUserExtendDTO) throws URISyntaxException {
        log.debug("REST request to update SflUserExtend : {}", sflUserExtendDTO);
        SflUserExtendDTO result = sflUserExtendService.save(sflUserExtendDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sflUserExtendDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sfl-user-extends} : get all the sflUserExtends.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sflUserExtends in body.
     */
    @GetMapping("/sfl-user-extends")
    public List<SflUserExtendDTO> getAllSflUserExtends() {
        log.debug("REST request to get all SflUserExtends");
        return sflUserExtendService.findAll();
    }

    @PostMapping("/sfl-user-extends/filter")
    public Page<SflUserExtendDTO> getAllFilterSflUserExtends(@RequestBody FilterRequestDTO filterRequestDTO) {
        log.info("REST request to get all getAllFilterSflUserExtends");
        return sflUserExtendService.getAllFilterSflUserExtends(filterRequestDTO);
    }

    /**
     * {@code GET  /sfl-user-extends/:id} : get the "id" sflUserExtend.
     *
     * @param id the id of the sflUserExtendDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sflUserExtendDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sfl-user-extends/{id}")
    public ResponseEntity<SflUserExtendDTO> getSflUserExtend(@PathVariable Long id) {
        log.debug("REST request to get SflUserExtend : {}", id);
        Optional<SflUserExtendDTO> sflUserExtendDTO = sflUserExtendService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sflUserExtendDTO);
    }

    /**
     * {@code DELETE  /sfl-user-extends/:id} : delete the "id" sflUserExtend.
     *
     * @param id the id of the sflUserExtendDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sfl-user-extends/{id}")
    public ResponseEntity<Void> deleteSflUserExtend(@PathVariable Long id) {
        log.debug("REST request to delete SflUserExtend : {}", id);
        sflUserExtendService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/user/device-detail")
    public ResponseEntity<UserDeviceDetailsDTO> persistUserDeviceDetails(@RequestBody UserDeviceDetailsDTO userDeviceDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save user device details : {}", userDeviceDetailsDTO);
        userDeviceDetailsDTO.setUserId(SecurityUtils.getCurrentUserId().longValue());
        UserDeviceDetailsDTO result = sflUserExtendService.saveDeviceDetails(userDeviceDetailsDTO);
        return ResponseEntity.created(new URI("/user/deviceDetail" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, "USER_DEVICE_DETAIL", result.getId().toString()))
            .body(result);
    }

    @PostMapping("/user-device-details/filter")
    public Page<UserDeviceDetailsDTO> getAllUserDevices(@RequestBody FilterRequestDTO filterRequestDTO) {
        log.info("REST request to get all getAllFilterSflUserExtends");
        return sflUserExtendService.getUserDeviceDetails(filterRequestDTO);
    }

    /**
     * * {@code POST  /sfl-user-extends/export} : Export mobile user data.
     * @param response
     */

    @PostMapping("/sfl-user-extends/export")
    public void exportMobileUserData(HttpServletResponse response) {
        try {
            log.info("Export Data for mobile user request");
            exportData.exportMobileUserData(response);
        } catch (Exception ignored) {

        }
    }
}
