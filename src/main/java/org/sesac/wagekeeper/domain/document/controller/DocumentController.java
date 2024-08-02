package org.sesac.wagekeeper.domain.document.controller;

import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.domain.document.dto.request.HtmlRequestDto;
import org.sesac.wagekeeper.domain.document.service.DocumentService;
import org.sesac.wagekeeper.domain.message.entity.Message;
import org.sesac.wagekeeper.domain.message.repository.MessageRepository;
import org.sesac.wagekeeper.domain.workspace.entity.Workspace;
import org.sesac.wagekeeper.domain.workspace.repository.WorkspaceRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/document")
public class DocumentController {
    private final DocumentService documentService;
    private final MessageRepository messageRepository;
    private final WorkspaceRepository workspaceRepository;
    @PostMapping()
    public ResponseEntity<InputStreamResource> convertHtmltoPdf(@RequestBody HtmlRequestDto htmlrequest) throws IOException {
        File pdfFile = documentService.convertHtmltoPdf(htmlrequest.getHtml());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(pdfFile));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "document.pdf");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
    @GetMapping("/{workspaceId}")
    public ResponseEntity<InputStreamResource> getPdf(@PathVariable("workspaceId") Long workspaceId) throws IOException {
        Optional<Workspace> safeWorkspace = workspaceRepository.findById(workspaceId);
        if(safeWorkspace.isEmpty()) throw new RuntimeException("");

        List<Message> allByWorkspaceOrderByCreatedAtDesc = messageRepository.findAllByWorkspaceOrderByCreatedAtDesc(safeWorkspace.get());

        String html = allByWorkspaceOrderByCreatedAtDesc.get(0).getContent();

        File pdfFile = documentService.convertHtmltoPdf(html);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(pdfFile));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "document.pdf");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
