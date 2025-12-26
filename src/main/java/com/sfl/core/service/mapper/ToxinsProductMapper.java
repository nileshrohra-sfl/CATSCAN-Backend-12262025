package com.sfl.core.service.mapper;

import com.sfl.core.domain.CatScanProduct;
import com.sfl.core.domain.ToxinsProduct;
import com.sfl.core.service.dto.CatScanProductDTO;
import com.sfl.core.service.dto.ToxinsProductDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CommonAfterMapper.class})
public interface ToxinsProductMapper extends EntityMapper<ToxinsProductDTO, ToxinsProduct>{

}
