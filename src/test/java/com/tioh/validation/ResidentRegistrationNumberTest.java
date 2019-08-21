package com.tioh.validation;

import com.tioh.validation.exceptions.GenderCodeException;
import com.tioh.validation.exceptions.SizeLimitException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("주민번호 테스트")
class ResidentRegistrationNumberTest {

    @Order(1)
    @DisplayName("주민번호 검증 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"8708121112314"})
    void validate(String rrn) {
        ResidentRegistrationNumber instance = ResidentRegistrationNumber.getInstance();
        Assertions.assertDoesNotThrow(() -> instance.validate().apply(rrn));
    }

    @Order(2)
    @DisplayName("주민번호 검증 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"791001212a141", "가708121112314"})
    void validateException(String rrn) {
        assertThrows(NumberFormatException.class, () -> ResidentRegistrationNumber.getInstance().validate().apply(rrn));
    }

    @Order(3)
    @DisplayName("주민번호 길이 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"7910012123141", "8708121112314", "6602161123311", "6602167123311"})
    void checkLength(String rrn) {
        assertDoesNotThrow(() -> {
            ResidentRegistrationNumber instance = ResidentRegistrationNumber.getInstance();
            instance.checkLength(instance.lengthState).apply(rrn);
        });
    }

    @Order(4)
    @DisplayName("주민번호 길이 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"010000000", "01000", "010000000000000", "010000000000"})
    void checkLengthException(String rrn) {
        assertThrows(SizeLimitException.class, () -> {
            ResidentRegistrationNumber instance = ResidentRegistrationNumber.getInstance();
            instance.checkLength(instance.lengthState).apply(rrn);
        });
    }

    @Order(5)
    @DisplayName("성별코드 체크 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"7910012123141", "8708121112314", "6602161123311"})
    void checkGenderCode(String rrn) {
        Assertions.assertDoesNotThrow(() -> ResidentRegistrationNumber.getInstance().checkGenderCode().apply(rrn));
    }

    @Order(6)
    @DisplayName("성별코드 체크 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"7910016123141", "7910010123141", "7910015123141"})
    void checkGenderCodeException(String rrn) {
        assertThrows(GenderCodeException.class, () -> ResidentRegistrationNumber.getInstance().checkGenderCode().apply(rrn));
    }
}
