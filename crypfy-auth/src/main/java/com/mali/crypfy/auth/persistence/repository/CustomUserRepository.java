package com.mali.crypfy.auth.persistence.repository;

import com.mali.crypfy.auth.persistence.entity.User;
import com.mali.crypfy.auth.persistence.enumeration.DocumentVerificationStatus;
import com.mali.crypfy.auth.persistence.enumeration.IdentityVerificationStatus;
import java.util.List;


public interface CustomUserRepository {
    public List<User> list(DocumentVerificationStatus documentStatus, IdentityVerificationStatus identityStatus, String email);
}
