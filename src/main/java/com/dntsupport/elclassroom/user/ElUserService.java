package com.dntsupport.elclassroom.user;

import com.dntsupport.elclassroom.exception.UserAlreadyExistsException;
import com.dntsupport.elclassroom.message.UserMessage;
import com.dntsupport.elclassroom.registration.token.ConfirmationToken;
import com.dntsupport.elclassroom.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ElUserService  implements UserDetailsService {

    private final ElUserRepository elUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ElUser elUser = elUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(UserMessage.USER_NOT_FOUND, email)));
        return new ElUserSecurity(elUser);
    }
    public String signUpUser(ElUser user){
        // check if the email provided by the user is already taken
        boolean userAlreadyExists = elUserRepository.findByEmail(user.getEmail()).isPresent();
        if(userAlreadyExists){
            throw new UserAlreadyExistsException(String.format(UserMessage.USER_ALREADY_EXISTS, user.getEmail()));
        }
        // get the user password encoded and save the user
        var encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        elUserRepository.save(user);

        //Create and send confirmation token for user
        // to confirmation the registration in order to enable the newly created account.
        var token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user);
         tokenService.saveToken(confirmationToken);
         // Send a confirmation email to the user
        return token;
    }

    public int enableUser(String email) {
        return elUserRepository.enableUser(email);
    }

    public Stream<ElUser> getUsers() {
     return elUserRepository.findAll().stream();
    }

    public ElUser getUser(String email) {
        return elUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(UserMessage.USER_NOT_FOUND, email)));
    }

    public int disableUser(String email) {
        return elUserRepository.disableUser(email);
    }

    @Transactional
    public void deleteUser(String email){
        var theUser = this.getUser(email);
        tokenService.deleteUserToken(theUser);
        elUserRepository.delete(theUser);

    }

}
