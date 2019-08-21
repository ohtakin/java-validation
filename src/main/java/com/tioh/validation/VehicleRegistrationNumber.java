package com.tioh.validation;

import com.tioh.validation.exceptions.AreaSignException;
import com.tioh.validation.exceptions.UsageSignException;
import com.tioh.validation.exceptions.VehicleRegistrationNumberFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class VehicleRegistrationNumber extends IValidatorRegistrationNumber {

    private Logger logger = LoggerFactory.getLogger(VehicleRegistrationNumber.class);

    private final List<String> USAGE_SIGN;
    private final List<String> AREA_SIGN;
    private final Pattern      pattern;
    private final Pattern      pattern2;
    Function<String, Boolean> lengthState = (str) -> str.length() == 7 || str.length() == 9;

    private VehicleRegistrationNumber() {
        super();
        setName("Vehicle registration number");
        BinaryOperator<String> reduceOperator = (p1, p2) -> p1 + "|" + p2;
        String regex = "^(\\d{2})([ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{1,2})(\\d{4})$";
        String regex2 = "^([ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{2})(\\d{2})([ㄱ-ㅎ|ㅏ-ㅣ|가-힣]{1,2})(\\d{4})$";
        USAGE_SIGN = Arrays.asList("가", "나", "다", "라", "마", "거", "너", "더", "러", "머", "버", "서", "어", "저", "고", "노", "도", "로", "모", "보", "소", "오", "조", "구", "누", "두", "루", "무", "부", "수", "우", "주", "바", "사", "아", "자", "허", "배", "호", "하", "준외", "준영", "국기", "협정");
        AREA_SIGN = Arrays.asList("경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "서울", "인천", "대전", "부산", "대구", "광주", "울산", "세종");
        String usage   = USAGE_SIGN.stream().reduce(reduceOperator).orElse("");
        String area   = AREA_SIGN.stream().reduce(reduceOperator).orElse("");
        this.pattern = Pattern.compile(String.format(regex, usage));
        this.pattern2 = Pattern.compile(String.format(regex2, area, usage));
    }

    private static class Singleton {
        private static final VehicleRegistrationNumber instance = new VehicleRegistrationNumber();
    }

    static synchronized VehicleRegistrationNumber getInstance() {
        return Singleton.instance;
    }


    @Override
    boolean isValid(String vrn) {

        logger.debug("Validate {}: {}", name, vrn);

        return checkEmpty()
                .andThen(cleanNumber())
                .andThen(checkLength(lengthState))
                .andThen(checkSign())
                .andThen(checkPattern())
                .apply(vrn);
    }

    Function<String, String> checkSign() {
        return matcher(pattern, "$2", USAGE_SIGN)
                .andThen(matcher(pattern2, "$3", USAGE_SIGN))
                .andThen(matcher(pattern2, "$1", AREA_SIGN, AreaSignException.class));
    }

    Function<String, String> matcher(Pattern pattern, String idx, List<String> signs, Class<? extends IllegalArgumentException> clazz) {
        return ctx -> {
            Matcher matcher = pattern.matcher(ctx);
            if (matcher.matches()) {
                String result = matcher.replaceAll(idx);
                logger.debug("usage sign: {}", result);
                try {
                    if (!signs.contains(result)) {
                        throw clazz.newInstance();
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("error: {}", e.getMessage());
                }
            }
            return ctx;
        };
    }

    Function<String, String> matcher(Pattern pattern, String idx, List<String> signs) {
        return matcher(pattern, idx, signs, UsageSignException.class);
    }

    Function<String, Boolean> checkPattern() {
        return ctx -> {
            if (!(pattern.matcher(ctx).matches() || pattern2.matcher(ctx).matches())) {
                throw new VehicleRegistrationNumberFormatException();
            }
            return true;
        };
    }
}
