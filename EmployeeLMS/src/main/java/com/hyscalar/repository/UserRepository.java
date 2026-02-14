package com.hyscalar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyscalar.entity.User;

public interface UserRepository extends JpaRepository<User, Long>
{
	Optional<User> findByEmailIgnoreCase(String email);


}
