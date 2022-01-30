package com.dntsupport.elclassroom.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

@Component
@AllArgsConstructor
public class EmailValidator {

    public boolean isValidEmail(String email){
        try {
            InternetAddress address = new InternetAddress(email);
            address.validate();
        } catch (AddressException e) {
            e.printStackTrace();
        }
        return true;
    }
}
