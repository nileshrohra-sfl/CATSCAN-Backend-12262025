package com.sfl.core.service;

import com.sfl.core.service.dto.RequestReportDTO;

import javax.servlet.http.HttpServletResponse;

public interface ExportData {

    public void exportGenericData(RequestReportDTO requestReportDTO, HttpServletResponse response);

    public void exportTryAgainProductData(RequestReportDTO requestReportDTO, HttpServletResponse response);

    void exportMobileUserData(HttpServletResponse response);

}
