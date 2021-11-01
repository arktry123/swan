package com.blackswan.app.repository;

import com.blackswan.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Iterable<User> findByUserNameContaining(String username);
}
