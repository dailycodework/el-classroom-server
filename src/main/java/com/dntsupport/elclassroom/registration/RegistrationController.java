package com.dntsupport.elclassroom.registration;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final RegistrationService  registrationService;

    @PostMapping
    public String register(@RequestBody Registration user){
        return registrationService.register(user);
    }

}
