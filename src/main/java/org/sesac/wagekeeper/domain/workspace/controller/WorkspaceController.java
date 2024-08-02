package org.sesac.wagekeeper.domain.workspace.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.coyote.Response;
import org.sesac.wagekeeper.domain.workspace.service.WorkspaceService;
import org.sesac.wagekeeper.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workspace")
@AllArgsConstructor
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @PostMapping("/generation/{userId}")
    public ResponseEntity<SuccessResponse<?>> generateWorkspace(@PathVariable("userId") Long userId) {
        Long id = workspaceService.generateWorkspace(userId);
        return SuccessResponse.ok(id);
    }
}
