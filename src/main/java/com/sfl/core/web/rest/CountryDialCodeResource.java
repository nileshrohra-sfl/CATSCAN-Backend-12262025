package com.sfl.core.web.rest;

import com.sfl.core.service.CountryDialCodeService;
import com.sfl.core.service.dto.CountryDialCodeDTO;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sfl.core.domain.CountryDialCode}.
 */
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "javabaseproject")
public class CountryDialCodeResource {

    private final Logger log = LoggerFactory.getLogger(CountryDialCodeResource.class);

    private final CountryDialCodeService countryDialCodeService;

    public CountryDialCodeResource(CountryDialCodeService countryDialCodeService) {
        this.countryDialCodeService = countryDialCodeService;
    }

    /**
     * {@code GET  /country-dial-codes} : get all the countryDialCodes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countryDialCodes in body.
     */
    @GetMapping("/country-dial-codes")
    public List<CountryDialCodeDTO> getAllCountryDialCodes() {
        log.debug("REST request to get all CountryDialCodes");
        return countryDialCodeService.findAll();
    }

    /**
     * {@code GET  /country-dial-codes/:id} : get the "id" countryDialCode.
     *
     * @param id the id of the countryDialCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countryDialCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/country-dial-codes/{id}")
    public ResponseEntity<CountryDialCodeDTO> getCountryDialCode(@PathVariable Long id) {
        log.debug("REST request to get CountryDialCode : {}", id);
        Optional<CountryDialCodeDTO> countryDialCodeDTO = countryDialCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countryDialCodeDTO);
    }
}
