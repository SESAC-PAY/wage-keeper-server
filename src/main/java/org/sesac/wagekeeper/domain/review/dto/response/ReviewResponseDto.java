package org.sesac.wagekeeper.domain.review.dto.response;

import org.sesac.wagekeeper.domain.review.entity.Review;

import java.time.LocalDate;
import java.util.List;

public record ReviewResponseDto(
        Long id,
        String title,
        String content,
        Double averageScore,
        LocalDate createdDate

) {
    public static List<ReviewResponseDto> of(List<Review> reviews) {
        return reviews.stream()
                .map(review -> new ReviewResponseDto(
                        review.getId(),
                        review.getTitle(),
                        review.getContent(),
                        review.getAverageScore(),
                        review.getCreatedDate().toLocalDate()
                ))
                .toList();
    }
}
