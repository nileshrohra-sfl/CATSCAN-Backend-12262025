package com.sfl.core.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sfl.core.web.rest.TestUtil;

@Disabled
public class SflUserExtendTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SflUserExtend.class);
        SflUserExtend sflUserExtend1 = new SflUserExtend();
        sflUserExtend1.setId(1L);
        SflUserExtend sflUserExtend2 = new SflUserExtend();
        sflUserExtend2.setId(sflUserExtend1.getId());
        assertThat(sflUserExtend1).isEqualTo(sflUserExtend2);
        sflUserExtend2.setId(2L);
        assertThat(sflUserExtend1).isNotEqualTo(sflUserExtend2);
        sflUserExtend1.setId(null);
        assertThat(sflUserExtend1).isNotEqualTo(sflUserExtend2);
    }
}
