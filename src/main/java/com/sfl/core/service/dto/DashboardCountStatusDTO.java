package com.sfl.core.service.dto;

public class DashboardCountStatusDTO {

    public long mobileUserCount = 0L;

    public long missingProductCount = 0L;

    public long productSelectionCount = 0L;

    public long activeUserCount = 0L;

    public long getMobileUserCount() {
        return mobileUserCount;
    }

    public void setMobileUserCount(long mobileUserCount) {
        this.mobileUserCount = mobileUserCount;
    }

    public long getMissingProductCount() {
        return missingProductCount;
    }

    public void setMissingProductCount(long missingProductCount) {
        this.missingProductCount = missingProductCount;
    }

    public long getProductSelectionCount() {
        return productSelectionCount;
    }

    public void setProductSelectionCount(long productSelectionCount) {
        this.productSelectionCount = productSelectionCount;
    }

    public long getActiveUserCount() {
        return activeUserCount;
    }

    public void setActiveUserCount(long activeUserCount) {
        this.activeUserCount = activeUserCount;
    }
}
