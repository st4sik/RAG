package org.home.gen.ai.service;

import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

public interface EmbeddingService {
  boolean addDocument(Resource uploadedFile);

  List<Document> similaritySearch(String searchText);
}
