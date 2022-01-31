package com.dntsupport.elclassroom.user;

import com.dntsupport.elclassroom.resources.ElResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ElUser {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
    private boolean isEnabled = false;

   @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   @JsonIgnore
    private List<ElResource> resources;

    public ElUser(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
