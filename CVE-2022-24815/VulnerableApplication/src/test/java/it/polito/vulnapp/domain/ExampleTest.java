package it.polito.vulnapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.polito.vulnapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExampleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Example.class);
        Example example1 = new Example();
        example1.setId("id1");
        Example example2 = new Example();
        example2.setId(example1.getId());
        assertThat(example1).isEqualTo(example2);
        example2.setId("id2");
        assertThat(example1).isNotEqualTo(example2);
        example1.setId(null);
        assertThat(example1).isNotEqualTo(example2);
    }
}
