package com.dntsupport.elclassroom.registration;

import com.dntsupport.elclassroom.exception.InvalidInputException;
import com.dntsupport.elclassroom.message.UserMessage;
import com.dntsupport.elclassroom.user.ElUser;
import com.dntsupport.elclassroom.user.ElUserService;
import com.dntsupport.elclassroom.util.EmailValidator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
//@NoArgsConstructor
public class RegistrationService {

    private final RegistrationRepository regRepository;
    private final EmailValidator validator;
    private final ElUserService elUserService;


    public String register(Registration registration) {
        // check if the user provided email address is valid
        if (!validator.isValidEmail(registration.getEmail())){
            throw new InvalidInputException(String.format(UserMessage.INVALID_EMAIL_ADDRESS, registration.getEmail()));
        }
        return elUserService.signUpUser(
                new ElUser(
                        registration.getFirstName(),
                        registration.getLastName(),
                        registration.getEmail(),
                        registration.getPassword()));
    }
}
