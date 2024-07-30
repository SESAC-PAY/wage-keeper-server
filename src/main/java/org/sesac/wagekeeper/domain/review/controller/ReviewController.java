package org.sesac.wagekeeper.domain.review.controller;


import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.domain.review.dto.request.ReviewRequestDto;
import org.sesac.wagekeeper.domain.review.service.ReviewService;
import org.sesac.wagekeeper.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/review")
@RestController
public class ReviewController {

    private final ReviewService reviewService;
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> createReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        reviewService.createReview(reviewRequestDto);
        return SuccessResponse.ok("리뷰가 등록되었습니다.");
    }

}
