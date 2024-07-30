package org.sesac.wagekeeper.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.domain.company.entity.Company;
import org.sesac.wagekeeper.domain.company.repository.CompanyRepository;
import org.sesac.wagekeeper.domain.review.dto.request.ReviewRequestDto;
import org.sesac.wagekeeper.domain.review.entity.Review;
import org.sesac.wagekeeper.domain.review.repository.ReviewRepository;
import org.sesac.wagekeeper.domain.user.entity.User;
import org.sesac.wagekeeper.domain.user.repository.UserRepository;
import org.sesac.wagekeeper.global.error.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.sesac.wagekeeper.global.error.ErrorCode.COMPANY_NOT_FOUND;
import static org.sesac.wagekeeper.global.error.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public void createReview(ReviewRequestDto reviewRequestDto) {

        User user = userRepository.findById(reviewRequestDto.userId())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        Company company = companyRepository.findById(reviewRequestDto.companyId())
                .orElseThrow(() -> new EntityNotFoundException(COMPANY_NOT_FOUND));

        Review review = Review.builder()
                .user(user)
                .company(company)
                .title(reviewRequestDto.title())
                .content(reviewRequestDto.content())
                .properPaymentScore(reviewRequestDto.properPaymentScore())
                .environmentScore(reviewRequestDto.environmentScore())
                .domitoryScore(reviewRequestDto.domitoryScore())
                .fullfillContractScore(reviewRequestDto.fullfillContractScore())
                .averageScore((double) (reviewRequestDto.properPaymentScore() + reviewRequestDto.environmentScore() + reviewRequestDto.domitoryScore() + reviewRequestDto.fullfillContractScore()) / 4)
                .createdDate(LocalDateTime.now())
                .build();

            reviewRepository.save(review);
    }

}


