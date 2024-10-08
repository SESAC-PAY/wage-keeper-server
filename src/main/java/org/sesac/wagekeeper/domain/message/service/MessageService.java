package org.sesac.wagekeeper.domain.message.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.Diff;
import org.sesac.wagekeeper.domain.Util.GPTConfig;
import org.sesac.wagekeeper.domain.message.dto.GptRequestDTO;
import org.sesac.wagekeeper.domain.message.dto.MessageInput;
import org.sesac.wagekeeper.domain.message.entity.Message;
import org.sesac.wagekeeper.domain.message.repository.MessageRepository;
import org.sesac.wagekeeper.domain.workspace.entity.Workspace;
import org.sesac.wagekeeper.domain.workspace.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class MessageService {
    private final MessageRepository messageRespository;
    private final WorkspaceRepository workspaceRepository;

    @Value("${openai.api.key}")
    private String apiKey;

    public MessageService(MessageRepository messageRespository, WorkspaceRepository workspaceRepository) {
        this.messageRespository = messageRespository;
        this.workspaceRepository = workspaceRepository;
    }

    public void saveMessage(MessageInput message) {
        Optional<Workspace> safeWorkspace = workspaceRepository.findById(message.getWorkspaceId());
        if(safeWorkspace.isEmpty()) throw new RuntimeException("No workspace id " + message.getWorkspaceId());

        messageRespository.save(
                Message.builder()
                        .workspace(safeWorkspace.get())
                        .role(message.getRole())
                        .content(message.getContent())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    public List<Message> getAllMessage(Long workspaceId) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if(workspace.isEmpty()) throw new RuntimeException("no workspace id " + workspaceId);

        return messageRespository.findAllByWorkspaceOrderByCreatedAtDesc(workspace.get());
    }

//    public String streamMessages(Long workspaceId, int level, boolean isFirst) {
//        Optional<Workspace> safeWorkspace = workspaceRepository.findById(workspaceId);
//        if(safeWorkspace.isEmpty()) throw new RuntimeException("No workspace id " + workspaceId);
//
//        Workspace workspace = safeWorkspace.get();
//
//        SseEmitter emitter = new SseEmitter();
//        List<Message> inputMessages = getLLMInputs(workspace, level, isFirst);
//
//        Flux<String> eventStream = getResponse(inputMessages);
//        StringBuilder llmResponse = new StringBuilder();
//
//        eventStream.subscribe(
//                event -> {
//                    try {
//                        String content = extractContent(event);
//
//                        if (!content.isEmpty()) {
//                            SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
//                                    .data(content)
//                                    .name("message");
//
//                            emitter.send(eventBuilder);
//                            llmResponse.append(content);
//                        }
//                    } catch (IOException e) {
//                        saveMessage(MessageInput.builder()
//                                .content(llmResponse.toString())
//                                .role(GPTConfig.ROLE_ASSISTANT)
//                                .workspaceId(workspaceId)
//                                .build()
//                        );
//                        emitter.completeWithError(e);
//                    }
//                },
//                error -> emitter.completeWithError(error),
//                () -> {
//                    try {
//                        emitter.send(SseEmitter.event()
//                                .data("")
//                                .name("end"));
//                        emitter.complete();
//
//                        // Save the GPT response to the database
//                        saveMessage(MessageInput.builder()
//                                .content(llmResponse.toString())
//                                .role(GPTConfig.ROLE_ASSISTANT)
//                                .workspaceId(workspaceId)
//                                .build()
//                        );
//                    } catch (IOException e) {
//                        emitter.completeWithError(e);
//                    }
//                }
//        );
//
//        return emitter;
//
//    }

    public List<Message> getLLMInputs(Workspace workspace, int level, boolean isFirst) {
        List<Message> messages = messageRespository.findAllByWorkspaceOrderByCreatedAtDesc(workspace);
        if(messages == null) throw new RuntimeException("The Messages is Null");
        //if(messages.isEmpty()) throw new RuntimeException("User input is not saved");

        List<Message> parsedDatas = new ArrayList<>();

        parsedDatas.add(Message.builder()
                .createdAt(LocalDateTime.now())
                .content(GPTConfig.getSystemPrompts(level, isFirst))
                .role("system")
                .workspace(workspace)
                .build()
        );
        if(isFirst) return parsedDatas;

        for(int i=messages.size()-1; i>=0; i--) parsedDatas.add(messages.get(i));
        return parsedDatas;
    }

    public Flux<String> getResponse(List<Message> messages) {
        for(Message message:messages) {
            System.out.println(message.getContent() + "\n");
        }
        WebClient webClient = WebClient.builder()
                .baseUrl(GPTConfig.CHAT_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(GPTConfig.AUTHORIZATION, GPTConfig.BEARER + apiKey)
                .build();

        GptRequestDTO request = GptRequestDTO.builder()
                .model(GPTConfig.CHAT_MODEL)
                .maxTokens(GPTConfig.MAX_TOKEN)
                .temperature(GPTConfig.TEMPERATURE)
                .stream(GPTConfig.STREAM)
                .messages(messages)
                .build();

        return webClient.post()
                .bodyValue(request)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
    }

    public String extractContent(String jsonEvent) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonEvent);
            return node.at(GPTConfig.RESPONSE_NODE_AT).asText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getGptOutputSync(Long workspaceId, int level, boolean isFirst) {
        Optional<Workspace> safeWorkspace = workspaceRepository.findById(workspaceId);
        if (safeWorkspace.isEmpty()) throw new RuntimeException("No workspace id " + workspaceId);

        Workspace workspace = safeWorkspace.get();

        List<Message> inputMessages = getLLMInputs(workspace, level, isFirst);

        // Get response from GPT synchronously
        String gptResponse = getResponseSync(inputMessages);

        // Save the GPT response to the database
        saveMessage(MessageInput.builder()
                .content(gptResponse)
                .role(GPTConfig.ROLE_ASSISTANT)
                .workspaceId(workspaceId)
                .build()
        );

        return gptResponse;
    }

    public String getResponseSync(List<Message> messages) {

//        ExchangeStrategies strategies = ExchangeStrategies.builder()
//                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
//                .build();

        WebClient webClient = WebClient.builder()
                .baseUrl(GPTConfig.CHAT_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(GPTConfig.AUTHORIZATION, GPTConfig.BEARER + apiKey)
//                .exchangeStrategies(strategies)
                .build();

        GptRequestDTO request = GptRequestDTO.builder()
                .model(GPTConfig.CHAT_MODEL)
                .maxTokens(GPTConfig.MAX_TOKEN)
                .temperature(GPTConfig.TEMPERATURE)
                .stream(false)
                .messages(messages)
                .build();

        String response = webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();  // Block to get the response synchronously

        return extractContent(response);
    }

    public CompletableFuture<String> collectStreamMessages(SseEmitter emitter) {
        CompletableFuture<String> resultFuture = new CompletableFuture<>();
        StringBuilder collectedText = new StringBuilder();

        emitter.onCompletion(() -> resultFuture.complete(collectedText.toString()));

        emitter.onTimeout(() -> resultFuture.completeExceptionally(new RuntimeException("SSE stream timed out")));

        emitter.onError(throwable -> resultFuture.completeExceptionally(throwable));

        return resultFuture;
    }

    public void addEvent(SseEmitter emitter, String content) throws IOException {
        SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
                .data(content)
                .name("message");
        emitter.send(eventBuilder);
    }

    public void completeEmitter(SseEmitter emitter) throws IOException {
        emitter.send(SseEmitter.event().data("").name("end"));
        emitter.complete();
    }

    public CompletableFuture<String> streamMessages(Long workspaceId, int level, boolean isFirst) {
        Optional<Workspace> safeWorkspace = workspaceRepository.findById(workspaceId);
        if (safeWorkspace.isEmpty()) throw new RuntimeException("No workspace id " + workspaceId);

        Workspace workspace = safeWorkspace.get();

        SseEmitter emitter = new SseEmitter();
        List<Message> inputMessages = getLLMInputs(workspace, level, isFirst);

        Flux<String> eventStream = getResponse(inputMessages);
        StringBuilder llmResponse = new StringBuilder();

        CompletableFuture<String> resultFuture = new CompletableFuture<>();

        eventStream.subscribe(
                event -> {
                    try {
                        String content = extractContent(event);

                        if (!content.isEmpty()) {
                            SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
                                    .data(content)
                                    .name("message");

                            emitter.send(eventBuilder);
                            llmResponse.append(content);
                        }
                    } catch (IOException e) {
                        saveMessage(MessageInput.builder()
                                .content(llmResponse.toString())
                                .role(GPTConfig.ROLE_ASSISTANT)
                                .workspaceId(workspaceId)
                                .build()
                        );
                        emitter.completeWithError(e);
                        resultFuture.completeExceptionally(e);
                    }
                },
                error -> {
                    emitter.completeWithError(error);
                    resultFuture.completeExceptionally(error);
                },
                () -> {
                    try {
                        emitter.send(SseEmitter.event()
                                .data("")
                                .name("end"));
                        emitter.complete();

                        // Save the GPT response to the database
                        saveMessage(MessageInput.builder()
                                .content(llmResponse.toString())
                                .role(GPTConfig.ROLE_ASSISTANT)
                                .workspaceId(workspaceId)
                                .build()
                        );

                        resultFuture.complete(llmResponse.toString());
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                        resultFuture.completeExceptionally(e);
                    }
                }
        );

        return resultFuture;
    }
}
