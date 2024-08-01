package org.sesac.wagekeeper.domain.document.controller;

import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.domain.document.dto.request.HtmlRequestDto;
import org.sesac.wagekeeper.domain.document.service.DocumentService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/document")
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping()
    public ResponseEntity<InputStreamResource> convertHtmltoPdf(@RequestBody HtmlRequestDto htmlrequest) throws IOException {
        File pdfFile = documentService.convertHtmltoPdf(htmlrequest.getHtml());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(pdfFile));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "document.pdf");

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }



}
