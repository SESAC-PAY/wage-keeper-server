package org.sesac.wagekeeper.domain.document.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sesac.wagekeeper.domain.user.entity.User;

@Getter
@NoArgsConstructor
@Table(name = "document")
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    String html;

    @OneToOne
    private User user;

}
