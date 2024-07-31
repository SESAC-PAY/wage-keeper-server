package org.sesac.wagekeeper.domain.workinglog.controller;


import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.domain.workinglog.dto.WorkingLogFromClient;
import org.sesac.wagekeeper.domain.workinglog.service.WorkingLogService;
import org.sesac.wagekeeper.global.common.SuccessCode;
import org.sesac.wagekeeper.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/workinglog")
@RestController
public class WorkingLogController {
    private final WorkingLogService workingLogService;
    @PostMapping("/generation")
    public ResponseEntity<SuccessResponse<?>> makeLog(@RequestBody WorkingLogFromClient workingLogFromClient) {
        workingLogService.makeLog(workingLogFromClient);
        return SuccessResponse.ok("");
    }
}
