package com.blackswan.app.repository;

import java.util.List;
import java.util.Optional;

import com.blackswan.app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByUserId(Long userId);

	Optional<Task> findByIdAndUserId(Long id, Long UserId);
}
