package org.sesac.wagekeeper.domain.company.controller;


import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.domain.company.service.CompanyService;
import org.sesac.wagekeeper.global.common.SuccessCode;
import org.sesac.wagekeeper.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/company")
@RestController
public class CompanyController {
    private final CompanyService companyService;
    @PostMapping("/registration/{companyId}/{userId}")
    public ResponseEntity<SuccessResponse<?>> joinCompany(@PathVariable("companyId") Long companyId, @PathVariable("userId") Long userId) {
        companyService.joinCompany(companyId, userId);
        return SuccessResponse.ok("");
    }
}
