package com.example.exceptionhandlingservice1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @author Heshan Karunaratne
 */
@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class UserRequest {
    @NotNull(message = "Name should not be null")
    private String name;
    @Email(message = "Invalid Email")
    private String email;
    @NotNull
    @Pattern(regexp = "^\\d{10}$", message = "Invalid Mobile Number")
    private String mobile;
    private String gender;
    @Min(18)
    @Max(60)
    private int age;
    @NotBlank
    private String nationality;
}
