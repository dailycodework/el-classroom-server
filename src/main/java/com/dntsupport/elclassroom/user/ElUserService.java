package com.dntsupport.elclassroom.user;

import com.dntsupport.elclassroom.exception.UserAlreadyExistsException;
import com.dntsupport.elclassroom.message.UserMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ElUserService  implements UserDetailsService {

    private final ElUserRepository elUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return elUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(UserMessage.USER_NOT_FOUND, email)));
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
        return "It works!";
    }
}
