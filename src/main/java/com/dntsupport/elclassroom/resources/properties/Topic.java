package com.dntsupport.elclassroom.resources.properties;

import com.dntsupport.elclassroom.resources.ElResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Topic {
    @Id
    @Column(name = "topic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "topic", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ElResource> resources;

    public Topic(String name) {
        this.name = name;
    }
}
