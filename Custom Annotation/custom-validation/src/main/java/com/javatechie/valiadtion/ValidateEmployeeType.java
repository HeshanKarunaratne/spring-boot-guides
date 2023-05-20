package com.javatechie.valiadtion;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Heshan Karunaratne
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EmployeeTypeValidator.class)
public @interface ValidateEmployeeType {

    String message() default "Invalid EmployeeType: It should be either Permanent or Vendor";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
