package com.ltech.bidding.service.dto.auth;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
@JsonSerialize
public class LoginRequestDto {
    @Value("password.salt")
    private String salt;
    @NotNull
    private String email;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginRequestDto() {}

    @NotNull
    private String password;
    private String key;

    public String getPassword() {
        return password + salt;
    }
}
