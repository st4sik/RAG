package org.home.gen.ai.service.impl;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.home.gen.ai.service.ChatService;
import org.home.gen.ai.service.EmbeddingService;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

  @Value("classpath:/prompts/answerquestion.st")
  private Resource promptResource;

  private AzureOpenAiChatOptions chatOptions;

  private final ChatClient.Builder builder;
  private final EmbeddingService embeddingService;

  @PostConstruct
  public void init() {
    chatOptions =
        AzureOpenAiChatOptions.builder()
            .withFrequencyPenalty(0.0F)
            .withPresencePenalty(0.0F)
            .withTopP(1.0F)
            .withTemperature(0.2F)
            .withMaxTokens(1024)
            .build();
  }

  @Override
  public String generateAnswer(String query) {

    var documents = embeddingService.similaritySearch(query);
    var context =
        documents.stream()
            .map(Document::getContent)
            .collect(Collectors.joining(System.lineSeparator()));

    var systemPromptTemplate = new SystemPromptTemplate(promptResource);
    var systemMessage = systemPromptTemplate.createMessage(Map.of("context", context));
    var userPromptTemplate = new PromptTemplate("User's question {question}");
    var userMessage = userPromptTemplate.createMessage(Map.of("question", query));

    var prompt = new Prompt(List.of(systemMessage, userMessage), chatOptions);

    return builder
        .build()
        .prompt(prompt)
        .call()
        .chatResponse()
        .getResult()
        .getOutput()
        .getContent();
  }
}
