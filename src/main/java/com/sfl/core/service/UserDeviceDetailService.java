package com.sfl.core.service;

import com.sfl.core.domain.SflUser;
import com.sfl.core.domain.SflUserExtend;
import com.sfl.core.domain.UserDeviceDetail;
import com.sfl.core.repository.UserDeviceDetailRepository;
import com.sfl.core.service.dto.CountPerOSDTO;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.dto.UserDeviceDetailsDTO;
import com.sfl.core.service.helper.CommonFilterSpecification;
import com.sfl.core.service.mapper.UserDeviceDetailMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDeviceDetailService {

    private final UserDeviceDetailMapper userDeviceDetailMapper;

    private final UserDeviceDetailRepository userDeviceDetailRepository;

    private final CommonFilterSpecification<UserDeviceDetail> userDeviceSpecification;



    public UserDeviceDetailService(UserDeviceDetailMapper userDeviceDetailMapper, UserDeviceDetailRepository userDeviceDetailRepository, CommonFilterSpecification<UserDeviceDetail> userDeviceSpecification) {
        this.userDeviceDetailMapper = userDeviceDetailMapper;
        this.userDeviceDetailRepository = userDeviceDetailRepository;
        this.userDeviceSpecification = userDeviceSpecification;
    }

    public UserDeviceDetailsDTO persistDeviceDetails(UserDeviceDetailsDTO userDeviceDetailsDTO){
        UserDeviceDetail userDeviceDetail = userDeviceDetailMapper.toEntity(userDeviceDetailsDTO);
        userDeviceDetail = userDeviceDetailRepository.save(userDeviceDetail);
        return userDeviceDetailMapper.toDto(userDeviceDetail);
    }

    public List<CountPerOSDTO> getOSWiseDemographic(){
        return userDeviceDetailRepository.getOSWiseDemographic();
    }

    public List<Object[]> getUserCountByMonth(){
        return userDeviceDetailRepository.getUserCountByMonth();
    }

    public Page<UserDeviceDetailsDTO> getUserDeviceDetails(FilterRequestDTO filterRequestDTO) {
        Pageable pageable = PageRequest.of(filterRequestDTO.getPageNumber(), filterRequestDTO.getPageSize());
        Specification<UserDeviceDetail> specification = userDeviceSpecification.applyFilters(filterRequestDTO);
        Page<UserDeviceDetail> userDeviceDetails = userDeviceDetailRepository.findAll(specification, pageable);
        return userDeviceDetails.map(userDeviceDetailMapper::toDto);
    }
}
