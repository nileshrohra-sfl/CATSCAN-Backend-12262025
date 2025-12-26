package com.sfl.core.service.impl;

import com.sfl.core.domain.Authority;
import com.sfl.core.domain.SflUser;
import com.sfl.core.domain.SflUserExtend;
import com.sfl.core.repository.SflUserExtendRepository;
import com.sfl.core.repository.SflUserRepository;
import com.sfl.core.security.SecurityUtils;
import com.sfl.core.service.SflUserExtendService;
import com.sfl.core.service.UserDeviceDetailService;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.SflUserExtendDTO;
import com.sfl.core.service.dto.UserDeviceDetailsDTO;
import com.sfl.core.service.helper.CommonFilterSpecification;
import com.sfl.core.service.mapper.SflUserExtendMapper;
import com.sfl.core.web.rest.customhandler.Constants;
import com.sfl.core.web.rest.errors.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SflUserExtend}.
 */
@Service
@Transactional
public class SflUserExtendServiceImpl implements SflUserExtendService {

    private final Logger log = LoggerFactory.getLogger(SflUserExtendServiceImpl.class);

    private final SflUserExtendRepository sflUserExtendRepository;

    private final SflUserRepository sflUserRepository;

    private final SflUserExtendMapper sflUserExtendMapper;

    private final UserDeviceDetailService deviceDetailService;

    private final CommonFilterSpecification<SflUserExtend> userExtendCommonFilterSpecification;
    private final ExportDataImpl exportData;
    private static final String ALLERGEN_PREFIX = "mo_user_alg_";

    public SflUserExtendServiceImpl(SflUserExtendRepository sflUserExtendRepository, SflUserRepository sflUserRepository, SflUserExtendMapper sflUserExtendMapper, UserDeviceDetailService deviceDetailService, CommonFilterSpecification<SflUserExtend> userExtendCommonFilterSpecification, ExportDataImpl exportData) {
        this.sflUserExtendRepository = sflUserExtendRepository;
        this.sflUserRepository = sflUserRepository;
        this.sflUserExtendMapper = sflUserExtendMapper;

        this.deviceDetailService = deviceDetailService;
        this.userExtendCommonFilterSpecification = userExtendCommonFilterSpecification;
        this.exportData = exportData;
    }

