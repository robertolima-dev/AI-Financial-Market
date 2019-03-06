package com.mali.crypfy.auth.persistence.repository;

import com.mali.crypfy.auth.persistence.entity.User;
import com.mali.crypfy.auth.persistence.enumeration.DocumentVerificationStatus;
import com.mali.crypfy.auth.persistence.enumeration.IdentityVerificationStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository extends CrudRepository<User,String>,CustomUserRepository {
    public User findByEmailConfirmationToken(String emailConfirmationToken);
    public User findByEmail(String email);
    public User findByEmailAndPassword(String email,String password);
}
