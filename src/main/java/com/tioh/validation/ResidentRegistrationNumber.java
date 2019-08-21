package com.tioh.validation;

import com.tioh.validation.exceptions.GenderCodeException;
import com.tioh.validation.exceptions.ResidentRegistrationNumberFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

class ResidentRegistrationNumber extends IValidatorRegistrationNumber {

    private Logger        logger = LoggerFactory.getLogger(ResidentRegistrationNumber.class);
    private List<Integer> genderCode;
    private List<Integer> weightsTable;
    Function<String, Boolean> lengthState = (str) -> str.length() == 13;

    private ResidentRegistrationNumber() {
        super();
        setName("Resident registration number");
        genderCode = Arrays.asList(1, 2, 3, 4);
        weightsTable = Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5);
    }

    private static class Singleton {
        private static final ResidentRegistrationNumber instance = new ResidentRegistrationNumber();
    }

    static synchronized ResidentRegistrationNumber getInstance() {
        return Singleton.instance;
    }

    @Override
    boolean isValid(String rrn) {

        logger.debug("Validate Resident registration number: {}", rrn);

        return checkEmpty()
                .andThen(cleanNumber())
                .andThen(checkLength(lengthState))
                .andThen(checkGenderCode())
                .andThen(validate())
                .apply(rrn);
    }

    Function<String, Boolean>  validate() {
        return ctx -> {
            try {
                Long.parseLong(ctx);
                int sum = IntStream.range(0, weightsTable.size())
                        .map(i -> getInt(ctx.charAt(i)) * weightsTable.get(i))
                        .sum();

                int key    = 11;
                int result = key - sum % key;
                if (!(result == getInt(ctx.charAt(12)))) throw new ResidentRegistrationNumberFormatException();
                return true;
            } catch (NumberFormatException e) {
                logger.debug("Resident registration number contains not a number. : {}",ctx);
                throw e;
            }
        };
    }

    Function<String, String> checkGenderCode() {
        return ctx -> {
            int genderCode = getInt(ctx.charAt(6));
            if (!this.genderCode.contains(genderCode)) {
                logger.debug("Wrong genderCode code for Resident registration number: {}", genderCode);
                throw new GenderCodeException();
            }
            return ctx;
        };
    }
}