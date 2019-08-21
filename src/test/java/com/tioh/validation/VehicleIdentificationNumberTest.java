package com.tioh.validation;

import com.tioh.validation.exceptions.IllegalCharacterException;
import com.tioh.validation.exceptions.SizeLimitException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("차대번호 테스트")
class VehicleIdentificationNumberTest {

    private Logger logger = LoggerFactory.getLogger(VehicleIdentificationNumberTest.class);

    static Stream<Arguments> getVehicleIdentificationNumberValidate() {
        return Stream.of(
                Arguments.of("wvwuk63b92p546818", true),
                Arguments.of("11111111111111111", true)
        );
    }

    @DisplayName("차대번호 검증 테스트")
    @ParameterizedTest(name = "{index} => vrn={0}, expectation={1}")
    @MethodSource("getVehicleIdentificationNumberValidate")
    void validate(String vin, boolean expectation) {
        logger.debug("vin: {}", vin);
        assertEquals(VehicleIdentificationNumber.getInstance().validate().apply(vin), expectation);
    }

    @DisplayName("차대번호 검증 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = "wvwuk63ba2p546818")
    void validateException(String vin) {
        logger.debug("vin: {}", vin);
        assertThrows(NumberFormatException.class, () -> VehicleIdentificationNumber.getInstance().validate().apply(vin));
    }

    @DisplayName("차대번호 길이 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"WVWUK63B92P54681i", "wvwuk63b92p546818", "11111111111111111"})
    void checkLength(String vin) {
        assertDoesNotThrow(() -> {
            VehicleIdentificationNumber instance = VehicleIdentificationNumber.getInstance();
            instance.checkLength(instance.lengthState).apply(vin);
        });
    }

    @DisplayName("차대번호 길이 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"010000000", "01000", "010000000000000", "010000000000", "010000000000000000"})
    void checkLengthException(String vin) {
        assertThrows(SizeLimitException.class, () -> {
            VehicleIdentificationNumber instance = VehicleIdentificationNumber.getInstance();
            instance.checkLength(instance.lengthState).apply(vin);
        });
    }

    @DisplayName("차대번호 패턴 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"wvwuk63b92p546818", "11111111111111111"})
    void checkPattern(String vin) {
        Assertions.assertDoesNotThrow(() -> VehicleIdentificationNumber.getInstance().checkPattern().apply(vin));
    }

    @DisplayName("차대번호 패턴 exception 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"WVWUK63B92P54681", "WVWUK63B92P5468188", "WVWUK63B92P54681i"})
    void checkPatternException(String vin) {
        assertThrows(IllegalCharacterException.class, () -> VehicleIdentificationNumber.getInstance().checkPattern().apply(vin));
    }
}
