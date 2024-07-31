package org.sesac.wagekeeper.domain.user.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserInfo {
    private Long id;

    private String name;

    private String comeFrom;

    private Boolean submissionHistory;  //진정서류 접수 이력
}
