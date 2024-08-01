package org.sesac.wagekeeper.domain.message.repository;

import org.sesac.wagekeeper.domain.message.entity.Message;
import org.sesac.wagekeeper.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByWorkspaceOrderByCreatedAtDesc(Workspace workspace);
}
