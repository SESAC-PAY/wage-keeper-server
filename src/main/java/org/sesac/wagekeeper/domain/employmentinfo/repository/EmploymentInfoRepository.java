package org.sesac.wagekeeper.domain.employmentinfo.repository;

import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;
import org.sesac.wagekeeper.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmploymentInfoRepository extends JpaRepository<EmploymentInfo, Long> {
    Optional<EmploymentInfo> findByUser(User user);
}
