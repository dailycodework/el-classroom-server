package com.dntsupport.elclassroom.user;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class FullName {
    private String firstName;
    private String lastName;
}
