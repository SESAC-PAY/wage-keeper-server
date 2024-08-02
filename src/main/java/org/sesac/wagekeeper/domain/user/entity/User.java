package org.sesac.wagekeeper.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;
import org.sesac.wagekeeper.domain.image.entity.Image;
import org.sesac.wagekeeper.domain.review.entity.Review;

import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "user")
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String comeFrom;

    private Boolean submissionHistory;


    @OneToMany(mappedBy = "user")
    private List<EmploymentInfo> employmentInfos;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    public void updateCountry(String country) {
        this.comeFrom = country;
    }
}