    /**
     * Save a sflUserExtend.
     *
     * @param sflUserExtendDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SflUserExtendDTO save(SflUserExtendDTO sflUserExtendDTO) {
        log.debug("Request to save SflUserExtend : {}", sflUserExtendDTO);
        Long loginUser = (SecurityUtils.getCurrentUserId()).longValue();

        // Security constrain like only login user can able to change only own profile details.
        if(Objects.nonNull(sflUserExtendDTO.getId()) && !Objects.equals(sflUserExtendDTO.getId(),loginUser)){
            throw new GlobalException(Constants.AUTHENTICATION_EXCEPTION_MESSAGE, Constants.USER_CAN_NOT_BE_REGISTERED_EXCEPTION_CODE,
                HttpStatus.BAD_REQUEST);
        }
        //Todo: As per requirement email is not mandatory so user can remove email.
        // also we here checking is in system already have email then throws the exception.
        if (Objects.nonNull(sflUserExtendDTO.getEmail()) && !sflUserExtendDTO.getEmail().trim().isEmpty()) {
            boolean existingCnt = sflUserExtendRepository.getCountByEmailAndIdNot(sflUserExtendDTO.getEmail(),loginUser);
            if (existingCnt)
            {
                throw new GlobalException(Constants.EMAIL_ALREADY_EXIST_EXCEPTION_MESSAGE, Constants.USER_ALREADY_EXIST_EXCEPTION_CODE,
                    HttpStatus.BAD_REQUEST);
            }
        }
        sflUserExtendDTO.setId(loginUser);
        // We are updating fields which are only user requested.
        applyPatchTheUpdateRequest(sflUserExtendDTO);

        SflUserExtend sflUserExtend = sflUserExtendMapper.toEntity(sflUserExtendDTO);
        sflUserExtend.setId(loginUser);
        sflUserExtend = sflUserExtendRepository.save(sflUserExtend);
        return sflUserExtendMapper.toDto(sflUserExtend);
    }

    public void applyPatchTheUpdateRequest(SflUserExtendDTO sflUserExtendDTO){
        Optional<SflUser> currentUserData = sflUserRepository.findById(sflUserExtendDTO.getId());
        currentUserData.ifPresent(existingUser -> {

            if(Objects.isNull(sflUserExtendDTO.getUserName()) && Objects.isNull(existingUser.getUserName())){
                sflUserExtendDTO.setUserName(sflUserExtendDTO.getFirstName()+"."+sflUserExtendDTO.getLastName()+"_"+sflUserExtendDTO.getId());
            }
            if (Objects.isNull(sflUserExtendDTO.getStatus())) {
                sflUserExtendDTO.setStatus(existingUser.getStatus());
            }
            else if (Objects.isNull(sflUserExtendDTO.getCountryDialCode())) {
                sflUserExtendDTO.setCountryDialCode(existingUser.getCountryDialCode());
            }
            if (Objects.isNull(sflUserExtendDTO.getPhoneNumber())) {
                sflUserExtendDTO.setPhoneNumber(existingUser.getPhoneNumber());
            }
            if (Objects.isNull(sflUserExtendDTO.getEmail())) {
                sflUserExtendDTO.setEmail(existingUser.getEmail());
            }
            if (Objects.isNull(sflUserExtendDTO.getAuthorities())) {
                sflUserExtendDTO.setAuthorities(existingUser.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
            }
            if (Objects.isNull(sflUserExtendDTO.getImageUrl())) {
                sflUserExtendDTO.setImageUrl(existingUser.getImageUrl());
            }
            if (Objects.isNull(sflUserExtendDTO.getIsDeleted())) {
                sflUserExtendDTO.setIsDeleted(existingUser.getIsDeleted());
            }
            if (Objects.isNull(sflUserExtendDTO.getFirstName())) {
                sflUserExtendDTO.setFirstName(existingUser.getFirstName());
            }
            if (Objects.isNull(sflUserExtendDTO.getLastName())) {
                sflUserExtendDTO.setLastName(existingUser.getLastName());
            }

            if (Objects.isNull(sflUserExtendDTO.getCountryDialCode())) {
                sflUserExtendDTO.setCountryDialCode(existingUser.getCountryDialCode());
            }

            Optional<SflUserExtend> extend = sflUserExtendRepository.findById(existingUser.getId());
            extend.ifPresent(userDetails -> {
                if (Objects.isNull(sflUserExtendDTO.getAllergens())) {
                    sflUserExtendDTO.setAllergens(userDetails.getAllergens());
                }
                if (Objects.isNull(sflUserExtendDTO.getAddress())) {
                    sflUserExtendDTO.setAddress(userDetails.getAddress());
                }
            });

        });


    }

    /**
     * Get all the sflUserExtends.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<SflUserExtendDTO> findAll() {
        log.debug("Request to get all SflUserExtends");
        return sflUserExtendRepository.findAllByIsDeleted(Boolean.FALSE).stream()
            .map(sflUserExtendMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one sflUserExtend by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SflUserExtendDTO> findOne(Long id) {
        log.debug("Request to get SflUserExtend : {}", id);
        return sflUserExtendRepository.findById(id)
            .map(sflUserExtendMapper::toDto);
    }

    /**
     * Delete the sflUserExtend by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SflUserExtend : {}", id);
       Optional<SflUserExtend> sflUserExtendOptional = sflUserExtendRepository.findById(id);

       if (sflUserExtendOptional.isPresent()) {
           SflUserExtend sflUserExtend = sflUserExtendOptional.get();
           sflUserExtend.setIsDeleted(Boolean.TRUE);
           sflUserExtend.setDeletedBy(sflUserExtendOptional.get().getEmail());
           sflUserExtend.setDeletedDate(Instant.now());
           sflUserExtendRepository.save(sflUserExtend);
       }
    }

    @Override
    public Page<SflUserExtendDTO> getAllFilterSflUserExtends(FilterRequestDTO filterRequestDTO) {
        Pageable pageable = PageRequest.of(filterRequestDTO.getPageNumber(), filterRequestDTO.getPageSize());
        Specification<SflUserExtend> specification = userExtendCommonFilterSpecification.applyFilters(filterRequestDTO);
        Page<SflUserExtend> missingProducts = sflUserExtendRepository.findAll(specification, pageable);
        // Step 1: Generate the complete sorted allergen map (key -> transformed_key)
        List<SflUserExtend> allUsers = missingProducts.getContent();
        Map<String, String> allergenKeyMap = exportData.generateSortedAllergenMap(allUsers); // master key list

        // Step 2: Map each user entity to DTO with flattened allergens
        return missingProducts.map(user -> {
            SflUserExtendDTO dto = sflUserExtendMapper.toDto(user);
            dto.setDynamicAllergens(dynamicAllergen(user.getAllergens(), allergenKeyMap));
            return dto;
        });
    }

    /**
     * Flatten the allergens for a single user.
     */
    private Map<String, String> dynamicAllergen(Map<String, Object> userAllergens, Map<String, String> allergenKeyMap) {
        log.info("Dynamic allergen key map: {}", allergenKeyMap);
        Map<String, String> flatMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : allergenKeyMap.entrySet()) {
            String allergenKey = entry.getKey();
            String transformedKey = entry.getValue();
            Object allergenData = (userAllergens != null) ? userAllergens.get(allergenKey) : null;
            String value = exportData.getAllergenValue(allergenData);
            flatMap.put(transformedKey, value);
        }
        return flatMap;
    }

    @Override
    public UserDeviceDetailsDTO saveDeviceDetails(UserDeviceDetailsDTO userDeviceDetailsDTO) {
        return deviceDetailService.persistDeviceDetails(userDeviceDetailsDTO);
    }

    @Override
    public long activeUserCount(boolean status, boolean isDeleted) {
        return sflUserExtendRepository.countByStatusAndIsDeleted(status, isDeleted);
    }

    @Override
    public long totalUserCount() {
        return sflUserExtendRepository.count();
    }

    @Override
    public Page<UserDeviceDetailsDTO> getUserDeviceDetails(FilterRequestDTO filterRequestDTO) {
       return deviceDetailService.getUserDeviceDetails(filterRequestDTO);
    }


}
