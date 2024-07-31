package org.sesac.wagekeeper.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MoneyToReceive {
    long userId;
    int workingTime;
    int totalMoneyToGet;
    int totalMoneyToDollar;

}
