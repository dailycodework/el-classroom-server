package com.dntsupport.elclassroom.registration;

import com.dntsupport.elclassroom.exception.InvalidInputException;
import com.dntsupport.elclassroom.exception.TokenNotFoundException;
import com.dntsupport.elclassroom.message.UserMessage;
import com.dntsupport.elclassroom.registration.token.ConfirmationToken;
import com.dntsupport.elclassroom.registration.token.ConfirmationTokenService;
import com.dntsupport.elclassroom.user.ElUser;
import com.dntsupport.elclassroom.user.ElUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
//@NoArgsConstructor
public class RegistrationService {

    private final EmailValidator validator;
    private final ElUserService elUserService;
    private final ConfirmationTokenService tokenService;


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

    public String confirmRegistration(String token){
     ConfirmationToken cToken = tokenService.getToken(token)
             .orElseThrow(() -> new TokenNotFoundException(UserMessage.TOKEN_NOT_FOUND));

     //Check if email already confirmed
     if (cToken.getConfirmedAt() !=null){
         throw new UnsupportedOperationException(String.format(UserMessage.REGISTRATION_ALREADY_CONFIRMED, cToken.getUser().getEmail()));
     }

      // Check if token already expired
        LocalDateTime expirationTime = cToken.getExpiresAt();
     if (expirationTime.isBefore(LocalDateTime.now())){
         throw new UnsupportedOperationException(UserMessage.EXPIRED_TOKEN);
     }

     // Set confirmation status and enable user account
     tokenService.setConfirmedAt(token);
     elUserService.enableUser(cToken.getUser().getEmail());
    return UserMessage.REGISTRATION_CONFIRMED;
    }
}
