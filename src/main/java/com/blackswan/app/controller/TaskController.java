package com.blackswan.app.controller;

import java.util.List;

import com.blackswan.app.exception.ResourceNotFoundException;
import com.blackswan.app.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blackswan.app.repository.TaskRepository;
import com.blackswan.app.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/api")
@RestController
public class TaskController {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserRepository userRepository;

	// #### Create Task

	@PostMapping("/user/{userId}/task")
	public Task createTask(@PathVariable(value = "userId") Long userId, @RequestBody Task task)
			throws ResourceNotFoundException {
		return userRepository.findById(userId).map(user -> {
			task.setUser(user);
			return taskRepository.save(task);
		}).orElseThrow(() -> new ResourceNotFoundException("user not found"));
	}

	// #### Update Task

	@PutMapping("/user/{userId}/task/{taskId}")
	public Task updateTask(@PathVariable(value = "userId") Long userId, @PathVariable(value = "taskId") Long taskId,
			@RequestBody Task task) throws ResourceNotFoundException {
		if (!userRepository.existsById(userId)) {
			throw new ResourceNotFoundException("userId not found");
		}
		return taskRepository.findById(taskId).map(Task -> {
			return taskRepository.save(task);
		}).orElseThrow(() -> new ResourceNotFoundException("Task id not found"));
	}

	// #### Delete Task

	@DeleteMapping("/user/{userId}/task/{taskId}")
	public ResponseEntity<?> deleteTask(@PathVariable(value = "userId") Long userId,
			@PathVariable(value = "taskId") Long taskId) throws ResourceNotFoundException {
		return taskRepository.findByIdAndUserId(taskId, userId).map(Task -> {
			taskRepository.delete(Task);
			return ResponseEntity.ok().build();
		}).orElseThrow(
				() -> new ResourceNotFoundException("Task not found with id " + taskId + " and userId " + userId));
	}

	// #### Get Task Info

	@GetMapping("/user/{userId}/task/{taskId}")
	public Task getTasksByuserByTask(@PathVariable(value = "taskId") Long taskId,
			@PathVariable(value = "userId") Long userId) {
		return taskRepository.findByIdAndUserId(taskId, userId).get();
	}

	// #### List all tasks for a user

	@GetMapping("/user/{userId}/task")
	public List<Task> getTasksByuser(@PathVariable(value = "userId") Long userId) {
		return taskRepository.findByUserId(userId);
	}

}