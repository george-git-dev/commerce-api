package br.com.george.commerce.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** Teste unitário puro: sem Spring e sem banco (roda em milissegundos). */
class CpfMaskTest {

    @ParameterizedTest
    @CsvSource({
            "52998224725,    ***.982.247-**",
            "529.982.247-25, ***.982.247-**",
            "123,            ***.***.***-**"
    })
    void shouldShowOnlyTheMiddleDigits(String cpf, String expected) {
        assertThat(CpfMask.mask(cpf)).isEqualTo(expected);
    }

    @Test
    void accountWithoutCpfStaysWithout() {
        assertThat(CpfMask.mask(null)).isNull();
    }
}
