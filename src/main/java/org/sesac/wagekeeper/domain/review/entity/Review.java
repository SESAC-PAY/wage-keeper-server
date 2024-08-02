package org.sesac.wagekeeper.domain.review.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sesac.wagekeeper.domain.company.entity.Company;
import org.sesac.wagekeeper.domain.user.entity.User;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "review")
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "writer_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    private String title;

    private String content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    private Long properPaymentScore;
    private Long environmentScore;
    private Long domitoryScore;
    private Long fullfillContractScore;
    private Double averageScore;
}
