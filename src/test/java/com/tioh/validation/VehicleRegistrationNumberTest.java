package com.tioh.validation;

import com.tioh.validation.exceptions.AreaSignException;
import com.tioh.validation.exceptions.SizeLimitException;
import com.tioh.validation.exceptions.UsageSignException;
import com.tioh.validation.exceptions.VehicleRegistrationNumberFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("차량번호 테스트")
class VehicleRegistrationNumberTest {

    private Logger logger = LoggerFactory.getLogger(VehicleRegistrationNumberTest.class);

    private static Stream<Arguments> getVehicleRegistrationNumberPattern() {
        return Stream.of(
                Arguments.of("12가1234", true),
                Arguments.of("경기12가1234", true)
        );
    }

    @DisplayName("차량번호 길이 테스트")
    @ParameterizedTest(name = "{index} => rrn={0}, expectation={1}")
    @ValueSource(strings = {"12가1234", "경기12가1234"})
    void checkLength(String rrn) {
        assertDoesNotThrow(() -> {
            VehicleRegistrationNumber instance = VehicleRegistrationNumber.getInstance();
            instance.checkLength(instance.lengthState).apply(rrn);
        });
    }

    @DisplayName("차량번호 길이 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"123가1234", "12가12345", "경기123가나1234", "경기12가나12354"})
    void checkLengthException(String rrn) {
        assertThrows(SizeLimitException.class, () -> {
            VehicleRegistrationNumber instance = VehicleRegistrationNumber.getInstance();
            instance.checkLength(instance.lengthState).apply(rrn);
        });
    }

    @DisplayName("차량번호 용도기호, 지역명 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"12가1234", "경기12가1234", "경기123가12334"})
    void checkUsageSign(String rrn) {
        logger.debug("rrn: {}", rrn);
        assertDoesNotThrow(() -> VehicleRegistrationNumber.getInstance().checkSign().apply(rrn));
    }

    @DisplayName("차량번호 용도기호 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"12과1234", "12규1235", "경기23가나1234", "경기12가나1254", "경기12유2541"})
    void checkSignException(String rrn) {
        assertThrows(UsageSignException.class, () -> VehicleRegistrationNumber.getInstance().checkSign().apply(rrn));
    }

    @DisplayName("차량번호 지역명 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"광명12가1234", "가산12가1234"})
    void checkAreaException(String rrn) {
        assertThrows(AreaSignException.class, () -> VehicleRegistrationNumber.getInstance().checkSign().apply(rrn));
    }

    @DisplayName("차량번호 패턴 테스트")
    @ParameterizedTest(name = "{index} => rrn={0}, expectation={1}")
    @MethodSource("getVehicleRegistrationNumberPattern")
    void checkPattern(String rrn, boolean expectation) {
        assertEquals(VehicleRegistrationNumber.getInstance().checkPattern().apply(rrn), expectation);
    }

    @DisplayName("차량번호 패턴 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"123가123", "123가나123", "12가12345", "경기123가나1234", "경기12가나12354", "a12가나12354", "경기12a2354"})
    void checkPatternException(String rrn) {
        assertThrows(VehicleRegistrationNumberFormatException.class, () -> VehicleRegistrationNumber.getInstance().checkPattern().apply(rrn));
    }
}
