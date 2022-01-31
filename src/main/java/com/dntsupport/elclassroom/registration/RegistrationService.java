package com.dntsupport.elclassroom.registration;

import com.dntsupport.elclassroom.exception.InvalidInputException;
import com.dntsupport.elclassroom.exception.TokenNotFoundException;
import com.dntsupport.elclassroom.mail.JEMailSender;
import com.dntsupport.elclassroom.message.UserMessage;
import com.dntsupport.elclassroom.registration.token.ConfirmationToken;
import com.dntsupport.elclassroom.registration.token.ConfirmationTokenService;
import com.dntsupport.elclassroom.user.ElUser;
import com.dntsupport.elclassroom.user.ElUserService;
import com.dntsupport.elclassroom.user.UserRole;
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
    private final JEMailSender jeMailSender;


    public String register(Registration registration) {
        // check if the user provided email address is valid
        if (!validator.isValidEmail(registration.getEmail())){
            throw new InvalidInputException(String.format(UserMessage.INVALID_EMAIL_ADDRESS, registration.getEmail()));
        }
        String userToken = elUserService.signUpUser(
                new ElUser(
                        registration.getFirstName(),
                        registration.getLastName(),
                        registration.getEmail(),
                        registration.getPassword(),
                        UserRole.USER));
        String link = "http://localhost:8081/api/registration/confirm-email?token="+userToken;
        jeMailSender.send(
                registration.getEmail(),
                buildEmail(registration.getFirstName(), link));
        return userToken;
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

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
