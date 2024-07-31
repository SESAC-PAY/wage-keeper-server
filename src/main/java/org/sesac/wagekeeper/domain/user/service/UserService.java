package org.sesac.wagekeeper.domain.user.service;

import lombok.RequiredArgsConstructor;

import org.sesac.wagekeeper.domain.Util.Util;
import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;
import org.sesac.wagekeeper.domain.employmentinfo.repository.EmploymentInfoRepository;
import org.sesac.wagekeeper.domain.user.dto.MoneyToReceive;
import org.sesac.wagekeeper.domain.user.dto.UserInfo;

import org.sesac.wagekeeper.domain.user.entity.User;
import org.sesac.wagekeeper.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final EmploymentInfoRepository employmentInfoRepository;

    public String setCountry(Long userId, String countryName) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new RuntimeException("No user id " + userId);

        User currUser = user.get();
        currUser.updateCountry(countryName);
        userRepository.save(currUser);

        return currUser.getComeFrom();
    }


    public UserInfo getUserInfo(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new RuntimeException("No user id " + userId);

        User currUser = user.get();

        return UserInfo.builder()
                .id(currUser.getId())
                .name(currUser.getName())
                .comeFrom(currUser.getComeFrom())
                .submissionHistory(currUser.getSubmissionHistory())
                .build();
    }

    public MoneyToReceive getMoneyToReceive(Long userId) {
        Optional<User> User = userRepository.findById(userId);
        if(User.isEmpty()) throw new RuntimeException("No user id " + userId);

        Optional<EmploymentInfo> employmentInfo = employmentInfoRepository.findByUser(User.get());
        if(employmentInfo.isEmpty()) {
            return MoneyToReceive.builder()
                    .userId(userId)
                    .totalMoneyToGet(0)
                    .totalMoneyToDollar(0)
                    .workingTime(0)
                    .build();
        }

        EmploymentInfo currEmploy = employmentInfo.get();

        int totalMoneyToGet = (int)(currEmploy.getTime() * currEmploy.getSalary());

        return MoneyToReceive.builder()
                .userId(userId)
                .workingTime((int)currEmploy.getTime())
                .totalMoneyToGet(totalMoneyToGet)
                .totalMoneyToDollar((int)(totalMoneyToGet * Util.EXCHANGE_RATE))
                .build();
    }
}
