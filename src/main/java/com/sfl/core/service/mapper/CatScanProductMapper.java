package com.sfl.core.service.mapper;

import com.sfl.core.domain.CatScanProduct;
import com.sfl.core.service.dto.CatScanProductDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CommonAfterMapper.class})
public interface CatScanProductMapper extends EntityMapper<CatScanProductDTO, CatScanProduct>{

}
