package com.sfl.core.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SflUserExtendMapperTest {

    private SflUserExtendMapper sflUserExtendMapper;

    @BeforeEach
    public void setUp() {
        sflUserExtendMapper = new SflUserExtendMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(sflUserExtendMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(sflUserExtendMapper.fromId(null)).isNull();
    }
}
