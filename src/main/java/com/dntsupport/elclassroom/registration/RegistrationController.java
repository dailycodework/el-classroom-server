package com.dntsupport.elclassroom.registration;


import com.dntsupport.elclassroom.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;




@AllArgsConstructor
@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final RegistrationService  registrationService;
    private ConfirmationTokenService tokenService;

    @PostMapping
    public String register(@RequestBody Registration user){
        return registrationService.register(user);
    }

    @GetMapping("/confirm-email")
    public String confirm(@RequestParam String token){
        return registrationService.confirmRegistration(token);
    }
}
