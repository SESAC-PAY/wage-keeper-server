package org.sesac.wagekeeper.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;
import org.sesac.wagekeeper.domain.review.entity.Review;

import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "user")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    private String comeFrom;

    private Boolean submissionHistory;  //진정서류 접수 이력

    @OneToMany(mappedBy = "user")
    private List<EmploymentInfo> employmentInfos;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

}
