package com.sfl.core.service.mapper;

import com.sfl.core.domain.ErrorLog;
import com.sfl.core.service.dto.ErrorLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CommonAfterMapper.class, SflUserMapper.class})
public interface ErrorLogMapper extends EntityMapper<ErrorLogDTO, ErrorLog>{

    @Mapping(source = "userId", target = "user")
    ErrorLog toEntity(ErrorLogDTO errorLogDTO);

    @Mapping(source = "user.id", target = "userId")
    ErrorLogDTO toDto(ErrorLog errorLog);
}
