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

@RestController
@RequestMapping("/api/message")
@AllArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/stream/{workspaceId}")
    public SseEmitter streamMessages(@PathVariable("workspaceId") Long workspaceId) {
        return messageService.streamMessages(workspaceId);
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
