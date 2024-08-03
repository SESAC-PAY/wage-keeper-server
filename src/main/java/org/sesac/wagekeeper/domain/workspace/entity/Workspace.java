package org.sesac.wagekeeper.domain.workspace.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.sesac.wagekeeper.domain.image.entity.Image;
import org.sesac.wagekeeper.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Workspace {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="workspace_id", nullable = false)
    private Long id;

    @Column(name="workspace_name", nullable = false)
    private String workspaceName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Setter
    @Column(nullable = false)
    private LocalDateTime editedAt;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

}
