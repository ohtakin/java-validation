package com.tioh.validation;

import com.tioh.validation.exceptions.BusinessRegistrationNumberFormatException;
import com.tioh.validation.exceptions.SizeLimitException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("사업자번호 테스트")
class BusinessRegistrationNumberTest {

    private static Stream<Arguments> getBusinessRegistrationNumberTrans() {
        return Stream.of(
                Arguments.of(1, "1", 3),
                Arguments.of(2, "1", 7),
                Arguments.of(3, "1", 1),
                Arguments.of(4, "1", 3),
                Arguments.of(5, "1", 7),
                Arguments.of(6, "1", 1),
                Arguments.of(7, "1", 3),
                Arguments.of(8, "1", 5),
                Arguments.of(9, "1", 1),
                Arguments.of(1, "2", 6),
                Arguments.of(2, "2", 4),
                Arguments.of(3, "2", 2),
                Arguments.of(4, "2", 6),
                Arguments.of(5, "2", 4),
                Arguments.of(6, "2", 2),
                Arguments.of(7, "2", 6),
                Arguments.of(8, "2", 0),
                Arguments.of(9, "2", 2)
        );
    }

    private static Stream<Arguments> getBusinessRegistrationNumber() {
        return Stream.of(
                Arguments.of("1058169429", true),
                Arguments.of("1058694786", true),
                Arguments.of("1058717995", true),
                Arguments.of("1068197118", true)
        );
    }

    @DisplayName("사업자번호 검증 테스트")
    @ParameterizedTest(name = "{index} => brn={0}, expectation={1}")
    @MethodSource("getBusinessRegistrationNumber")
    void validate(String brn, boolean expectation) {
        assertEquals(BusinessRegistrationNumber.getInstance().validate().apply(brn), expectation);
    }

    @DisplayName("사업자번호 검증 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"0000000002", "0000000003", "0000000004",})
    void validateException(String brn) {
        assertThrows(BusinessRegistrationNumberFormatException.class, () -> BusinessRegistrationNumber.getInstance().validate().apply(brn));
    }

    @DisplayName("사업자번호 검증 숫자 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"01000000ab", "01000가나"})
    void validateNumberException(String brn) {
        assertThrows(NumberFormatException.class, () -> BusinessRegistrationNumber.getInstance().validate().apply(brn));
    }

    @DisplayName("사업자번호 길이 테스트")
    @ParameterizedTest(name = "{index} => brn={0}, expectation={1}")
    @ValueSource(strings = {"0000000004", "1058169429", "1058694786"})
    void checkLength(String brn) {
        assertDoesNotThrow(() -> {
            BusinessRegistrationNumber instance = BusinessRegistrationNumber.getInstance();
            instance.checkLength(instance.lengthState).apply(brn);
        });
    }

    @DisplayName("사업자번호 길이 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"010000000", "01000", "010000000000000", "010000000000"})
    void checkLengthException(String brn) {
        assertThrows(SizeLimitException.class, () -> {
            BusinessRegistrationNumber instance = BusinessRegistrationNumber.getInstance();
            instance.checkLength(instance.lengthState).apply(brn);
        });
    }

    @DisplayName("사업자번호 숫자변환 테스트")
    @ParameterizedTest(name = "{index} => idx={0}, c={1}, expectation={2}")
    @MethodSource("getBusinessRegistrationNumberTrans")
    void getNumberTransform(int idx, char c, int expectation) {
        assertEquals(BusinessRegistrationNumber.getInstance().getNumberTransform(idx, c), expectation);
    }
}
