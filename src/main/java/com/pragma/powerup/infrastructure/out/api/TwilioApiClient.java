package com.pragma.powerup.infrastructure.out.api;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TwilioApiClient {

    @Value("${twilio.accountSid}")
    private String accountSid;
    @Value("${twilio.authToken}")
    private String authToken;
    @Value("${twilio.phoneNumber}")
    private String phoneNumber;

    public void sendSMS(String toPhoneNumber, String message) {
        Twilio.init(accountSid, authToken);
        Message.creator(
                        new PhoneNumber(toPhoneNumber), // Destination
                        new PhoneNumber(phoneNumber), // Origin
                        message // Message
                )
                .create();
    }
}
