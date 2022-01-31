package com.dntsupport.elclassroom.registration.token;

import com.dntsupport.elclassroom.user.ElUser;
import com.dntsupport.elclassroom.user.ElUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository tokenRepository;
    private final ElUserRepository elUserRepository;
    public void saveToken(ConfirmationToken token){
        tokenRepository.save(token);
    }


    public Optional<ConfirmationToken> getToken(String token) {
     return tokenRepository.findByToken(token);

    }
    public int setConfirmedAt(String token) {
        return tokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    @Transactional
    public void deleteUserToken(ElUser user){
      var theUser =  elUserRepository.findByEmail(user.getEmail());
      if (theUser.isPresent()){
          tokenRepository.deleteUserToken(user);
      }
    }

}
