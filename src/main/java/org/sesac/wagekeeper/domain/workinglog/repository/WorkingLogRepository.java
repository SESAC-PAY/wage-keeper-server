package org.sesac.wagekeeper.domain.workinglog.repository;

import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;
import org.sesac.wagekeeper.domain.workinglog.entity.WorkingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingLogRepository extends JpaRepository<WorkingLog, Long> {
}
