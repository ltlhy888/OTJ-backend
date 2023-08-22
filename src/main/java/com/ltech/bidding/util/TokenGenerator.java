package com.ltech.bidding.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TokenGenerator {
    public String generateDigitsToken() {
            // It will generate 6 digit random Number.
            // from 0 to 999999
            Random rnd = new Random();
            int number = rnd.nextInt(999999);

            // this will convert any number sequence into 6 character.
            return String.format("%06d", number);
    }
}
