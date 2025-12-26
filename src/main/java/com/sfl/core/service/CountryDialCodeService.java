package com.sfl.core.service;

import com.sfl.core.domain.CountryDialCode;
import com.sfl.core.repository.CountryDialCodeRepository;
import com.sfl.core.service.dto.CountryDialCodeDTO;
import com.sfl.core.service.mapper.CountryDialCodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CountryDialCode}.
 */
@Service
@Transactional
public class CountryDialCodeService {

    private final Logger log = LoggerFactory.getLogger(CountryDialCodeService.class);

    private final CountryDialCodeRepository countryDialCodeRepository;

    private final CountryDialCodeMapper countryDialCodeMapper;

    public CountryDialCodeService(CountryDialCodeRepository countryDialCodeRepository, CountryDialCodeMapper countryDialCodeMapper) {
        this.countryDialCodeRepository = countryDialCodeRepository;
        this.countryDialCodeMapper = countryDialCodeMapper;
    }

    /**
     * Get all the countryDialCodes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CountryDialCodeDTO> findAll() {
        log.debug("Request to get all CountryDialCodes");
        return countryDialCodeRepository.findAll().stream()
            .map(countryDialCodeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one countryDialCode by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CountryDialCodeDTO> findOne(Long id) {
        log.debug("Request to get CountryDialCode : {}", id);
        return countryDialCodeRepository.findById(id)
            .map(countryDialCodeMapper::toDto);
    }

    public CountryDialCode findByDialCode(String countryDialCode) {
        return countryDialCodeRepository.findByDialCode(countryDialCode);
    }
}
