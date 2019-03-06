package com.mali.crypfy.auth.core.user;

import com.mali.crypfy.auth.core.user.exceptions.RedefinePasswordInvalidTokenException;
import com.mali.crypfy.auth.core.user.exceptions.RedefinePasswordRequestException;
import com.mali.crypfy.auth.core.user.exceptions.UserException;
import com.mali.crypfy.auth.persistence.entity.RedefinePasswordRequest;
import com.mali.crypfy.auth.persistence.entity.User;

import java.math.BigDecimal;

public interface UserService {
    public User add(User user) throws UserException;
}
