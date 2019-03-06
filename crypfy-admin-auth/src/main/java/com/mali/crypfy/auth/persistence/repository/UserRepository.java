package com.mali.crypfy.auth.persistence.repository;

import com.mali.crypfy.auth.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,String> {
    public User findByEmailAndPassword(String email,String password);
}
