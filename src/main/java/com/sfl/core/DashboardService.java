package com.sfl.core;

import com.sfl.core.service.dto.CountPerOSDTO;
import com.sfl.core.service.dto.DashboardCountStatusDTO;

import java.util.List;

public interface DashboardService {

    public DashboardCountStatusDTO getDashboardCountStatus();

    public List<CountPerOSDTO> getOSWiseDemographic();

    List<Object[]> getUserCountByMonth();
}
