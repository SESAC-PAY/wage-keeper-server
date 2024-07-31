package org.sesac.wagekeeper.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserAllInfo {
    UserInfo userInfo;
    MoneyToReceive moneyToReceive;
}
