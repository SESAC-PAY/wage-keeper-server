package org.sesac.wagekeeper.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.WageKeeperApplication;
import org.sesac.wagekeeper.domain.Util.Util;
import org.sesac.wagekeeper.domain.user.dto.MoneyToReceive;
import org.sesac.wagekeeper.domain.user.entity.User;
import org.sesac.wagekeeper.domain.user.service.UserService;
import org.sesac.wagekeeper.global.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;
    @PatchMapping("/country/{userId}/{countryName}")
    public ResponseEntity<SuccessResponse<?>> setCountry(@PathVariable("userId") Long userId, @PathVariable("countryName") String countryName) {
        String comeFrom = userService.setCountry(userId, countryName);
        return SuccessResponse.ok(comeFrom);
    }

    @GetMapping("/info/basic/{userId}")
    public ResponseEntity<SuccessResponse<?>> getUserInfo(@PathVariable("userId") Long userId) {
        User user = userService.getUserInfo(userId);
        return SuccessResponse.ok(user);
    }

    @GetMapping("/info/salary/{userId}")
    public ResponseEntity<SuccessResponse<?>> getMoneyToReceive(@PathVariable("userId") Long userId) {
        int workingTime = 200;
        return SuccessResponse.ok(
                MoneyToReceive.builder()
                        .userId(userId)
                        .workingTime(workingTime)
                        .salary(workingTime*Util.WAGE_PER_HOUR)
                        .salaryToDollar((int)(workingTime*Util.WAGE_PER_HOUR* Util.EXCHANGE_RATE))
                        .build()

        );
    }
}
