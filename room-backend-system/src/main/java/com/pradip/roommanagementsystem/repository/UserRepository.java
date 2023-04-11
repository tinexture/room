package com.pradip.roommanagementsystem.repository;

import com.pradip.roommanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);
	<T> List<T> findAllBy(Class<T> projectionType);
	<T> Optional<T> findById(Long id, Class<T> type);
}
