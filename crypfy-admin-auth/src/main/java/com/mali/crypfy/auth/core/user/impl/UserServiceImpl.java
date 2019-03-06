package com.mali.crypfy.auth.core.user.impl;

import com.mali.crypfy.auth.core.user.UserService;
import com.mali.crypfy.auth.core.user.exceptions.UserException;
import com.mali.crypfy.auth.persistence.entity.User;
import com.mali.crypfy.auth.persistence.repository.UserRepository;
import com.mali.crypfy.auth.utils.StringUtils;
import com.mali.crypfy.auth.validator.ServiceErrorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    //email errors code
    public static final int CODE_ERROR_EMAIL_NOT_NULL = 1000;
    public static final int CODE_ERROR_EMAIL_INVALID = 1001;
    public static final int CODE_ERROR_EMAIL_IN_USE = 1002;
    public static final int CODE_ERROR_EMAIL_MIN_LENGTH = 1003;
    public static final int CODE_ERROR_EMAIL_NOT_FOUND = 1004;

    //name errors code
    public static final int CODE_ERROR_NAME_NOT_NULL = 2000;
    public static final int CODE_ERROR_NAME_MIN_LENGTH = 2001;

    //password errors code
    public static final int CODE_ERROR_PASSWORD_NOT_NULL = 3000;
    public static final int CODE_ERROR_CONFIRM_PASSWORD_NOT_NULL = 3001;
    public static final int CODE_ERROR_PASSWORD_DIVERGENCE = 3002;
    public static final int CODE_ERROR_PASSWORD_MIN_LENGTH = 3003;

    public static final int CODE_ERROR_GENERIC = 9000;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public User add(User user) throws UserException {

        List<ServiceErrorItem> errors = validateAddUser(user);

        if (!errors.isEmpty())
            throw new UserException("Não foi possível cadastrar o usuário", errors, 400);

        user.setCreated(new Date());
        user.setPassword(StringUtils.getMD5(user.getPassword()));

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("error on signup", e);

            errors.clear();
            errors.add(new ServiceErrorItem("ocorreu um erro no servidor", CODE_ERROR_GENERIC));

            throw new UserException("Ocorreu um erro no servidor", errors, 500);
        }
    }

    private List<ServiceErrorItem> validateAddUser(User user) {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        //email validation
        if (user.getEmail() == null || user.getEmail().equals(""))
            errors.add(new ServiceErrorItem("Email é obrigatório", CODE_ERROR_EMAIL_NOT_NULL));
        if (user.getEmail() != null && !user.getEmail().equals("") && !Pattern.compile(StringUtils.PATTERN_EMAIL).matcher(user.getEmail()).matches())
            errors.add(new ServiceErrorItem("Email inválido", CODE_ERROR_EMAIL_INVALID));
        if (user.getEmail() != null && user.getEmail().length() > 100)
            errors.add(new ServiceErrorItem("Email não pode ser maior que 100 caracteres", CODE_ERROR_EMAIL_MIN_LENGTH));
        if (userRepository.findOne(user.getEmail()) != null)
            errors.add(new ServiceErrorItem("Email já cadastrado", CODE_ERROR_EMAIL_IN_USE));

        //name validation
        if (user.getName() == null || user.getName().equals(""))
            errors.add(new ServiceErrorItem("Nome é obrigatório", CODE_ERROR_NAME_NOT_NULL));
        if (user.getName() != null && !user.getName().equals("") && user.getName().length() > 200)
            errors.add(new ServiceErrorItem("Nome não pode ser maior que 200 caracteres", CODE_ERROR_NAME_MIN_LENGTH));

        //password validation
        if (user.getPassword() == null || user.getPassword().equals(""))
            errors.add(new ServiceErrorItem("Senha é obrigatório", CODE_ERROR_PASSWORD_NOT_NULL));
        if (user.getConfirmPassword() == null || user.getConfirmPassword().equals(""))
            errors.add(new ServiceErrorItem("Confirmação de senha é obrigatório", CODE_ERROR_CONFIRM_PASSWORD_NOT_NULL));
        if (user.getPassword() != null && user.getConfirmPassword() != null & !user.getPassword().equals(user.getConfirmPassword()))
            errors.add(new ServiceErrorItem("Divergência na confirmação de senha", CODE_ERROR_PASSWORD_DIVERGENCE));
        if (user.getPassword() != null && !user.getPassword().equals("") && user.getPassword().length() < 8)
            errors.add(new ServiceErrorItem("Senha deve ter no mínimo 8 caracteres", CODE_ERROR_PASSWORD_MIN_LENGTH));

        return errors;
    }


}
