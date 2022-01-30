package com.dntsupport.elclassroom.registration;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class Registration {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Registration(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
