package org.sesac.wagekeeper.domain.review.controller;


import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.domain.company.dto.response.CompanyInfoResponseDto;
import org.sesac.wagekeeper.domain.company.service.CompanyService;
import org.sesac.wagekeeper.domain.review.dto.request.ReviewRequestDto;
import org.sesac.wagekeeper.domain.review.dto.response.ReviewResponseDto;
import org.sesac.wagekeeper.domain.review.dto.response.ReviewScoreResponseDto;
import org.sesac.wagekeeper.domain.review.dto.response.TotalCompanyInfoResponseDto;
import org.sesac.wagekeeper.domain.review.dto.response.ReportIssueResponseDto;
import org.sesac.wagekeeper.domain.review.service.ReviewService;
import org.sesac.wagekeeper.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/review")
@RestController
public class ReviewController {


    private final ReviewService reviewService;
    private final CompanyService companyService;
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        reviewService.createReview(reviewRequestDto);
        return SuccessResponse.ok("리뷰가 등록되었습니다.");
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<SuccessResponse<?>> getReview(@PathVariable Long companyId, @RequestParam(defaultValue = "ko") String language) throws IOException {
        String companyName = companyService.getCompanyName(companyId);
        CompanyInfoResponseDto companyInfoResponseDto = companyService.getCompanyInfo(companyName);

        ReportIssueResponseDto reportIssueResponseDto = reviewService.getReportIssue(companyId);

        ReviewScoreResponseDto reviewScoreResponseDto = reviewService.getReviewScore(companyId);
        List<ReviewResponseDto> reviewResponseDto = reviewService.getReview(companyId, language);

        TotalCompanyInfoResponseDto totalCompanyInfoResponseDto = new TotalCompanyInfoResponseDto(companyInfoResponseDto, reportIssueResponseDto, reviewScoreResponseDto, reviewResponseDto);
        return SuccessResponse.ok(totalCompanyInfoResponseDto);

    }

}
