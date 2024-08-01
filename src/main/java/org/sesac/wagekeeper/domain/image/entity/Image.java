package org.sesac.wagekeeper.domain.image.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sesac.wagekeeper.domain.user.entity.User;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Table(name = "image")
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
