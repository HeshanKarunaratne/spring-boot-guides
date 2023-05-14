package com.example.piimasking.dto;

import com.example.piimasking.encrypt.MaskData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Heshan Karunaratne
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    @MaskData
    private String ssn;
    @MaskData
    private String phoneNumber;
    private int age;
    private String username;
    @MaskData
    private String password;
    private List<AccountDetails> accountDetails;
}
