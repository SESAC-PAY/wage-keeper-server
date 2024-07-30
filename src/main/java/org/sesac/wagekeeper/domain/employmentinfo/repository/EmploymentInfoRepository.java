package org.sesac.wagekeeper.domain.employmentinfo.repository;

import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentInfoRepository extends JpaRepository<EmploymentInfo, Long> {
}
