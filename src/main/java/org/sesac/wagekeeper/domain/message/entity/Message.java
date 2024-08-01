package org.sesac.wagekeeper.domain.message.entity;

import jakarta.persistence.*;
import lombok.*;
import org.sesac.wagekeeper.domain.workspace.entity.Workspace;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    private String role;

    @JoinColumn(name="workspace_id", nullable = false)
    @ManyToOne
    private Workspace workspace;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}