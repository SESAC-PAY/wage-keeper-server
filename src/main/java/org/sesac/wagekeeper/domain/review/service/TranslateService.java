package org.sesac.wagekeeper.domain.review.service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.sesac.wagekeeper.domain.review.dto.response.ReviewResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TranslateService {
    private final Translate translate;
    public TranslateService(@Value("${google.api.key}") String apiKey) {
        this.translate = TranslateOptions.newBuilder().setApiKey(apiKey).build().getService();
    }

    public List<ReviewResponseDto> translateReviews(List<ReviewResponseDto> reviews, String targetLanguage) {
        return reviews.stream()
                .map(review -> new ReviewResponseDto(
                        review.id(),
                        translateText(review.title(), targetLanguage),
                        translateText(review.content(), targetLanguage),
                        review.averageScore(),
                        review.createdDate()
                ))
                .collect(Collectors.toList());
    }

    private String translateText(String text, String targetLanguage) {
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.targetLanguage(targetLanguage) // targetLanguage는 문자열이어야 함
        );
        return translation.getTranslatedText();
    }


    //중국 (중국어 간체): zh
    //베트남: vi
    //네팔: ne
    //우즈베키스탄: uz
    //캄보디아: km
}
