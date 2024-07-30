package org.sesac.wagekeeper.domain.workinglog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sesac.wagekeeper.domain.employmentinfo.entity.EmploymentInfo;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Table(name = "working_log")
@Entity
public class WorkingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employment_id")
    private EmploymentInfo employmentInfo;

    private String location;

    private Boolean isWorkspace;

    private LocalDateTime timestamp;


}
