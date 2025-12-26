package com.sfl.core.service.mapper;

import com.sfl.core.domain.AppVersion;
import com.sfl.core.service.dto.AppVersionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CommonAfterMapper.class})
public interface AppVersionMapper extends EntityMapper<AppVersionDTO, AppVersion> {
}
