package com.dntsupport.elclassroom.message;

import com.dntsupport.elclassroom.resources.properties.Topic;
import lombok.Data;

@Data
public class ResponseFile {
  private String name;
  private Topic topic;
  private String grade;
  private String url;
  private String type;
  private long size;

  public ResponseFile(String name, Topic topic, String grade, String url, String type, long size) {
    this.name = name;
    this.topic = topic;
    this.grade = grade;
    this.url = url;
    this.type = type;
    this.size = size;
  }
}