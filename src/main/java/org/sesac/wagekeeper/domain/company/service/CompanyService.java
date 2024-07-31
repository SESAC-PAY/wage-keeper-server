package org.sesac.wagekeeper.domain.company.service;

import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.domain.Util.Util;
import org.sesac.wagekeeper.domain.company.entity.Company;
import org.sesac.wagekeeper.domain.company.repository.CompanyRepository;
import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;
import org.sesac.wagekeeper.domain.employmentinfo.repository.EmploymentInfoRepository;
import org.sesac.wagekeeper.domain.user.entity.User;
import org.sesac.wagekeeper.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CompanyService {
    private final EmploymentInfoRepository employmentInfoRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public void joinCompany(Long companyId, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Company> company = companyRepository.findById(companyId);

        if(user.isEmpty() || company.isEmpty()) throw new RuntimeException("No User " + userId + " or Company " + companyId);

        employmentInfoRepository.save(
                EmploymentInfo.builder()
                        .user(user.get())
                        .company(company.get())
                        .salary(Util.WAGE_PER_HOUR)
                        .time(0L)
                        .build()
        );
    }
}
