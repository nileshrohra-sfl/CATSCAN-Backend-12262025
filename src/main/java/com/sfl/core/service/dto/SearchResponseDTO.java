package com.sfl.core.service.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * A SearchResponseDTO
 */
public class SearchResponseDTO {

    private List<ProductDTO> respDataList = new ArrayList<>();

    public List<ProductDTO> getRespDataList() {
        return respDataList;
    }

    public void setRespDataList(List<ProductDTO> respDataList) {
        this.respDataList = respDataList;
    }

    @Override
    public String toString() {
        return "SearchResponseDTO{" +
            "respDataList=" + respDataList +
            '}';
    }
}
