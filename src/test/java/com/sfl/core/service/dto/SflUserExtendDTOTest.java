package com.sfl.core.service.dto;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sfl.core.web.rest.TestUtil;

@Disabled
public class SflUserExtendDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SflUserExtendDTO.class);
        SflUserExtendDTO sflUserExtendDTO1 = new SflUserExtendDTO();
        sflUserExtendDTO1.setId(1L);
        SflUserExtendDTO sflUserExtendDTO2 = new SflUserExtendDTO();
        assertThat(sflUserExtendDTO1).isNotEqualTo(sflUserExtendDTO2);
        sflUserExtendDTO2.setId(sflUserExtendDTO1.getId());
        assertThat(sflUserExtendDTO1).isEqualTo(sflUserExtendDTO2);
        sflUserExtendDTO2.setId(2L);
        assertThat(sflUserExtendDTO1).isNotEqualTo(sflUserExtendDTO2);
        sflUserExtendDTO1.setId(null);
        assertThat(sflUserExtendDTO1).isNotEqualTo(sflUserExtendDTO2);
    }
}
