package com.tioh.validation;

import com.tioh.validation.exceptions.EmptyStringException;
import com.tioh.validation.exceptions.SizeLimitException;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

abstract class IValidatorRegistrationNumber {

    @Setter
    String name;
    abstract boolean isValid(String vrn);

    Function<String, String> checkEmpty() {
        return ctx -> {
            if (StringUtils.isEmpty(ctx)) throw new EmptyStringException(String.format("%s is empty.", name));
            return ctx;
        };
    }

    Function<String, String> cleanNumber() {
        return ctx -> ctx.replaceAll("-", "").replaceAll(" ", "");
    }

    Function<String, String> checkLength(Function<String, Boolean> predicate) {
        return ctx -> {
            if (!predicate.apply(ctx)) {
                throw new SizeLimitException(String.format("Wrong length for %s (%s), length: %d", name, ctx, ctx.length()));
            }
            return ctx;
        };
    }

    int getInt(char c) {
        return Integer.parseInt(Character.toString(c));
    }
}
