package com.mali.crypfy.auth.persistence.repository;

import com.mali.crypfy.auth.persistence.entity.RedefinePasswordRequest;
import com.mali.crypfy.auth.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedefinePasswordRequestRepository extends CrudRepository<RedefinePasswordRequest,Integer> {
    public List<RedefinePasswordRequest> findByEmailOrderByCreatedDesc(String email);
}
