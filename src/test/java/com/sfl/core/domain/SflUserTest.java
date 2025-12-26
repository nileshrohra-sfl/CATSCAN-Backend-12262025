package com.sfl.core.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.sfl.core.web.rest.TestUtil;

@Disabled
public class SflUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SflUser.class);
        SflUser sflUser1 = new SflUser();
        sflUser1.setId(1L);
        SflUser sflUser2 = new SflUser();
        sflUser2.setId(sflUser1.getId());
        assertThat(sflUser1).isEqualTo(sflUser2);
        sflUser2.setId(2L);
        assertThat(sflUser1).isNotEqualTo(sflUser2);
        sflUser1.setId(null);
        assertThat(sflUser1).isNotEqualTo(sflUser2);
    }
}
