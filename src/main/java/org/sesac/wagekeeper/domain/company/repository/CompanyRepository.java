package org.sesac.wagekeeper.domain.company.repository;

import org.sesac.wagekeeper.domain.company.entity.Company;
import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
