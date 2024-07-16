package org.home.gen.ai.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.home.gen.ai.service.EmbeddingService;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingServiceImpl implements EmbeddingService {

  private final VectorStore vectorStore;
  private final PdfDocumentReaderConfig config =
      PdfDocumentReaderConfig.builder()
          .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().build())
          .build();

  @Override
  public boolean addDocument(Resource file) {
    var pdfReader = new PagePdfDocumentReader(file, config);
    var textSplitter = new TokenTextSplitter();

    try {
      vectorStore.accept(textSplitter.apply(pdfReader.get()));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
    return true;
  }

  @Override
  public List<Document> similaritySearch(String searchText) {
    var searchRequest = SearchRequest.query(searchText);

    searchRequest.withTopK(3);
    searchRequest.withSimilarityThreshold(0.8);
    return vectorStore.similaritySearch(searchRequest);
  }
}
