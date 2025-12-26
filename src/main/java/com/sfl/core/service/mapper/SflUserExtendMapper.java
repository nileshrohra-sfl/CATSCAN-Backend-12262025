package com.sfl.core.service.mapper;


import com.sfl.core.domain.*;
import com.sfl.core.service.dto.SflUserExtendDTO;

import org.mapstruct.Mapper;


/**
 * Mapper for the entity {@link SflUserExtend} and its DTO {@link SflUserExtendDTO}.
 */
@Mapper(componentModel = "spring", uses = {SflUserExtendAfterMapper.class})
public interface SflUserExtendMapper extends EntityMapper<SflUserExtendDTO, SflUserExtend> {

    public default SflUserExtend fromId(Long id) {
        if (id == null) {
            return null;
        }
        SflUserExtend sflUserExtend = new SflUserExtend();
        sflUserExtend.setId(id);
        return sflUserExtend;
    }

}
