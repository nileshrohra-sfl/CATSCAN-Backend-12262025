package com.sfl.core.web.rest;

import com.sfl.core.service.SflUserService;
import com.sfl.core.service.dto.SflUserDTO;
import com.sfl.core.service.dto.SflUserExtendDTO;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import com.sfl.core.web.rest.system.filter.domain.SflUserQuery;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sfl.core.domain.SflUser}.
 */
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "javabaseproject")
public class SflUserResource {

    private final Logger log = LoggerFactory.getLogger(SflUserResource.class);

    private static final String ENTITY_NAME = "sflUser";

    @Value("${jhipster.clientApp.name}")
    private  String applicationName;

    private final SflUserService sflUserService;

    public SflUserResource(SflUserService sflUserService) {
        this.sflUserService = sflUserService;
    }


    /**
     * {@code POST  /sfl-users} : Create a new sflUser.
     *
     * @param sflUserDTO the sflUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sflUserDTO, or with status {@code 400 (Bad Request)} if the sflUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sfl-users")
    public ResponseEntity<SflUserDTO> createSflUser(@RequestBody SflUserDTO sflUserDTO) throws URISyntaxException {
        log.debug("REST request to save SflUser : {}", sflUserDTO);
        if (sflUserDTO.getId() != null) {
            throw new GlobalException(Constants.NEW_USER_CANNOT_HAVE_ID_EXCEPTION_MESSAGE,
                Constants.USER_RESOURCE_NEW_USER_CANNOT_HAVE_ID_EXCEPTION_CODE, HttpStatus.BAD_REQUEST);
        }
        SflUserDTO result = sflUserService.save(sflUserDTO);
        return ResponseEntity.created(new URI("/api/sfl-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sfl-users} : Updates an existing sflUser.
     *
     * @param sflUserDTO the sflUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sflUserDTO,
     * or with status {@code 400 (Bad Request)} if the sflUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sflUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sfl-users")
    public ResponseEntity<SflUserDTO> updateSflUser(@RequestBody SflUserDTO sflUserDTO) throws URISyntaxException {
        log.debug("REST request to update SflUser : {}", sflUserDTO);
        if (sflUserDTO.getId() == null) {
            throw new GlobalException(Constants.USER_CANNOT_UPDATE_WITHOUT_ID_EXCEPTION_MESSAGE,
                Constants.USER_RESOURCE_USER_CANNOT_UPDATE_WITHOUT_ID_EXCEPTION_CODE,
                HttpStatus.BAD_REQUEST);
        }
        SflUserDTO result = sflUserService.save(sflUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sflUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sfl-users} : get all the sflUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sflUsers in body.
     */
    @GetMapping("/sfl-users")
    public List<SflUserDTO> getAllSflUsers() {
        log.debug("REST request to get all SflUsers");
        return sflUserService.findAll();
    }

    //global filter functionality implementation
    @PostMapping(value = {"/sfl-users/filter"})
    public Page<SflUserDTO> filter(@RequestBody(required = false) SflUserQuery query,
                                   Pageable pageable) {
        return sflUserService.filter(query, pageable);
    }
    /**
     * {@code GET  /sfl-users/:id} : get the "id" sflUser.
     *
     * @param id the id of the sflUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sflUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sfl-users/{id}")
    public ResponseEntity<SflUserDTO> getSflUser(@PathVariable Long id) {
        log.debug("REST request to get SflUser : {}", id);
        Optional<SflUserDTO> sflUserDTO = sflUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sflUserDTO);
    }

    /**
     * {@code DELETE  /sfl-users/:id} : delete the "id" sflUser.
     *
     * @param id the id of the sflUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sfl-users/{id}")
    public ResponseEntity<Void> deleteSflUser(@PathVariable Long id) {
        log.debug("REST request to delete SflUser : {}", id);
        sflUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code GET  /paginated-sfl-users} : get all the sflUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the page of sflUsers in body.
     */
    @GetMapping("/paginated-sfl-users")
    public Page<SflUserExtendDTO> getSflUsers(@RequestParam(value = "pageNumber", defaultValue = com.sfl.core.config.Constants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
                                              @RequestParam(value = "pageSize", defaultValue = com.sfl.core.config.Constants.DEFAULT_PAGE_SIZE) Integer pageSize,
                                              @RequestParam(value = "orderBy", defaultValue = com.sfl.core.config.Constants.DEFAULT_ORDER_BY) String orderBy,
                                              @RequestParam(value = "orderField", defaultValue = com.sfl.core.config.Constants.DEFAULT_ORDER_FIELD) String orderField,
                                              @RequestParam(value = "status", required = false) Boolean status,
                                              @RequestParam(value = "search", required = false) String search) {
        log.debug("REST request to get all SflUsers with pagination");
        Sort sort = Sort.by(orderBy.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, orderField);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return sflUserService.findAllSflUsers(status, search, pageable);
    }
}
