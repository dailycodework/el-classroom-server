package com.dntsupport.elclassroom.resources;

import com.dntsupport.elclassroom.resources.properties.Category;
import com.dntsupport.elclassroom.resources.properties.Topic;
import com.dntsupport.elclassroom.user.ElUser;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "file")
public class ElResource {
    @Id
    @Column(name = "resource_id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "topic")
    private Topic topic;

    private String grade;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user")
    private ElUser user;
    @Lob
    private byte[] data;
    private String type;

    @Column(name = "upload_dir")
    private String uploadDir;
    private String description;

    public ElResource(String name, Topic topic, String grade, Category category,
                      byte[] data,String description) {
        this.name = name;
        this.topic = topic;
        this.grade = grade;
        this.category = category;
        this.data = data;
        this.description = description;
    }
}
