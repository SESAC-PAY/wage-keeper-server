package org.sesac.wagekeeper.domain.workspace.entity;

import jakarta.persistence.*;
import lombok.*;
import org.sesac.wagekeeper.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
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
