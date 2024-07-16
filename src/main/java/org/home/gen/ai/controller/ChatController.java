package org.home.gen.ai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.home.gen.ai.service.ChatService;
import org.home.gen.ai.service.EmbeddingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
  private final ChatService chatService;
  private final EmbeddingService embeddingService;

  @GetMapping("/chat")
  public ResponseEntity<String> generateAnswer(
      @RequestParam(defaultValue = "What is RAG?") String query) {
    try {
      var response = chatService.generateAnswer(query);
      if (response != null) {
        var jsonResponse = new HashMap<>();
        jsonResponse.put("response", response);

        var mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(jsonResponse);
        return ResponseEntity.ok().body(json);
      } else {
        return ResponseEntity.noContent().build();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PostMapping("/admin/document/upload")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      var result = embeddingService.addDocument(file.getResource());
      if (result) {
        return new ResponseEntity<>(HttpStatus.CREATED);
      }
    } catch (RuntimeException e) {
      log.error(e.getMessage(), e);
    }
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
