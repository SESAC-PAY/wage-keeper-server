package org.sesac.wagekeeper.domain.workspace.repository;

import org.sesac.wagekeeper.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}
