package org.sesac.wagekeeper.domain.company.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;
import org.sesac.wagekeeper.domain.review.entity.Review;

import java.util.List;

@Getter
@NoArgsConstructor
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


}
