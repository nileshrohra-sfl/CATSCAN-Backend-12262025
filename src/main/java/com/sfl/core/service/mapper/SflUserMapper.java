package com.sfl.core.service.mapper;


import com.sfl.core.domain.SflUser;
import com.sfl.core.service.dto.SflUserDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {SflUserExtendAfterMapper.class})
public interface SflUserMapper extends EntityMapper<SflUserDTO, SflUser> {

    default SflUser userFromId(Long id) {
        if (id == null) {
            return null;
        }
        SflUser user = new SflUser();
        user.setId(id);
        return user;
    }
}
