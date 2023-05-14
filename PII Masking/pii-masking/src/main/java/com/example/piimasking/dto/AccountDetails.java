package com.example.piimasking.dto;

import com.example.piimasking.encrypt.MaskData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDetails {

    private String accountHolderName;
    @MaskData
    private String accountNumber;
    private String accountType;
}