package com.tioh.validation;

import com.tioh.validation.exceptions.BusinessRegistrationNumberFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.stream.IntStream;

class BusinessRegistrationNumber extends IValidatorRegistrationNumber {

    private Logger logger = LoggerFactory.getLogger(BusinessRegistrationNumber.class);
    Function<String, Boolean> lengthState = (str) -> str.length() == 10;
    private BusinessRegistrationNumber() {
        super();
        setName("Business registration number");
    }

    private static class Singleton {
        private static final BusinessRegistrationNumber instance = new BusinessRegistrationNumber();
    }

    static synchronized BusinessRegistrationNumber getInstance() {
        return Singleton.instance;
    }

    @Override
    boolean isValid(String brn) {

        logger.debug("Validate {}: {}", name, brn);

        return checkEmpty()
                .andThen(cleanNumber())
                .andThen(checkLength(lengthState))
                .andThen(validate())
                .apply(brn);
    }

    Function<String, Boolean> validate() {
        return ctx -> {
            try {
                Integer.parseInt(ctx);
                int sum = IntStream.range(0, ctx.length())
                        .map(i -> getNumberTransform(i, ctx.charAt(i)))
                        .sum();

                int result = sum + getInt(ctx.charAt(8)) * 5 / 10;

                if (!(result % 10 == 0 && result != 0)) throw new BusinessRegistrationNumberFormatException();
                return true;
            } catch (NumberFormatException e) {
                logger.debug("Business registration number contains not a number: {}", ctx);
                throw e;
            }
        };
    }

    int getNumberTransform(int idx, char c) {

        int num = getInt(c);
        switch (idx) {
            case 1:
            case 4:
            case 7:
                return num * 3 % 10;
            case 2:
            case 5:
                return num * 7 % 10;
            case 3:
            case 6:
                return num % 10;
            case 8:
                return num * 5 % 10;
            default:
                return num;
        }
    }
}