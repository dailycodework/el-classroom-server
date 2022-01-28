package com.dntsupport.elclassroom.resources;

import com.dntsupport.elclassroom.resources.properties.Category;
import com.dntsupport.elclassroom.resources.properties.Topic;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ElResourceService {

    private final ElResourceRepo repository;
    private final Path uploadDir;

    @Autowired
    public ElResourceService(ElResourceRepo repository, ElResource elResource) throws FileUploadException {
        this.repository = repository;
        this.uploadDir = Paths.get(elResource
                .getUploadDir()).toAbsolutePath().normalize();
        try{
            Files.createDirectories(this.uploadDir);
        }catch(IOException ex){
            throw new FileUploadException("Error creating directory");
        }
    }


    public void create(Topic topic, String grade, Category category,
                       MultipartFile data, String description) throws IOException {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(data.getOriginalFilename()));
        Path location = this.uploadDir.resolve(originalFileName);
        Files.copy(data.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);
        ElResource e = new ElResource(originalFileName,topic,grade,category,data.getBytes(),description);
        e.setType(data.getContentType());
        e.setUploadDir(String.valueOf(this.uploadDir));
        repository.save(e);
    }
    public ElResource getFile(String id) {
        return repository.findById(id).orElseThrow();
    }

    public Stream<ElResource> getAllFiles() {
        return repository.findAll().stream();
    }
}
