package org.sesac.wagekeeper.domain.company.controller;


import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.domain.company.dto.response.CompanyInfoResponseDto;
import org.sesac.wagekeeper.domain.company.dto.response.CompanyLocationDto;
import org.sesac.wagekeeper.domain.company.dto.response.CompanyResponseDto;
import org.sesac.wagekeeper.domain.company.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sesac.wagekeeper.global.common.SuccessCode;
import org.sesac.wagekeeper.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/api/company")
@RestController
public class CompanyController {


    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getCompanyInfo(@RequestParam String searchWord) {
        // 회사 세부 정보
        CompanyInfoResponseDto companyInfoResponseDto = companyService.getCompanyInfo(searchWord);
        String companyAddress = companyInfoResponseDto.address();

        // 회사 위치 정보
        CompanyLocationDto companyLocationDto = companyService.getLatLng(companyAddress);

        CompanyResponseDto companyResponseDto = new CompanyResponseDto(companyInfoResponseDto, companyLocationDto);
        return SuccessResponse.ok(companyResponseDto);
    }

    @PostMapping("/registration/{companyId}/{userId}")
    public ResponseEntity<SuccessResponse<?>> joinCompany(@PathVariable("companyId") Long companyId, @PathVariable("userId") Long userId) {
        companyService.joinCompany(companyId, userId);
        return SuccessResponse.ok("");

    }
}
