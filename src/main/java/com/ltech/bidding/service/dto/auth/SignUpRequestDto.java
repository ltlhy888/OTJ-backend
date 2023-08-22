package com.ltech.bidding.service.dto.auth;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
@JsonSerialize
public class SignUpRequestDto {
    @Value("password.salt")
    private String salt;

    @NotNull
    private String username;

    public SignUpRequestDto(String username, String email, String password, String ip, String key) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.ip = ip;
        this.key = key;
    }

    public SignUpRequestDto() {}

    @NotNull
    private String email;
    @NotNull
    private String password;
    private String ip;
    private String key;

    public String getPassword() {
        return password + salt;
    }
}