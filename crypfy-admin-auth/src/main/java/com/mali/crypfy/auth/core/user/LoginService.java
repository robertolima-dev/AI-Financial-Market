package com.mali.crypfy.auth.core.user;

import com.mali.crypfy.auth.core.user.exceptions.LoginException;
import com.mali.crypfy.auth.persistence.entity.User;

public interface LoginService {
    public User login(String email, String password) throws LoginException;
}
