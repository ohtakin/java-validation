package com.tioh.validation;

import com.tioh.validation.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ValidatorTest {

    private IValidator validator;

    @BeforeEach
    void initAll() {
        validator = Validator.getInstance();
    }

    private static Stream<Arguments> getBusinessRegistrationNumber() {
        return Stream.of(
                Arguments.of("1058169429", true),
                Arguments.of("1058694786", true),
                Arguments.of("105-86-94786", true),
                Arguments.of("1058717995", true),
                Arguments.of("1068197118", true)
        );
    }

    private static Stream<Arguments> getBusinessRegistrationNumberException() {
        return Stream.of(
                Arguments.of("0000000002", BusinessRegistrationNumberFormatException.class),
                Arguments.of("0000000003", BusinessRegistrationNumberFormatException.class),
                Arguments.of("0000000004", BusinessRegistrationNumberFormatException.class),
                Arguments.of("가나다라123410", NumberFormatException.class),
                Arguments.of("10681971181", SizeLimitException.class)
        );
    }

    private static Stream<Arguments> getResidentRegistrationNumber() {
        return Stream.of(
                Arguments.of("6407131018433", true),
                Arguments.of("640713 - 1018433", true)
        );
    }

    private static Stream<Arguments> getResidentRegistrationNumberException() {
        return Stream.of(
                Arguments.of("7910011123141", ResidentRegistrationNumberFormatException.class),
                Arguments.of("870812111231411", SizeLimitException.class),
                Arguments.of("660216112311", SizeLimitException.class),
                Arguments.of("8110225123112", GenderCodeException.class),
                Arguments.of("811022r123112", NumberFormatException.class)
        );
    }

    private static Stream<Arguments> getVehicleRegistrationNumber() {
        return Stream.of(
                Arguments.of("09조0360", true),
                Arguments.of("09 조 0360", true),
                Arguments.of("09허0360", true),
                Arguments.of("서울02가1111", true)
        );
    }

    private static Stream<Arguments> getVehicleRegistrationNumberException() {
        return Stream.of(
                Arguments.of("0000000001", SizeLimitException.class),
                Arguments.of("09차0360", UsageSignException.class),
                Arguments.of("서울2가1111", SizeLimitException.class)
        );
    }

    private static Stream<Arguments> getVehicleIdentificationNumber() {
        return Stream.of(
                Arguments.of("wvwuk63b92p546818", true),
                Arguments.of("wvw - uk63b - 92p - 546818", true),
                Arguments.of("11111111111111111", true)
        );
    }

    private static Stream<Arguments> getVehicleIdentificationNumberException() {
        return Stream.of(
                Arguments.of("WVWUK63B92P54681", SizeLimitException.class),
                Arguments.of("WVWUK63B92P5468188", SizeLimitException.class),
                Arguments.of("WVWUK63B92P54681i", IllegalCharacterException.class),
                Arguments.of("WVWUK63B92P54681I", IllegalCharacterException.class),
                Arguments.of("WVWUK63B92P54681o", IllegalCharacterException.class),
                Arguments.of("WVWUK63B92P54681O", IllegalCharacterException.class),
                Arguments.of("WVWUK63B92P54681q", IllegalCharacterException.class),
                Arguments.of("WVWUK63B92P54681Q", IllegalCharacterException.class),
                Arguments.of("WVWUK63B82P546818", VehicleIdentificationNumberFormatException.class)
        );
    }


    private static Stream<Arguments> getMobilePhoneNumber() {
        return Stream.of(
                Arguments.of("0100000000", true),
                Arguments.of("0160000000", true),
                Arguments.of("01000000000", true)
        );
    }

    private static Stream<Arguments> getMobilePhoneNumberException() {
        return Stream.of(
                Arguments.of("1100000000", PhoneNumberFormatException.class),
                Arguments.of("0100000000r", NumberFormatException.class),
                Arguments.of("11000000000", NumberFormatException.class),
                Arguments.of("017000000", SizeLimitException.class),
                Arguments.of("01000", SizeLimitException.class),
                Arguments.of("010000000000000", SizeLimitException.class),
                Arguments.of("010000000000", SizeLimitException.class)
        );
    }

    @Order(1)
    @DisplayName("사업자번호 테스트")
    @ParameterizedTest(name = "{index} => brn={0}, expectation={1}")
    @MethodSource("getBusinessRegistrationNumber")
    void businessRegistrationNumber(String brn, boolean expectation) {
        assertEquals(validator.businessRegistrationNumber(brn), expectation);
    }

    @Order(2)
    @DisplayName("사업자번호 exception 테스트")
    @ParameterizedTest
    @MethodSource("getBusinessRegistrationNumberException")
    void businessRegistrationNumber(String brn, Class<? extends Throwable> expectation) {
        assertThrows(expectation, () -> validator.businessRegistrationNumber(brn));
    }

    @Order(3)
    @DisplayName("주민등록번호 테스트")
    @ParameterizedTest(name = "{index} => rrn={0}, expectation={1}")
    @MethodSource("getResidentRegistrationNumber")
    void residentRegistrationNumber(String rrn, boolean expectation) {
        assertEquals(validator.residentRegistrationNumber(rrn), expectation);
    }

    @Order(4)
    @DisplayName("주민등록번호 exception 테스트")
    @ParameterizedTest(name = "{index} => rrn={0}, exception={1}")
    @MethodSource("getResidentRegistrationNumberException")
    void residentRegistrationNumber(String rrn, Class<? extends Throwable> exception) {
        assertThrows(exception, () -> validator.residentRegistrationNumber(rrn));
    }

    @Order(5)
    @DisplayName("차량번호 테스트")
    @ParameterizedTest(name = "{index} => vrn={0}, expectation={1}")
    @MethodSource("getVehicleRegistrationNumber")
    void vehicleRegistrationNumber(String vrn, boolean expectation) {
        Assertions.assertEquals(validator.vehicleRegistrationNumber(vrn), expectation);
    }

    @Order(6)
    @DisplayName("차량번호 테스트")
    @ParameterizedTest(name = "{index} => vrn={0}, exception={1}")
    @MethodSource("getVehicleRegistrationNumberException")
    void vehicleRegistrationNumber(String vrn, Class<? extends Throwable> exception) {
        assertThrows(exception, () -> validator.vehicleRegistrationNumber(vrn));
    }

    @Order(7)
    @DisplayName("차대번호 테스트")
    @ParameterizedTest(name = "{index} => vin={0}, expectation={1}")
    @MethodSource("getVehicleIdentificationNumber")
    void vehicleIdentificationNumber(String vin, boolean expectation) {
        Assertions.assertEquals(validator.vehicleIdentificationNumber(vin), expectation);
    }

    @Order(8)
    @DisplayName("차대번호 exception 테스트")
    @ParameterizedTest(name = "{index} => vin={0}, exception={1}")
    @MethodSource("getVehicleIdentificationNumberException")
    void vehicleIdentificationNumber(String vin, Class<? extends Throwable> exception) {
        assertThrows(exception, () -> validator.vehicleIdentificationNumber(vin));
    }

    @Order(9)
    @DisplayName("모바일번호 테스트")
    @ParameterizedTest(name = "{index} => num={0}, expectation={1}")
    @MethodSource("getMobilePhoneNumber")
    void mobilePhoneNumber(String num, boolean expectation) {
        Assertions.assertEquals(validator.mobilePhoneNumber(num), expectation);
    }

    @Order(10)
    @DisplayName("모바일번호 exception 테스트")
    @ParameterizedTest(name = "{index} => num={0}, exception={1}")
    @MethodSource("getMobilePhoneNumberException")
    void mobilePhoneNumber(String num, Class<? extends Throwable> exception) {
        assertThrows(exception,() -> validator.mobilePhoneNumber(num));
    }
}
