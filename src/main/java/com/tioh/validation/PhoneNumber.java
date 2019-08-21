package com.tioh.validation;

import com.tioh.validation.exceptions.PhoneNumberFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.regex.Pattern;

class PhoneNumber extends IValidatorRegistrationNumber {

    private Logger  logger = LoggerFactory.getLogger(PhoneNumber.class);
    private Pattern pattern;
    Function<String, Boolean> lengthState = (str) -> str.length() == 10 || str.length() == 11;
    private PhoneNumber() {
        super();
        setName("Phone number");
        this.pattern = Pattern.compile("^(0(?:2|1(?:0|1|[6-9])|[3-6][1-9]|[7-8][0]))\\d{7,8}$");
    }

    private static class Singleton {
        private static final PhoneNumber instance = new PhoneNumber();
    }

    static synchronized PhoneNumber getInstance() {
        return Singleton.instance;
    }

    boolean isValid(String num) {

        logger.debug("Validate {}: {}", name, num);

        return checkEmpty()
                .andThen(cleanNumber())
                .andThen(checkLength(lengthState))
                .andThen(checkPattern())
                .apply(num);
    }

    Function<String, Boolean> checkPattern() {
        return ctx -> {
            try {
                Integer.parseInt(ctx);
                if (!pattern.matcher(ctx).matches()) throw new PhoneNumberFormatException();
                return true;
            } catch (NumberFormatException e) {
                logger.debug("Phone number contains not a number. : {}", ctx);
                throw e;
            }
        };
    }
}
