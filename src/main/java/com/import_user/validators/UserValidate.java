package com.import_user.validators;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class UserValidate {
    public static boolean isAdult(Date birthDate) {
        LocalDate localBirthDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        Period period = Period.between(localBirthDate, now);
        return period.getYears() >= 18;
    }
}
