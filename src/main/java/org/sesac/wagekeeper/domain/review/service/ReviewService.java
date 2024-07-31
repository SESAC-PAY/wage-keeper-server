package org.sesac.wagekeeper.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sesac.wagekeeper.domain.company.entity.Company;
import org.sesac.wagekeeper.domain.company.repository.CompanyRepository;
import org.sesac.wagekeeper.domain.review.dto.request.ReviewRequestDto;
import org.sesac.wagekeeper.domain.review.dto.response.ReviewResponseDto;
import org.sesac.wagekeeper.domain.review.dto.response.ReviewScoreResponseDto;
import org.sesac.wagekeeper.domain.review.dto.response.ReportIssueResponseDto;
import org.sesac.wagekeeper.domain.review.entity.Review;
import org.sesac.wagekeeper.domain.review.repository.ReviewRepository;
import org.sesac.wagekeeper.domain.user.entity.User;
import org.sesac.wagekeeper.domain.user.repository.UserRepository;
import org.sesac.wagekeeper.global.error.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.sesac.wagekeeper.global.error.ErrorCode.COMPANY_NOT_FOUND;
import static org.sesac.wagekeeper.global.error.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final TranslateService translateService;


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

    private static final String MOEL_BASE_URL = "https://www.moel.go.kr/info/defaulter/defaulterList.do";

    // 임금 체불 사업장명 검색
    public ReportIssueResponseDto getReportIssue(Long companyId) throws IOException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(COMPANY_NOT_FOUND));

        if (company.isReportedIssues()) {
            return ReportIssueResponseDto.builder()
                    .reportIssue(true)
                    .reportedCount(company.getReportedCount())
                    .build();
        }

        Connection.Response response = Jsoup.connect(MOEL_BASE_URL)
                .data("pageIndex", "1")
                .data("searchOrder", "1")
                .data("searchYear", "")
                .data("pageUnit", "10")
                .data("searchField", "4")
                .data("searchText", company.getCompanyName())
                .data("searchRegion", "")
                .data("searchIndustry", "")
                .method(Connection.Method.POST)
                .execute();

        Document document = response.parse();
        Elements rows = document.select(".arr_list tbody tr");

        for (Element row : rows) {
            Elements columns = row.select("td");

            if (columns.size() >= 6) { //임금 체불 사업장 검색 결과가 존재한다면
                String personName = columns.get(0).text();
                String companyName = columns.get(2).text();

                if (companyName.equals(company.getCompanyName()) && personName.equals(company.getEmployer())) {
                    company.setReportedIssues(true);
                    company.setReportedCount(ThreadLocalRandom.current().nextInt(1, 4));
                    companyRepository.save(company);
                    return ReportIssueResponseDto.builder()
                            .reportIssue(true)
                            .reportedCount(company.getReportedCount())
                            .build();
                }
            }
        }
        return ReportIssueResponseDto.builder()
                .reportIssue(false)
                .reportedCount(0)
                .build();
    }

    //리뷰 리스트 조회

    public List<ReviewResponseDto> getReview(Long companyId, String targetLanguage) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(COMPANY_NOT_FOUND));

        List<Review> reviews = reviewRepository.findByCompany(company);
        List<ReviewResponseDto> reviewResponse = ReviewResponseDto.of(reviews);
        List<ReviewResponseDto> translatedReviewResponse = translateService.translateReviews(reviewResponse, targetLanguage);

        return translatedReviewResponse;
    }

    //리뷰 세부 평점 조회
    public ReviewScoreResponseDto getReviewScore(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(COMPANY_NOT_FOUND));

        List<Review> reviews = reviewRepository.findByCompany(company);

        Double totalAvgScore = calculateAverage(reviews, "averageScore");

        Double totalAvgProperPaymentScore = calculateAverage(reviews, "properPaymentScore");
        Double totalAvgEnvironmentScore = calculateAverage(reviews, "environmentScore");
        Double totalAvgDomitoryScore = calculateAverage(reviews, "domitoryScore");
        Double totalAvgFullfillContractScore = calculateAverage(reviews, "fullfillContractScore");

        return ReviewScoreResponseDto.builder()
                .totalAvgScore(totalAvgScore)
                .totalAvgProperPaymentScore(totalAvgProperPaymentScore)
                .totalAvgEnvironmentScore(totalAvgEnvironmentScore)
                .totalAvgDomitoryScore(totalAvgDomitoryScore)
                .totalAvgFullfillContractScore(totalAvgFullfillContractScore)
                .build();
    }


    //평균 계산
    private Double calculateAverage(List<Review> reviews, String scoreType) {
        if (reviews == null || reviews.isEmpty()) {
            return null;
        }

        double average = reviews.stream()
                .mapToDouble(review -> {
                    switch (scoreType) {
                        case "properPaymentScore":
                            return review.getProperPaymentScore();
                        case "environmentScore":
                            return review.getEnvironmentScore();
                        case "domitoryScore":
                            return review.getDomitoryScore();
                        case "fullfillContractScore":
                            return review.getFullfillContractScore();
                        case "averageScore":
                            return review.getAverageScore();
                        default:
                            throw new IllegalArgumentException("scoreType Error: " + scoreType);
                    }
                })
                .average()
                .orElse(Double.NaN);

        return Double.valueOf(String.format("%.1f", average));
    }
}



