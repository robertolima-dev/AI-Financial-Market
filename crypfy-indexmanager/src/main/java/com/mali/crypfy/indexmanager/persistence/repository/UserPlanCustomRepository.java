package com.mali.crypfy.indexmanager.persistence.repository;

import com.mali.crypfy.indexmanager.persistence.entity.UserPlan;
import com.mali.crypfy.indexmanager.persistence.enumeration.UserPlanStatus;

import java.util.List;

public interface UserPlanCustomRepository {
    public List<UserPlan> list(String email, UserPlanStatus status);
}
