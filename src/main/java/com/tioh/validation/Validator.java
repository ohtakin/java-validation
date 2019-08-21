package com.tioh.validation;


public class Validator implements IValidator {

    private Validator() {
    }

    private static class Singleton {
        private static final Validator instance = new Validator();
    }

    public static synchronized Validator getInstance() {
        return Singleton.instance;
    }

    /**
     * 사업자번호 검증
     *
     * @param brn 사업자번호
     * @return true | false
     */
    @Override
    public boolean businessRegistrationNumber(String brn) {
        return BusinessRegistrationNumber.getInstance().isValid(brn);
    }

    /**
     * 주민등록번호 검증
     *
     * @param rrn 주민등록번호
     * @return true | false
     */
    @Override
    public boolean residentRegistrationNumber(String rrn) {
        return ResidentRegistrationNumber.getInstance().isValid(rrn);
    }

    /**
     * 차량번호 검증
     *
     * @param vnr 차량번호
     * @return true | false
     */
    @Override
    public boolean vehicleRegistrationNumber(String vnr) {
        return VehicleRegistrationNumber.getInstance().isValid(vnr);
    }

    /**
     * 차대번호 검증
     *
     * @param vin 차대번호
     * @return true | false
     */
    @Override
    public boolean vehicleIdentificationNumber(String vin) {
        return VehicleIdentificationNumber.getInstance().isValid(vin);
    }

    /**
     * 모바일번호 검증
     * @param num 모바일번호
     * @return true | false
     */
    @Override
    public boolean mobilePhoneNumber(String num) {
        return PhoneNumber.getInstance().isValid(num);
    }
}
