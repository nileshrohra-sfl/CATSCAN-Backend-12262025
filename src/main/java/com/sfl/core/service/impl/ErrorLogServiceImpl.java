package com.sfl.core.service.impl;

import com.sfl.core.domain.ErrorLog;
import com.sfl.core.domain.SflUser;
import com.sfl.core.repository.ErrorLogRepository;
import com.sfl.core.service.ErrorLogService;
import com.sfl.core.service.SflUserService;
import com.sfl.core.service.dto.ErrorLogDTO;
import com.sfl.core.service.dto.FilterRequestDTO;
import com.sfl.core.service.helper.CommonFilterSpecification;
import com.sfl.core.service.mapper.ErrorLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/***
 * @author Henil Mistryss
 * This service belong to persist mobile error log.
 * In future also persisting other logs here.
 */
@Service
public class ErrorLogServiceImpl implements ErrorLogService {

    private final ErrorLogRepository errorLogRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ErrorLogMapper mapper;
    private final SflUserService sflUserService;

    private final CommonFilterSpecification<ErrorLog> errorLogFilterSpecification;

    public ErrorLogServiceImpl(ErrorLogRepository errorLogRepository, ErrorLogMapper mapper, SflUserService sflUserService, CommonFilterSpecification<ErrorLog> errorLogFilterSpecification) {
        this.errorLogRepository = errorLogRepository;
        this.mapper = mapper;
        this.sflUserService = sflUserService;
        this.errorLogFilterSpecification = errorLogFilterSpecification;
    }

    @Override
    public ErrorLogDTO persistError(ErrorLogDTO errorLogDTO) {
        log.info("Error log persisting.");
        Optional<SflUser> userOptional = sflUserService.getCurrentLoggedInUser();
        ErrorLog  errorLog = mapper.toEntity(errorLogDTO);
        userOptional.ifPresent(errorLog::setUser);
        errorLogRepository.save(errorLog);
        return mapper.toDto(errorLog);
    }

    @Override
    public Page<ErrorLogDTO> getErrorLog(Pageable pageable) {
        log.info("Execute getErrorLog.");
        return errorLogRepository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Page<ErrorLogDTO> filterErrorLog(FilterRequestDTO filterRequestDTO) {
        log.info("Execute filter search service for error log");
        Pageable pageable = PageRequest.of(filterRequestDTO.getPageNumber(), filterRequestDTO.getPageSize());
        filterRequestDTO.setRangeBasedOnUpdatedDate(false);
        Specification<ErrorLog> specification = errorLogFilterSpecification.applyFilters(filterRequestDTO);
        log.info("Prepared specifications for filter error logs");
        Page<ErrorLog> errorLogs = errorLogRepository.findAll(specification, pageable);
        log.info("error log specifications are executed.");
        return errorLogs.map(mapper::toDto);
    }
}
