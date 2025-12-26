package com.sfl.core.service;

import com.sfl.core.service.dto.ErrorLogDTO;
import com.sfl.core.service.dto.FilterRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ErrorLogService {

    public ErrorLogDTO persistError(ErrorLogDTO errorLogDTO);

    public Page<ErrorLogDTO> getErrorLog(Pageable pageable);

    public Page<ErrorLogDTO> filterErrorLog(FilterRequestDTO filterRequestDTO);
}
