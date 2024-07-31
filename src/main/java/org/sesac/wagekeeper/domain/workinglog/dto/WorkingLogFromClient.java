package org.sesac.wagekeeper.domain.workinglog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WorkingLogFromClient {
    private Long userId;
    private String location;
}
