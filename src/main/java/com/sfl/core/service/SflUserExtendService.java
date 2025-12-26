package com.sfl.core.service;

import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.SflUserExtendDTO;
import com.sfl.core.service.dto.UserDeviceDetailsDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.sfl.core.domain.SflUserExtend}.
 */
public interface SflUserExtendService {

    /**
     * Save a sflUserExtend.
     *
     * @param sflUserExtendDTO the entity to save.
     * @return the persisted entity.
     */
    SflUserExtendDTO save(SflUserExtendDTO sflUserExtendDTO);

    /**
     * Get all the sflUserExtends.
     *
     * @return the list of entities.
     */
    List<SflUserExtendDTO> findAll();

    /**
     * Get the "id" sflUserExtend.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SflUserExtendDTO> findOne(Long id);

    /**
     * Delete the "id" sflUserExtend.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<SflUserExtendDTO> getAllFilterSflUserExtends(FilterRequestDTO filterRequestDTO);

    UserDeviceDetailsDTO saveDeviceDetails(UserDeviceDetailsDTO userDeviceDetailsDTO);

    long activeUserCount(boolean status, boolean isDeleted);

    long totalUserCount();

    Page<UserDeviceDetailsDTO> getUserDeviceDetails(FilterRequestDTO filterRequestDTO);

}
