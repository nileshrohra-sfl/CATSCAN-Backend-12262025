package com.sfl.core.service.dto;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sfl.core.web.rest.TestUtil;

@Disabled
public class SflUserDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SflUserDTO.class);
        SflUserDTO sflUserDTO1 = new SflUserDTO();
        sflUserDTO1.setId(1L);
        SflUserDTO sflUserDTO2 = new SflUserDTO();
        assertThat(sflUserDTO1).isNotEqualTo(sflUserDTO2);
        sflUserDTO2.setId(sflUserDTO1.getId());
        assertThat(sflUserDTO1).isEqualTo(sflUserDTO2);
        sflUserDTO2.setId(2L);
        assertThat(sflUserDTO1).isNotEqualTo(sflUserDTO2);
        sflUserDTO1.setId(null);
        assertThat(sflUserDTO1).isNotEqualTo(sflUserDTO2);
    }
}
