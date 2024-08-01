package org.sesac.wagekeeper.domain.image.repository;

import org.sesac.wagekeeper.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
