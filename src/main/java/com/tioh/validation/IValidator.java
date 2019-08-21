package com.tioh.validation;

public interface IValidator {

    boolean businessRegistrationNumber(String brn);

    boolean residentRegistrationNumber(String rrn);

    boolean vehicleRegistrationNumber(String vnr);

    boolean vehicleIdentificationNumber(String vin);

    boolean mobilePhoneNumber(String num);
}
