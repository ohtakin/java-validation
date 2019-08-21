package com.tioh.validation;

import com.tioh.validation.exceptions.IllegalCharacterException;
import com.tioh.validation.exceptions.VehicleIdentificationNumberFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class VehicleIdentificationNumber extends IValidatorRegistrationNumber {

    private Logger logger = LoggerFactory.getLogger(VehicleIdentificationNumber.class);

    private List<Integer>        weightsTable;
    private Map<String, Integer> transliterationTable;
    private Pattern              pattern;
    Function<String, Boolean> lengthState = (str) -> str.length() == 17;

    private VehicleIdentificationNumber() {
        super();
        setName("Vehicle identification number");
        String regex = "^[A-HJ-NPR-Z\\d]{8}[\\dX][A-HJ-NPR-Z\\d]{2}\\d{6}$";
        pattern = Pattern.compile(regex);
        weightsTable = Arrays.asList(8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2);
        transliterationTable = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>("0", 0),
                new AbstractMap.SimpleImmutableEntry<>("1", 1),
                new AbstractMap.SimpleImmutableEntry<>("2", 2),
                new AbstractMap.SimpleImmutableEntry<>("3", 3),
                new AbstractMap.SimpleImmutableEntry<>("4", 4),
                new AbstractMap.SimpleImmutableEntry<>("5", 5),
                new AbstractMap.SimpleImmutableEntry<>("6", 6),
                new AbstractMap.SimpleImmutableEntry<>("7", 7),
                new AbstractMap.SimpleImmutableEntry<>("8", 8),
                new AbstractMap.SimpleImmutableEntry<>("9", 9),
                new AbstractMap.SimpleImmutableEntry<>("A", 1),
                new AbstractMap.SimpleImmutableEntry<>("B", 2),
                new AbstractMap.SimpleImmutableEntry<>("C", 3),
                new AbstractMap.SimpleImmutableEntry<>("D", 4),
                new AbstractMap.SimpleImmutableEntry<>("E", 5),
                new AbstractMap.SimpleImmutableEntry<>("F", 6),
                new AbstractMap.SimpleImmutableEntry<>("G", 7),
                new AbstractMap.SimpleImmutableEntry<>("H", 8),
                new AbstractMap.SimpleImmutableEntry<>("J", 1),
                new AbstractMap.SimpleImmutableEntry<>("K", 2),
                new AbstractMap.SimpleImmutableEntry<>("L", 3),
                new AbstractMap.SimpleImmutableEntry<>("M", 4),
                new AbstractMap.SimpleImmutableEntry<>("N", 5),
                new AbstractMap.SimpleImmutableEntry<>("P", 7),
                new AbstractMap.SimpleImmutableEntry<>("R", 9),
                new AbstractMap.SimpleImmutableEntry<>("S", 2),
                new AbstractMap.SimpleImmutableEntry<>("T", 3),
                new AbstractMap.SimpleImmutableEntry<>("U", 4),
                new AbstractMap.SimpleImmutableEntry<>("V", 5),
                new AbstractMap.SimpleImmutableEntry<>("W", 6),
                new AbstractMap.SimpleImmutableEntry<>("X", 7),
                new AbstractMap.SimpleImmutableEntry<>("Y", 8),
                new AbstractMap.SimpleImmutableEntry<>("Z", 9))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static class Singleton {
        private static final VehicleIdentificationNumber instance = new VehicleIdentificationNumber();
    }

    static synchronized VehicleIdentificationNumber getInstance() {
        return Singleton.instance;
    }

    @Override
    boolean isValid(String vin) {

        logger.info("Validate {}: {}", name, vin);
        return checkEmpty()
                .andThen(cleanNumber())
                .andThen(checkLength(lengthState))
                .andThen(checkPattern())
                .andThen(validate())
                .apply(vin);
    }

    Function<String, Boolean> validate() {
        return ctx -> {
            try {
                String upperVin = ctx.toUpperCase();
                int sum = IntStream.range(0, ctx.length())
                        .map(i -> transliterationTable.get(Character.toString(upperVin.charAt(i))) * weightsTable.get(i))
                        .sum();
                int key    = 11;
                int result = sum % key;

                String checkDigit = Character.toString(upperVin.charAt(8));
                if (!(result == 10 ? checkDigit.equals("X") : Integer.parseInt(checkDigit) == result))
                    throw new VehicleIdentificationNumberFormatException();
                return true;
            } catch (NumberFormatException e) {
                logger.debug("Check Digit({}) must be 'x' or number.", ctx.charAt(8));
                throw e;
            }
        };
    }

    Function<String, String> checkPattern() {
        return ctx -> {
            if (!pattern.matcher(ctx.toUpperCase()).matches()) {
                logger.debug("Vehicle identification number contains Wrong character: {}", ctx);
                throw new IllegalCharacterException();
            }
            return ctx;
        };
    }
}
