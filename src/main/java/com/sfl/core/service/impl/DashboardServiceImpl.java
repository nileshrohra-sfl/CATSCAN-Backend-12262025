package com.sfl.core.service.impl;

import com.sfl.core.DashboardService;
import com.sfl.core.service.MissingProductService;
import com.sfl.core.service.SflUserExtendService;
import com.sfl.core.service.TryAgainProductService;
import com.sfl.core.service.UserDeviceDetailService;
import com.sfl.core.service.dto.CountPerOSDTO;
import com.sfl.core.service.dto.DashboardCountStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final TryAgainProductService tryAgainProductService;

    private final SflUserExtendService sflUserExtendService;

    private final MissingProductService missingProductService;

    private final UserDeviceDetailService userDeviceDetailService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public DashboardServiceImpl(TryAgainProductService tryAgainProductService, SflUserExtendService sflUserExtendService, MissingProductService missingProductService, UserDeviceDetailService userDeviceDetailService) {
        this.tryAgainProductService = tryAgainProductService;
        this.sflUserExtendService = sflUserExtendService;
        this.missingProductService = missingProductService;
        this.userDeviceDetailService = userDeviceDetailService;
    }

    @Override
    public DashboardCountStatusDTO getDashboardCountStatus() {
        log.info("Request for dashboard count.");
        long productSelectionCnt = tryAgainProductService.getTryAgainProductCount();
        long activeUserCnt = sflUserExtendService.activeUserCount(true, false);
        long missingProductCnt = missingProductService.getTotalMissingProductCount();
        long totalUserCnt = sflUserExtendService.totalUserCount();

        DashboardCountStatusDTO statusCountDto = new DashboardCountStatusDTO();
        statusCountDto.setProductSelectionCount(productSelectionCnt);
        statusCountDto.setActiveUserCount(activeUserCnt);
        statusCountDto.setMissingProductCount(missingProductCnt);
        statusCountDto.setMobileUserCount(totalUserCnt);
        return statusCountDto;
    }

    @Override
    public List<CountPerOSDTO> getOSWiseDemographic() {
        return userDeviceDetailService.getOSWiseDemographic();
    }

    @Override
    public List<Object[]> getUserCountByMonth(){
        return userDeviceDetailService.getUserCountByMonth();
    }
}
