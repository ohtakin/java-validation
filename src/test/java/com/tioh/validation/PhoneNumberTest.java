package com.tioh.validation;


import com.tioh.validation.exceptions.SizeLimitException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("모바일번호 테스트")
class PhoneNumberTest {

    private static Stream<Arguments> getMobilePhoneNumberPattern() {
        return Stream.of(
                Arguments.of("0100000000", true),
                Arguments.of("01000000000", true),
                Arguments.of("01700000000", true),
                Arguments.of("0200000000", true)
        );
    }

    @DisplayName("모바일번호 길이 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"0100000000", "01000000000"})
    void checkLength(String num) {
        assertDoesNotThrow(() -> {
            PhoneNumber instance = PhoneNumber.getInstance();
            instance.checkLength(instance.lengthState).apply(num);
        });
    }

    @DisplayName("모바일번호 길이 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"010000000", "01000", "010000000000000", "010000000000"})
    void checkLengthException(String num) {
        assertThrows(SizeLimitException.class, () -> {
            PhoneNumber instance = PhoneNumber.getInstance();
            instance.checkLength(instance.lengthState).apply(num);
        });
    }

    @DisplayName("모바일번호 패턴 테스트")
    @ParameterizedTest(name = "{index} => num={0}, expectation={1}")
    @MethodSource("getMobilePhoneNumberPattern")
    void checkPattern(String num, boolean expectation) {
        assertEquals(PhoneNumber.getInstance().checkPattern().apply(num), expectation);
    }

    @DisplayName("모바일번호 패턴 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"010000000ㅁㄴㅇㄹ", "01000afe", "11000000000", "010000000000000"})
    void checkPatternException2(String num) {
        assertThrows(NumberFormatException.class, () -> PhoneNumber.getInstance().checkPattern().apply(num));
    }
}
