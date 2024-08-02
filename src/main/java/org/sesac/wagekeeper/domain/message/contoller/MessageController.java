package org.sesac.wagekeeper.domain.message.contoller;

import lombok.AllArgsConstructor;
import org.sesac.wagekeeper.domain.message.dto.MessageInput;
import org.sesac.wagekeeper.domain.message.entity.Message;
import org.sesac.wagekeeper.domain.message.service.MessageService;
import org.sesac.wagekeeper.domain.workspace.entity.Workspace;
import org.sesac.wagekeeper.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/message")
@AllArgsConstructor
public class MessageController {
    private final MessageService messageService;


    @GetMapping("/stream/{workspaceId}/{level}/{isFirst}")
    public String streamMessages(@PathVariable("workspaceId") Long workspaceId,
                                 @PathVariable("level") int level,
                                 @PathVariable("isFirst") boolean isFirst) {
        CompletableFuture<String> str = messageService.streamMessages(workspaceId, level, isFirst);
        try {
            String result = str.get();
            System.out.println("output is : " + result);
            return result;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @GetMapping("/sync/{workspaceId}/{level}/{isFirst}")
    public String getGptOutputSync(@PathVariable("workspaceId") Long workspaceId, @PathVariable("level") int level, @PathVariable("isFirst") boolean isFirst) {
        return messageService.getGptOutputSync(workspaceId, level, isFirst);
    }

    @PostMapping("/send")
    public ResponseEntity<SuccessResponse<?>> sendMessage (@RequestBody MessageInput message) {
        messageService.saveMessage(message);
        return SuccessResponse.ok("");
    }

    @GetMapping("/message/{workspaceId}")
    public ResponseEntity<List<Message>> getChatPage(@PathVariable("workspaceId") Long workspaceId) {
        List<Message> messages = messageService.getAllMessage(workspaceId);
        return ResponseEntity.ok(messages);
    }

}
