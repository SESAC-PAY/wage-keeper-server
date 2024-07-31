package org.sesac.wagekeeper.domain.employmentinfo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sesac.wagekeeper.domain.company.entity.Company;
import org.sesac.wagekeeper.domain.user.entity.User;
import org.sesac.wagekeeper.domain.workinglog.entity.WorkingLog;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "employment_info")
@Entity
public class EmploymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private User user;

    @OneToMany(mappedBy = "employmentInfo")
    private List<WorkingLog> workingLogs;


    private long time;

    private long salary;

}
