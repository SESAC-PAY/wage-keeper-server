package org.sesac.wagekeeper.domain.workspace.service;

import lombok.AllArgsConstructor;
import org.sesac.wagekeeper.domain.user.entity.User;
import org.sesac.wagekeeper.domain.user.repository.UserRepository;
import org.sesac.wagekeeper.domain.workspace.entity.Workspace;
import org.sesac.wagekeeper.domain.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    public Long generateWorkspace(Long userId) {
        Optional<User> safeUser = userRepository.findById(userId);
        if(safeUser.isEmpty()) throw new RuntimeException("No User id " + userId);

        Workspace workspace = workspaceRepository.save(
                Workspace.builder()
                        .user(safeUser.get())
                        .createdAt(LocalDateTime.now())
                        .editedAt(LocalDateTime.now())
                        .workspaceName("workspace")
                        .build()
        );

        return workspace.getId();
    }
}
