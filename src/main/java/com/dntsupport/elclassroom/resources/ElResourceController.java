package com.dntsupport.elclassroom.resources;

import com.dntsupport.elclassroom.message.ResponseFile;
import com.dntsupport.elclassroom.message.ResponseMessage;
import com.dntsupport.elclassroom.resources.properties.Category;
import com.dntsupport.elclassroom.resources.properties.Topic;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/resources")
public class ElResourceController {

    private ElResourceService resourceService;

    @PostMapping
    private ResponseEntity<ResponseMessage> createResource(
                                      @RequestParam("topic") Topic topic,
                                      @RequestParam("grade") String grade,
                                      @RequestParam("category") Category category,
                                      @RequestParam("data")MultipartFile data,
                                      @RequestParam("description") String description) throws IOException {
        String message = "";
        try {
            resourceService.create(topic, grade,category,data, description);
            message = "Uploaded the file successfully: " + data.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + data.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/all-resources")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> resources = resourceService.getAllFiles().map(res -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/uploaded/resources/")
                    .path(String.valueOf(res.getId()))
                    .toUriString();
            return new ResponseFile(
                    res.getName(),
                    res.getTopic(),
                    res.getGrade(),
                    fileDownloadUri,
                    res.getType(),
                    res.getData().length);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }

    @GetMapping("/{id}")
    public ResponseFile getFileById(@PathVariable("id") String id) {

        ElResource res = resourceService.getFile(id);
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/uploaded/resources/")
                    .path(String.valueOf(((ElResource) res).getId())).toUriString();
            return new ResponseFile(
                    res.getName(),
                    res.getTopic(),
                    res.getGrade(),
                    fileDownloadUri,
                    res.getType(),
                    res.getData().length);

    }







  /*  @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        ElResource resource = resourceService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getName() + "\"")
                .body(resource.getData());
    }*/
}
