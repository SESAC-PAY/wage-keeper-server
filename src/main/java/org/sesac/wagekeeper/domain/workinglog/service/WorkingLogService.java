package org.sesac.wagekeeper.domain.workinglog.service;

import lombok.RequiredArgsConstructor;
import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;
import org.sesac.wagekeeper.domain.employmentinfo.repository.EmploymentInfoRepository;
import org.sesac.wagekeeper.domain.user.entity.User;
import org.sesac.wagekeeper.domain.user.repository.UserRepository;
import org.sesac.wagekeeper.domain.workinglog.dto.WorkingLogFromClient;
import org.sesac.wagekeeper.domain.workinglog.entity.WorkingLog;
import org.sesac.wagekeeper.domain.workinglog.repository.WorkingLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class WorkingLogService {
    private final EmploymentInfoRepository employmentInfoRepository;
    private final UserRepository userRepository;
    private final WorkingLogRepository workingLogRepository;

    public void makeLog(WorkingLogFromClient workingLogFromClient) {
        Long currUserId = workingLogFromClient.getUserId();
        Optional<User> user = userRepository.findById(currUserId);
        if(user.isEmpty()) throw new RuntimeException("No User Id " + currUserId);

        Optional<EmploymentInfo> safeemploymentInfo = employmentInfoRepository.findByUser(user.get());
        if(safeemploymentInfo.isEmpty()) throw new RuntimeException("There is no record of joining the company.");

        EmploymentInfo employmentInfo = safeemploymentInfo.get();

        boolean isInWorking = compWithCompanyLocation(employmentInfo, workingLogFromClient.getLocation());
        if(isInWorking) employmentInfo.increaseWorkingTime();

        employmentInfoRepository.save(employmentInfo);

        workingLogRepository.save(
                WorkingLog.builder()
                        .timestamp(LocalDateTime.now())
                        .location(workingLogFromClient.getLocation())
                        .employmentInfo(employmentInfo)
                        .isWorkspace(isInWorking)
                        .build()
        );
    }

    private boolean compWithCompanyLocation(EmploymentInfo employmentInfo, String userLocation) {
        return userLocation.equals(employmentInfo.getCompany().getAddress());
    }
}
