package org.sesac.wagekeeper.domain.user.service;

import lombok.RequiredArgsConstructor;
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
    public String setCountry(Long userId, String countryName) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new RuntimeException("No user id " + userId);

        User currUser = user.get();
        currUser.updateCountry(countryName);
        userRepository.save(currUser);

        return currUser.getComeFrom();
    }

    public User getUserInfo(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new RuntimeException("No user id " + userId);

        return user.get();
    }
}
