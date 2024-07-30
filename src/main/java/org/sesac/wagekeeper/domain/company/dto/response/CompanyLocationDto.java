package org.sesac.wagekeeper.domain.company.dto.response;

public record CompanyLocationDto(
        Double latitude, //위도
        Double longitude //경도
) {
}
