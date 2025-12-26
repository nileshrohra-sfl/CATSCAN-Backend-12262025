package com.sfl.core.service;

import com.sfl.core.repository.AppVersionRepository;
import com.sfl.core.service.dto.AppVersionDTO;
import com.sfl.core.service.mapper.AppVersionMapper;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    private final AppVersionMapper appVersionMapper;

    private final AppVersionRepository appVersionRepository;

    public CommonService(AppVersionMapper appVersionMapper, AppVersionRepository appVersionRepository) {
        this.appVersionMapper = appVersionMapper;
        this.appVersionRepository = appVersionRepository;
    }

    public AppVersionDTO saveOrUpdate(AppVersionDTO appVersionDTO) {
        return appVersionMapper.toDto(appVersionRepository.save(appVersionMapper.toEntity(appVersionDTO)));
    }

    public AppVersionDTO getCurrentAppVersion() {
        return appVersionMapper.toDto(appVersionRepository.findFirstByOrderByIdDesc());
    }
}
