package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.User;
import java.util.Optional;




public interface UserRepository extends JpaRepository<User, Integer> {

	boolean existsByEmailAndPassword(String email, String password);
	
	@Query("SELECT u.username FROM User u WHERE u.email = :email")
	String findUsernameByEmail(String email);
	
	@Query("SELECT u.role FROM User u WHERE u.email = :email")
	String findRoleByEmail(String email);
	
	
	Optional<User> findByEmail(String email);
	
	default void updatePasswordByEmail(String email, String newPassword) {
        findByEmail(email).ifPresent(user -> {
            // Update the user's password
            user.setPassword(newPassword);
            save(user);
        });
    }

	Optional<User> findByPhone(String phone);
	
	long count();

	
}
