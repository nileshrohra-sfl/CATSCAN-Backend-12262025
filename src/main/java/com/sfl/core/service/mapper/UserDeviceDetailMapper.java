package com.sfl.core.service.mapper;

import com.sfl.core.domain.UserDeviceDetail;
import com.sfl.core.service.dto.UserDeviceDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CommonAfterMapper.class, SflUserExtendMapper.class})
public interface UserDeviceDetailMapper  extends EntityMapper<UserDeviceDetailsDTO, UserDeviceDetail>{

    @Mapping(source = "userId", target = "user")
    UserDeviceDetail toEntity(UserDeviceDetailsDTO userDeviceDetailsDTO);

    @Mapping(source = "user.id", target = "userId")
    UserDeviceDetailsDTO toDto(UserDeviceDetail userDeviceDetail);
}
