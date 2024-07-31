package org.sesac.wagekeeper.domain.review.dto.response;

import lombok.Builder;

@Builder
public record ReportIssueResponseDto(
        boolean reportIssue,
        int reportedCount
) {
}
