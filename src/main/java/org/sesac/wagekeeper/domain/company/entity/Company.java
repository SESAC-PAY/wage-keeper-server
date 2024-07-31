package org.sesac.wagekeeper.domain.company.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;
import org.sesac.wagekeeper.domain.review.entity.Review;

import java.util.List;


@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "company")
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "company")
    private List<Review> reviews;

    @OneToMany(mappedBy = "company")
    private List<EmploymentInfo> employmentInfos;

    private String address;

    private String employer;

    @Column(name = "company_name", unique = true)
    private String companyName;

    private String image;

    private boolean reportedIssues; // 임금체불 신고 사업장 여부
    private int reportedCount; // 임금체불 진정서 접수 건수

    public void setReportedIssues(boolean reportedIssues) {
        this.reportedIssues = reportedIssues;
    }

}
