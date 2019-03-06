package com.mali.crypfy.auth.core.user.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mali.crypfy.auth.core.email.MailSender;
import com.mali.crypfy.auth.core.email.exceptions.EmailException;
import com.mali.crypfy.auth.core.template.HtmlTemplateBuilder;
import com.mali.crypfy.auth.core.template.exception.HtmlTemplateBuilderException;
import com.mali.crypfy.auth.core.user.UserService;
import com.mali.crypfy.auth.core.user.exceptions.RedefinePasswordInvalidTokenException;
import com.mali.crypfy.auth.core.user.exceptions.RedefinePasswordRequestException;
import com.mali.crypfy.auth.core.user.exceptions.UserException;
import com.mali.crypfy.auth.integrations.slack.SlackService;
import com.mali.crypfy.auth.integrations.slack.exception.SlackException;
import com.mali.crypfy.auth.persistence.entity.RedefinePasswordRequest;
import com.mali.crypfy.auth.persistence.entity.User;
import com.mali.crypfy.auth.persistence.enumeration.DocumentType;
import com.mali.crypfy.auth.persistence.enumeration.DocumentVerificationStatus;
import com.mali.crypfy.auth.persistence.enumeration.IdentityVerificationStatus;
import com.mali.crypfy.auth.persistence.repository.RedefinePasswordRequestRepository;
import com.mali.crypfy.auth.persistence.repository.UserRepository;
import com.mali.crypfy.auth.utils.DateUtils;
import com.mali.crypfy.auth.utils.StringUtils;
import com.mali.crypfy.auth.validator.ServiceErrorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    //email errors code
    public static final int CODE_ERROR_EMAIL_NOT_NULL = 1000;
    public static final int CODE_ERROR_EMAIL_INVALID = 1001;
    public static final int CODE_ERROR_EMAIL_IN_USE= 1002;
    public static final int CODE_ERROR_EMAIL_MIN_LENGTH = 1003;
    public static final int CODE_ERROR_EMAIL_NOT_FOUND = 1004;
    public static final int CODE_ERROR_EMAIL_NOT_VERIFIED = 1005;

    //name errors code
    public static final int CODE_ERROR_NAME_NOT_NULL = 2000;
    public static final int CODE_ERROR_NAME_MIN_LENGTH = 2001;

    //password errors code
    public static final int CODE_ERROR_PASSWORD_NOT_NULL = 3000;
    public static final int CODE_ERROR_CONFIRM_PASSWORD_NOT_NULL = 3001;
    public static final int CODE_ERROR_PASSWORD_DIVERGENCE = 3002;
    public static final int CODE_ERROR_PASSWORD_MIN_LENGTH = 3003;
    public static final int CODE_ERROR_CURRENT_PASSWORD_INVALID = 3004;
    public static final int CODE_ERROR_NEW_PASSWORD_INVALID = 3005;
    public static final int CODE_ERROR_CURRENT_PASSWORD_NOT_NULL = 3006;

    //document errors code
    public static final int CODE_ERROR_DOCUMENT_TYPE_NOT_NULL = 4000;
    public static final int CODE_ERROR_DOCUMENT_NUMBER_NOT_NULL = 4001;
    public static final int CODE_ERROR_DOCUMENT_NUMBER_INVALID = 4002;

    public static final int CODE_ERROR_USER_NOT_FOUND = 5000;
    public static final int CODE_ERROR_PHONE_INVALID = 5001;
    public static final int CODE_ERROR_REQUEST_NEW_PASSOWORD_ABUSE = 6000;
    public static final int CODE_ERROR_REQUEST_NEW_PASSWORD_TOKEN_INVALID = 7000;
    public static final int CODE_ERROR_BALANCE_INVALID = 8000;
    public static final int CODE_ERROR_BALANCE_NOT_ENOUGH = 8001;

    public static final int CODE_ERROR_GENERIC = 9000;

    private static final String REDEFINE_PASSWORD_SECRET_KEY_TOKEN = "ulissesrobertoviniciusfelipe";

    private static final String AVATAR_DEFAULT = "https://s3-sa-east-1.amazonaws.com/static.crypfy/img/avatar.svg";

    @Value("${spring.crypfy.services.email-confirmation-resource}")
    private String emailConfirmationResource;
    @Value("${spring.crypfy.queue.topics.auth-updated-brl-balance}")

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedefinePasswordRequestRepository redefinePasswordRequestRepository;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private HtmlTemplateBuilder htmlTemplateBuilder;
    @Autowired
    private SlackService slackService;
    @Autowired
    private KafkaTemplate<String,Map<String,Object>> kafkaTemplate;

    @Override
    @Transactional
    public User add(User user) throws UserException {

        List<ServiceErrorItem> errors = validateAddUser(user);

        if(!errors.isEmpty())
            throw new UserException("Não foi possível cadastrar o usuário",errors,400);

        user.setCreated(new Date());
        user.setEmailConfirmationToken(generateEmailConfirmationToken(user.getEmail()));
        user.setPassword(StringUtils.getMD5(user.getPassword()));
        user.setAvailableBalanceBrl(BigDecimal.ZERO);
        user.setIdentityVerificationStatus(IdentityVerificationStatus.UNVERIFIED);
        user.setDocumentVerificationStatus(DocumentVerificationStatus.UNVERIFIED);
        user.setAvatar(AVATAR_DEFAULT);

        sendEmailConfirmation(user);

        try {
            slackService.sendMessage("Novo usuário cadastrado! Nome *"+user.getName()+" Email "+ user.getEmail());
        } catch (SlackException e) {
            logger.error("error on send slack message");
        }

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("error on signup",e);

            errors.clear();
            errors.add(new ServiceErrorItem("ocorreu um erro no servidor",CODE_ERROR_GENERIC));

            throw new UserException("Ocorreu um erro no servidor",errors,500);
        }
    }

    private void sendEmailConfirmation(User user) throws UserException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        try {
            Map<String,String> templateParams = new HashMap<String,String>();
            templateParams.put("user",user.getName());
            templateParams.put("link",emailConfirmationResource + user.getEmailConfirmationToken());
            String bodyHtml = htmlTemplateBuilder.buildTemplate("email_confirmation.ftl",templateParams);

            mailSender.sendSimpleMailHtml(user.getEmail(),"no-reply@crypfy.com","Confirmação da conta Crypfy","Crypfy Plataforrma",bodyHtml);
        } catch (EmailException e) {
            logger.error("signup error on send email confirmation",e);

            errors.clear();
            errors.add(new ServiceErrorItem("ocorreu um erro no servidor",CODE_ERROR_GENERIC));

            throw new UserException("Ocorreu um erro no servidor",errors,500);
        } catch (HtmlTemplateBuilderException e) {
            logger.error("signup error on build email template confirmation",e);

            errors.clear();
            errors.add(new ServiceErrorItem("ocorreu um erro no servidor",CODE_ERROR_GENERIC));

            throw new UserException("Ocorreu um erro no servidor",errors,500);
        }
    }

    @Override
    public User updateProfile(User user) throws UserException {
        User foundUser = userRepository.findByEmail(user.getEmail());

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(foundUser == null)
            throw new UserException("Usuário não encontrado",null,500);

        if(user.getName() == null || user.getName().equals(""))
            errors.add(new ServiceErrorItem("Nome é obrigatório",CODE_ERROR_NAME_NOT_NULL));
        if(user.getName() != null && !user.getName().equals("") && user.getName().length() > 200)
            errors.add(new ServiceErrorItem("Nome não pode ser maior que 200 caracteres",CODE_ERROR_NAME_MIN_LENGTH));
        if((foundUser.getIdentityVerificationStatus() == IdentityVerificationStatus.UNVERIFIED || foundUser.getIdentityVerificationStatus() == IdentityVerificationStatus.DENIED) && (user.getDocumentNumber() == null || user.getDocumentNumber().equals("")))
            errors.add(new ServiceErrorItem("Número do Documento é obrigatório",CODE_ERROR_DOCUMENT_NUMBER_NOT_NULL));
        if((user.getDocumentType() == DocumentType.CPF) && (user.getDocumentNumber() != null && !user.getDocumentNumber().equals("")) && !user.getDocumentNumber().matches(StringUtils.PATTERN_CPF))
            errors.add(new ServiceErrorItem("Número do Documento inválido",CODE_ERROR_DOCUMENT_NUMBER_INVALID));
        if((user.getDocumentType() == DocumentType.CNPJ) && (user.getDocumentNumber() != null || !user.getDocumentNumber().equals("")) && !user.getDocumentNumber().matches(StringUtils.PATTERN_CNPJ))
            errors.add(new ServiceErrorItem("Número do Documento inválido",CODE_ERROR_DOCUMENT_NUMBER_INVALID));
        if(user.getPhone() != null && !user.getPhone().equals("") && !user.getPhone().matches(StringUtils.PATTERN_PHONE))
            errors.add(new ServiceErrorItem("Número de Telefone Inválido",CODE_ERROR_PHONE_INVALID));

        if(!errors.isEmpty())
            throw new UserException("Não foi possível atualizar o perfil",errors,400);

        if(foundUser.getIdentityVerificationStatus() == IdentityVerificationStatus.UNVERIFIED || foundUser.getIdentityVerificationStatus() == IdentityVerificationStatus.DENIED) {
            foundUser.setName(user.getName());
            foundUser.setDocumentType(user.getDocumentType());
            foundUser.setDocumentNumber(user.getDocumentNumber());
            foundUser.setPhone(user.getPhone());
        } else {
            foundUser.setPhone(user.getPhone());
            foundUser.setName(user.getName());
        }

        userRepository.save(foundUser);

        return foundUser;
    }

    @Override
    public User checkEmailConfirmation(String emailConfirmationToken) throws UserException {
        User user = userRepository.findByEmailConfirmationToken(emailConfirmationToken);

        if(user == null)
            throw new UserException("Link Inválido",null,401);
        else {
            //if email was already verified
            if(user.isEmailVerified())
                throw new UserException("Link Inválido",null,401);
            user.setEmailVerified(true);
        }

        try {
            slackService.sendMessage("Conta do usuário *"+user.getName() +"*("+ user.getEmail()+") Confirmada com sucesso!");
        } catch (SlackException e) {
            logger.error("error on send slack message");
        }

        try {
            userRepository.save(user);
            return user;
        } catch (Exception e) {
            logger.error("error on check email confirmation",e);
            List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
            errors.add(new ServiceErrorItem("Ocorreu um erro no servidor",CODE_ERROR_GENERIC));
            throw new UserException("Ocorreu um erro no servidor",errors,500);
        }
    }

    @Override
    public User addAvailableBalanceBrl(String email, BigDecimal amount) throws UserException {
        userRepository.findByEmail(email);
        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        //email not found
        User user = userRepository.findByEmail(email);
        if(user == null) {
            errors.add(new ServiceErrorItem("Email não encontrado",CODE_ERROR_EMAIL_NOT_FOUND));
            throw new UserException("Email não encontrado",errors,400);
        }

        //invalid amount
        if(amount == null) {
            errors.add(new ServiceErrorItem("Saldo inválido",CODE_ERROR_BALANCE_INVALID));
            throw new UserException("Saldo inválido",errors,400);
        }

        BigDecimal currentAvailableBalanceBrl = (user.getAvailableBalanceBrl() == null) ? BigDecimal.ZERO : user.getAvailableBalanceBrl();
        user.setAvailableBalanceBrl(currentAvailableBalanceBrl.add(amount));

        if(user.getAvailableBalanceBrl().compareTo(BigDecimal.ZERO) < 0) {
            errors.add(new ServiceErrorItem("Saldo insuficiente",CODE_ERROR_BALANCE_NOT_ENOUGH));
            throw new UserException("Saldo insuficiente",errors,400);
        }

        try {
            userRepository.save(user);

            //produce a message new available balance
            Map<String,Object> payload = new HashMap<>();
            payload.put("email",email);
            kafkaTemplate.send(updatedBrlBalanceTopic,payload);

            return user;
        } catch (Exception e) {
            logger.error("error on update user",e);
            throw new UserException("Ocorreu um erro no servidor",null,500);
        }
    }

    @Override
    public User getInfo(String email) throws UserException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(email == null || email.equals("")) {
            errors.add(new ServiceErrorItem("email não pode ser vazio",CODE_ERROR_EMAIL_NOT_NULL));
            throw new UserException("Email não pode ser vazio",errors,400);
        }

        User user = null;
        try {
            user = userRepository.findByEmail(email);
        } catch (Exception e) {
            logger.error("error on retrieve user info",e);
            errors.add(new ServiceErrorItem("Ocorreu um erro no servidor",CODE_ERROR_GENERIC));
            throw new UserException("Ocorreu um erro no servidor",errors,500);
        }

        if(user == null) {
            errors.add(new ServiceErrorItem("Usuário não encontrado",CODE_ERROR_USER_NOT_FOUND));
            throw new UserException("Usuário não encontrado",errors,400);
        } else {
            return user;
        }
    }

    @Override
    public RedefinePasswordRequest requestNewPassword(String email) throws RedefinePasswordRequestException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(email == null || email.equals(""))
            errors.add(new ServiceErrorItem("Email não pode ser vazio",CODE_ERROR_EMAIL_NOT_NULL));

        if(!Pattern.compile(StringUtils.PATTERN_EMAIL).matcher(email).matches())
            errors.add(new ServiceErrorItem("Email inválido",CODE_ERROR_EMAIL_INVALID));

        if(email != null && email.length() > 100)
            errors.add(new ServiceErrorItem("Email não pode ser maior que 100 caracteres",CODE_ERROR_EMAIL_MIN_LENGTH));

        if(!errors.isEmpty())
            throw new RedefinePasswordRequestException("Não foi possível redefinir senha",errors,400);

        //email not found
        User user = userRepository.findByEmail(email);
        if(user == null)
                errors.add(new ServiceErrorItem("Email não encontrado",CODE_ERROR_EMAIL_NOT_FOUND));
        if(!errors.isEmpty())
            throw new RedefinePasswordRequestException("redefine password error",errors,400);

        //validate if there is a recent request
        List<RedefinePasswordRequest> redefinePasswordRequests = redefinePasswordRequestRepository.findByEmailOrderByCreatedDesc(email);
        if(!redefinePasswordRequests.isEmpty()){
            Date now = new Date();
            RedefinePasswordRequest lastRedefineRequest = redefinePasswordRequests.get(0);
            long diff = now.getTime() - lastRedefineRequest.getCreated().getTime();
            long diffHours = diff / (60 * 60 * 1000);
            //if request was made less then 2 hours, block it
            if (diffHours < 2){
                errors.add(new ServiceErrorItem("Você fez uma solicitação de senha recentemente, por favor aguarde 2 horas para fazer outra solicitação",CODE_ERROR_REQUEST_NEW_PASSOWORD_ABUSE));
                throw new RedefinePasswordRequestException("redefine password error",errors,400);
            }
        }

        RedefinePasswordRequest redefinePasswordRequest = new RedefinePasswordRequest();
        redefinePasswordRequest.setCreated(new Date());
        redefinePasswordRequest.setEmail(email);
        redefinePasswordRequest.setToken(generateRedefinePasswordToken(email));

        try {
            return redefinePasswordRequestRepository.save(redefinePasswordRequest);
        } catch (Exception e) {
            logger.error("redefine password error",e);
            errors.add(new ServiceErrorItem("Ocorreu um erro no servidor",CODE_ERROR_GENERIC));
            throw new RedefinePasswordRequestException("Ocorreu um erro no servidor",errors,500);
        }
    }

    @Override
    public boolean isRedefinePasswordTokenValid(String token) throws RedefinePasswordInvalidTokenException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(REDEFINE_PASSWORD_SECRET_KEY_TOKEN);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("crypfy")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException exception){
            return false;
        } catch (JWTVerificationException exception){
            return false;
        }
    }

    private DecodedJWT getDecodedRedefinePasswordToken(String token) throws RedefinePasswordInvalidTokenException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(REDEFINE_PASSWORD_SECRET_KEY_TOKEN);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("crypfy")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return jwt;
        } catch (UnsupportedEncodingException e){
            logger.error("error on decode token",e);
            List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
            errors.add(new ServiceErrorItem("Ocorreu um erro no servidor",CODE_ERROR_GENERIC));
            throw new RedefinePasswordInvalidTokenException("Ocorreu um erro no servidor",errors,500);
        } catch (JWTVerificationException e){
            logger.error("error on verify jwt token",e);
            List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
            errors.add(new ServiceErrorItem("Ocorreu um erro no servidor",CODE_ERROR_GENERIC));
            throw new RedefinePasswordInvalidTokenException("Ocorreu um erro no servidor",errors,500);
        }
    }

    @Override
    public User redefinePassword(String token, String newPassword, String confirmNewPassword) throws RedefinePasswordRequestException {

        //test if token is valid
        try {
            isRedefinePasswordTokenValid(token);
        } catch (RedefinePasswordInvalidTokenException e) {
            List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
            errors.add(new ServiceErrorItem("Token Inválido",CODE_ERROR_REQUEST_NEW_PASSWORD_TOKEN_INVALID));
            throw new RedefinePasswordRequestException("Token Inválido",errors,400);
        }

        //test basic params
        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(newPassword == null || newPassword.equals(""))
            errors.add(new ServiceErrorItem("Senha é obrigatório",CODE_ERROR_PASSWORD_NOT_NULL));
        if(confirmNewPassword == null || confirmNewPassword.equals(""))
            errors.add(new ServiceErrorItem("Confirmação de senha é obrigatório",CODE_ERROR_CONFIRM_PASSWORD_NOT_NULL));
        if(newPassword != null && confirmNewPassword != null & !newPassword.equals(confirmNewPassword))
            errors.add(new ServiceErrorItem("Divergência na confirmação de senha",CODE_ERROR_PASSWORD_DIVERGENCE));
        if(newPassword != null &&  newPassword.length() < 8)
            errors.add(new ServiceErrorItem("Senha deve ter no mínimo 8 caracteres",CODE_ERROR_PASSWORD_MIN_LENGTH));

        if(!errors.isEmpty())
            throw new RedefinePasswordRequestException("Erro ao redefinir a senha",errors,400);

        //decode jwt token
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = getDecodedRedefinePasswordToken(token);
        } catch (RedefinePasswordInvalidTokenException e) {
            logger.error("error on decode jwt token",e);
            errors.add(new ServiceErrorItem("Ocorreu um erro no servidor",CODE_ERROR_GENERIC));
            throw new RedefinePasswordRequestException("Ocorreu um erro no servidor",errors,500);
        }

        //find user
        String email = decodedJWT.getClaim("email").asString();
        User user = userRepository.findByEmail(email);
        if(user == null) {
            errors.add(new ServiceErrorItem("Token Inválido",CODE_ERROR_REQUEST_NEW_PASSWORD_TOKEN_INVALID));
            throw new RedefinePasswordRequestException("Token Inválido",errors,500);
        }

        //update the new password
        user.setPassword(StringUtils.getMD5(newPassword));
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("error on redefine password",e);
            errors.add(new ServiceErrorItem("ocorreu um erro no servidor",CODE_ERROR_GENERIC));
            throw new RedefinePasswordRequestException("Ocorreu um erro no servidor",errors,500);
        }
    }

    @Override
    public List<User> list(DocumentVerificationStatus documentStatus, IdentityVerificationStatus identityStatus, String email) throws UserException {
        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        List<User> users = new ArrayList<>();

        try {
            users = userRepository.list(documentStatus,identityStatus,email);
        } catch (Exception e) {
            logger.error("error on get user list", e);
            errors.add(new ServiceErrorItem("ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            throw new UserException("Ocorreu um erro no servidor", errors, 500);
        }

        return users;
    }

    @Override
    public User redefinePassword(String email,String currentPassword, String newPassword, String confirmNewPassword) throws RedefinePasswordRequestException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        User user = null;

        if(currentPassword != null)
            user = userRepository.findByEmailAndPassword(email,StringUtils.getMD5(currentPassword));

        if(user == null)
            errors.add(new ServiceErrorItem("Senha atual inválida",CODE_ERROR_CURRENT_PASSWORD_INVALID));
        if(newPassword == null || newPassword.equals(""))
            errors.add(new ServiceErrorItem("Senha é obrigatório",CODE_ERROR_PASSWORD_NOT_NULL));
        if(currentPassword == null || currentPassword.equals(""))
            errors.add(new ServiceErrorItem("Senha atual é obrigatório",CODE_ERROR_CURRENT_PASSWORD_NOT_NULL));
        if(confirmNewPassword == null || confirmNewPassword.equals(""))
            errors.add(new ServiceErrorItem("Confirmação de senha é obrigatório",CODE_ERROR_CONFIRM_PASSWORD_NOT_NULL));
        if(newPassword != null && confirmNewPassword != null & !newPassword.equals(confirmNewPassword))
            errors.add(new ServiceErrorItem("Divergência na confirmação de senha",CODE_ERROR_PASSWORD_DIVERGENCE));
        if(newPassword != null && !newPassword.equals("") && newPassword.length() < 8)
            errors.add(new ServiceErrorItem("Senha deve ter no mínimo 8 caracteres",CODE_ERROR_PASSWORD_MIN_LENGTH));


        if(!errors.isEmpty())
            throw new RedefinePasswordRequestException("Ocorreu um erro ao redefinir a senha",errors,400);

        //second level validation
        if(currentPassword.equals(newPassword)) {
            errors.add(new ServiceErrorItem("A nova senha não pode ser uma senha antiga",CODE_ERROR_NEW_PASSWORD_INVALID));
            throw new RedefinePasswordRequestException("Ocorreu um erro ao redefinir a senha",errors,400);
        }

        user.setPassword(StringUtils.getMD5(newPassword));
        return userRepository.save(user);
    }

    @Override
    public User uploadAvatar(String email, String avatar) throws UserException {
        User user = userRepository.findByEmail(email);

        if(user == null)
            throw new UserException("Usuário não encontrado",null,400);

        user.setAvatar(avatar);
        userRepository.save(user);

        return user;
    }

    @Override
    public boolean isAccountVerified(String email) throws UserException {
        User user = userRepository.findByEmail(email);

        if(user == null)
            throw new UserException("Usuário não encontrado",null,400);

        return (user.getIdentityVerificationStatus() == IdentityVerificationStatus.VERIFIED && user.getIdentityVerificationStatus() == IdentityVerificationStatus.VERIFIED) ? true : false;
    }

    @Override
    public void resendEmailConfirmation(String email) throws UserException {
        User user = userRepository.findByEmail(email);

        if(user == null)
            throw new UserException("Usuário não encontrado",null,400);

        sendEmailConfirmation(user);
    }

    @Override
    public User updateIdentityVerificationStatusToWaitingApproval(String email, String identityVerificationPhoto) throws UserException {

        User user = userRepository.findByEmail(email);

        if(user == null)
            throw new UserException("Usuário não encontrado",null,400);

        if(identityVerificationPhoto == null || identityVerificationPhoto.equals(""))
            throw new UserException("Foto é obrigatório",null,400);

        try {
            slackService.sendMessage("Nova solicitação de aprovação de IDENTIDADE pelo usuário *"+email+"*");
        } catch (SlackException e) {
            logger.error("error on send slack message");
        }

        user.setIdentityVerificationPhoto(identityVerificationPhoto);
        user.setIdentityVerificationStatus(IdentityVerificationStatus.WAITING_APPROVAL);
        userRepository.save(user);

        return user;
    }

    @Override
    public User updateDocumentVerificationStatusToWaitingApproval(String email, String documentVerificationPhoto) throws UserException {
        User user = userRepository.findByEmail(email);

        if(user == null)
            throw new UserException("Usuário não encontrado",null,400);

        if(documentVerificationPhoto == null || documentVerificationPhoto.equals(""))
            throw new UserException("Foto é obrigatório",null,400);

        try {
            slackService.sendMessage("Nova solicitação de aprovação de DOCUMENTO pelo usuário *"+email+"*");
        } catch (SlackException e) {
            logger.error("error on send slack message");
        }

        user.setDocumentVerificationPhoto(documentVerificationPhoto);
        user.setDocumentVerificationStatus(DocumentVerificationStatus.WAITING_APPROVAL);
        userRepository.save(user);

        return user;
    }

    @Override
    public User updateIdentityVerificationStatusToVerified(String email) throws UserException {
        User user = userRepository.findByEmail(email);

        if(user == null)
            throw new UserException("Usuário não encontrado",null,400);
        user.setIdentityVerificationStatus(IdentityVerificationStatus.VERIFIED);
        userRepository.save(user);

        //end email notification
        sendGeneralIdentityVerificationEmail(user);

        return user;
    }

    @Override
    public User updateDocumentVerificationStatusToVerified(String email) throws UserException {
        User user = userRepository.findByEmail(email);

        if(user == null)
            throw new UserException("Usuário não encontrado",null,400);
        user.setDocumentVerificationStatus(DocumentVerificationStatus.VERIFIED);
        userRepository.save(user);

        //end email notification
        sendGeneralDocumentVerificationEmail(user);

        return user;
    }

    @Override
    public User updateIdentityVerificationStatusToDenied(String email,String deniedReason) throws UserException {
        User user = userRepository.findByEmail(email);

        if(deniedReason == null || deniedReason.equals(""))
            throw new UserException("Motivo de status negado é obrigatório",null,400);

        if(user == null)
            throw new UserException("Usuário não encontrado",null,400);
        user.setIdentityVerificationStatus(IdentityVerificationStatus.DENIED);
        user.setDeniedReasonIdentityVerification(deniedReason);
        userRepository.save(user);

        //end email notification
        sendGeneralIdentityVerificationEmail(user);

        return user;
    }

    @Override
    public User updateDocumentVerificationStatusToDenied(String email,String deniedReason) throws UserException {
        User user = userRepository.findByEmail(email);

        if(deniedReason == null || deniedReason.equals(""))
            throw new UserException("Motivo de status negado é obrigatório",null,400);

        if(user == null)
            throw new UserException("Usuário não encontrado",null,400);

        user.setDocumentVerificationStatus(DocumentVerificationStatus.DENIED);
        user.setDeniedReasonDocumentVerification(deniedReason);
        userRepository.save(user);

        //end email notification
        sendGeneralDocumentVerificationEmail(user);

        return user;
    }

    private String generateRedefinePasswordToken(String email) throws RedefinePasswordRequestException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(REDEFINE_PASSWORD_SECRET_KEY_TOKEN);

            Map<String, Object> headerClaims = new HashMap();
            headerClaims.put("owner", "crypfy");

            String token = JWT.create().
                    withIssuer("crypfy").withExpiresAt(DateUtils.addDaysFromNow(1))
                    .withIssuedAt(new Date()).withClaim("email",email).withHeader(headerClaims)
                    .sign(algorithm);

            return token;
        } catch (UnsupportedEncodingException e) {
            List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
            logger.error("error build jwt token (redefine password token)",e);
            errors.add(new ServiceErrorItem("ocorreu um erro no servidor",CODE_ERROR_GENERIC));
            throw new RedefinePasswordRequestException("redefine password error",errors);
        }
    }


    private String generateEmailConfirmationToken(String email) {
        String randomString = StringUtils.generateRandomString();
        String mixedString = randomString + email;
        return new String(Base64.getEncoder().encode(mixedString.getBytes())).replaceAll("=","");
    }

    private List<ServiceErrorItem> validateAddUser(User user) {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        //email validation
        if(user.getEmail() == null || user.getEmail().equals(""))
            errors.add(new ServiceErrorItem("Email é obrigatório",CODE_ERROR_EMAIL_NOT_NULL));
        if(user.getEmail() != null && !user.getEmail().equals("") && !Pattern.compile(StringUtils.PATTERN_EMAIL).matcher(user.getEmail()).matches())
            errors.add(new ServiceErrorItem("Email inválido",CODE_ERROR_EMAIL_INVALID));
        if(user.getEmail() != null && user.getEmail().length() > 100)
            errors.add(new ServiceErrorItem("Email não pode ser maior que 100 caracteres",CODE_ERROR_EMAIL_MIN_LENGTH));

        //check if user already been included
        User insertedUser = userRepository.findById(user.getEmail()).orElse(null);
        if(insertedUser != null)
            errors.add(new ServiceErrorItem("Email já cadastrado",CODE_ERROR_EMAIL_IN_USE));

        //name validation
        if(user.getName() == null || user.getName().equals(""))
            errors.add(new ServiceErrorItem("Nome é obrigatório",CODE_ERROR_NAME_NOT_NULL));
        if(user.getName() != null && !user.getName().equals("") && user.getName().length() > 200)
            errors.add(new ServiceErrorItem("Nome não pode ser maior que 200 caracteres",CODE_ERROR_NAME_MIN_LENGTH));

        //password validation
        if(user.getPassword() == null || user.getPassword().equals(""))
            errors.add(new ServiceErrorItem("Senha é obrigatório",CODE_ERROR_PASSWORD_NOT_NULL));
        if(user.getConfirmPassword() == null || user.getConfirmPassword().equals(""))
            errors.add(new ServiceErrorItem("Confirmação de senha é obrigatório",CODE_ERROR_CONFIRM_PASSWORD_NOT_NULL));
        if(user.getPassword() != null && user.getConfirmPassword() != null & !user.getPassword().equals(user.getConfirmPassword()))
            errors.add(new ServiceErrorItem("Divergência na confirmação de senha",CODE_ERROR_PASSWORD_DIVERGENCE));
        if(user.getPassword() != null && !user.getPassword().equals("") && user.getPassword().length() < 8)
            errors.add(new ServiceErrorItem("Senha deve ter no mínimo 8 caracteres",CODE_ERROR_PASSWORD_MIN_LENGTH));

        if(user.getDocumentType() == null)
            errors.add(new ServiceErrorItem("Tipo de Documento é obrigatório",CODE_ERROR_DOCUMENT_TYPE_NOT_NULL));
        if(user.getDocumentNumber() == null || user.getDocumentNumber().equals(""))
            errors.add(new ServiceErrorItem("Número do Documento é obrigatório",CODE_ERROR_DOCUMENT_NUMBER_NOT_NULL));
        if(user.getDocumentNumber() != null && !user.getDocumentNumber().equals("") && user.getDocumentType() == DocumentType.CNPJ && !Pattern.compile(StringUtils.PATTERN_CNPJ).matcher(user.getDocumentNumber()).matches())
            errors.add(new ServiceErrorItem("Número do Documento inválido",CODE_ERROR_DOCUMENT_NUMBER_INVALID));
        if(user.getDocumentType() == DocumentType.CPF && user.getDocumentNumber() !=null && !user.getDocumentNumber().equals("") && !Pattern.compile(StringUtils.PATTERN_CPF).matcher(user.getDocumentNumber()).matches())
            errors.add(new ServiceErrorItem("Número do Documento inválido",CODE_ERROR_DOCUMENT_NUMBER_INVALID));

        //phone
        if(user.getPhone() != null && !user.getPhone().equals("") && !user.getPhone().matches(StringUtils.PATTERN_PHONE))
            errors.add(new ServiceErrorItem("Número de Telefone Inválido",CODE_ERROR_PHONE_INVALID));

        return errors;
    }

    @Async
    public void sendGeneralIdentityVerificationEmail(User user) {
        try {
            String template = null;
            String subject = null;

            Map<String,String> params = new HashMap<>();
            params.put("user",user.getName());

            if(user.getIdentityVerificationStatus() == IdentityVerificationStatus.VERIFIED) {
                template = "identity_verified.ftl";
                subject = "Identidade Verificada com Sucesso";
            }

            if(user.getIdentityVerificationStatus() == IdentityVerificationStatus.DENIED) {
                template = "identity_denied.ftl";
                subject = "Identidade Negada";
                params.put("denied_reason",user.getDeniedReasonIdentityVerification());
            }

            String bodyHtml = htmlTemplateBuilder.buildTemplate(template,params);
            mailSender.sendSimpleMailHtml(user.getEmail(),"no-reply@crypfy.com",subject,"Crypfy Plataforrma",bodyHtml);
        } catch (HtmlTemplateBuilderException e) {
            logger.error("error on build html",e);
        } catch (EmailException e) {
            logger.error("error on send email",e);
        }
    }

    @Async
    public void sendGeneralDocumentVerificationEmail(User user) {
        try {
            String template = null;
            String subject = null;

            Map<String,String> params = new HashMap<>();
            params.put("user",user.getName());

            if(user.getDocumentVerificationStatus() == DocumentVerificationStatus.VERIFIED) {
                template = "document_verified.ftl";
                subject = "Documento Verificado com Sucesso";
            }

            if(user.getDocumentVerificationStatus() == DocumentVerificationStatus.DENIED) {
                template = "document_denied.ftl";
                subject = "Documento Negado";
                params.put("denied_reason",user.getDeniedReasonDocumentVerification());
            }

            String bodyHtml = htmlTemplateBuilder.buildTemplate(template,params);
            mailSender.sendSimpleMailHtml(user.getEmail(),"no-reply@crypfy.com",subject,"Crypfy Plataforrma",bodyHtml);
        } catch (HtmlTemplateBuilderException e) {
            logger.error("error on build html",e);
        } catch (EmailException e) {
            logger.error("error on send email",e);
        }
    }

}
