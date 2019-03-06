package com.mali.crypfy.auth.core.user.impl;

import com.mali.crypfy.auth.core.user.LoginService;
import com.mali.crypfy.auth.core.user.exceptions.LoginException;
import com.mali.crypfy.auth.persistence.entity.User;
import com.mali.crypfy.auth.persistence.repository.UserRepository;
import com.mali.crypfy.auth.utils.StringUtils;
import com.mali.crypfy.auth.validator.ServiceErrorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    public static final int CODE_ERROR_EMAIL_NOT_NULL = 1000;
    public static final int CODE_ERROR_PASSWORD_NOT_NULL = 1001;
    public static final int CODE_ERROR_INVALID_LOGIN = 1003;
    public static final int CODE_ERROR_GENERIC = 9000;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(String email, String password) throws LoginException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(email == null || email.equals(""))
            errors.add(new ServiceErrorItem("Email não pode ser vazio",CODE_ERROR_EMAIL_NOT_NULL));

        if(password == null || password.equals(""))
            errors.add(new ServiceErrorItem("Senha não pode ser vazio",CODE_ERROR_PASSWORD_NOT_NULL));

        if(!errors.isEmpty())
            throw new LoginException("Login Inválido",errors,400);

        User user = null;
        try {
            user = userRepository.findByEmailAndPassword(email, StringUtils.getMD5(password));
        } catch (Exception e) {
            logger.error("error on find user",e);
            errors.add(new ServiceErrorItem("Ocorreu um erro no servidor",CODE_ERROR_GENERIC));
            throw new LoginException("Ocorreu um erro no servidor",errors,500);
        }

        if(user == null){
            errors.add(new ServiceErrorItem("Login Inválido",CODE_ERROR_INVALID_LOGIN));
            throw new LoginException("Login Inválido",errors,401);
        } else
            return user;
    }
}
