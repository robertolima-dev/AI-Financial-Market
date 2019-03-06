package com.mali.crypfy.moneymanager.core.impl;

import com.mali.crypfy.moneymanager.core.BankAccountService;
import com.mali.crypfy.moneymanager.core.DepositBrlService;
import com.mali.crypfy.moneymanager.core.exception.BankAccountException;
import com.mali.crypfy.moneymanager.core.exception.DepositBrlException;
import com.mali.crypfy.moneymanager.core.exception.NoResultException;
import com.mali.crypfy.moneymanager.core.validator.ServiceItemError;
import com.mali.crypfy.moneymanager.integration.auth.UserService;
import com.mali.crypfy.moneymanager.integration.auth.exception.UserException;
import com.mali.crypfy.moneymanager.integration.slack.SlackService;
import com.mali.crypfy.moneymanager.integration.slack.exception.SlackException;
import com.mali.crypfy.moneymanager.persistence.entity.BankAccount;
import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.StatusDepositWithdrawBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.TypeDepositWithdraw;
import com.mali.crypfy.moneymanager.persistence.repository.DepositWithdrawRequestBrlRepository;
import com.mali.crypfy.moneymanager.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Implementation of Deposit Brl Service
 */
@Service
public class DepositBrlServiceImpl implements DepositBrlService {

    final static Logger logger = LoggerFactory.getLogger(DepositBrlServiceImpl.class);

    public static final int CODE_ERROR_AMOUNT_NOT_NULL = 1000;
    public static final int CODE_ERROR_BANK_ACCOUNT_NOT_NULL = 1001;
    public static final int CODE_ERROR_TYPE_NOT_NULL = 1003;
    public static final int CODE_ERROR_PHOTO_NOT_NULL = 1004;
    public static final int CODE_ERROR_DEPOSIT_WITHDRAWL_NOT_FOUND = 1005;
    public static final int CODE_ERROR_INVALID_STATUS = 1006;
    public static final int CODE_ERROR_EMAIL_NOT_NULL = 1007;
    public static final int CODE_ERROR_BANK_ACCOUNT_NOT_FOUND = 1008;
    public static final int CODE_ERROR_INVALID_AMOUNT = 1011;
    public static final int CODE_ERROR_ACCOUNT_NOT_VERIFIED = 1012;
    public static final int CODE_ERROR_DENIED_REASON_NOT_NULL = 1015;

    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private DepositWithdrawRequestBrlRepository depositWithdrawRequestBrlRepository;
    @Autowired
    private SlackService slackService;
    @Autowired
    private KafkaTemplate<String, DepositWithdrawRequestBrl> kafkaTemplateDepositBrl;
    @Autowired
    private KafkaTemplate<String, Map<String,Object>> kafkaTemplateMap;

    //queue topics config
    @Value("${app.integrations.topics.deposit-brl.change-status-to-confirmed}")
    private String changeStatusToConfirmedTopic;
    @Value("${app.integrations.topics.deposit-brl.change-status-to-denied}")
    private String changeStatusToDeniedTopic;
    @Value("${app.integrations.topics.deposit-brl.change-status-to-cancelled}")
    private String changeStatusToCancelledTopic;
    @Value("${app.integrations.topics.deposit-brl.change-status-to-waiting-approval}")
    private String changeStatusToWaitingApprovalTopic;
    @Value("${app.integrations.topics.deposit-brl.change-status-to-waiting-photo-upload}")
    private String changeStatusToWaitingPhotoUploadTopic;
    @Value("${app.integrations.topics.auth.add-brl-balance}")
    private String addBrlBalanceTopic;

    @Override
    public DepositWithdrawRequestBrl add(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws DepositBrlException {

        try {
            //check account verification
            checkAccountVerification(depositWithdrawRequestBrl.getEmail());
            //pre add validations
            preAddValidation(depositWithdrawRequestBrl);

            depositWithdrawRequestBrl.setType(TypeDepositWithdraw.DEPOSIT);
            depositWithdrawRequestBrl.setStatus(StatusDepositWithdrawBrl.WAITING_PHOTO_UPLOAD);
            depositWithdrawRequestBrl.setCreated(new Date());
            depositWithdrawRequestBrl.setLastUpdated(new Date());

            //link bank account info to deposit brl
            BankAccount bankAccount = bankAccountService.findById(depositWithdrawRequestBrl.getIdbankAccount());
            depositWithdrawRequestBrl.setBankName(bankAccount.getBank().getName());
            depositWithdrawRequestBrl.setBankCode(bankAccount.getBank().getCode());
            depositWithdrawRequestBrl.setBankLogo(bankAccount.getBank().getLogo());
            depositWithdrawRequestBrl.setBankAgency(bankAccount.getAgency());
            depositWithdrawRequestBrl.setBankAccountNumber(bankAccount.getAccountNumber());
            depositWithdrawRequestBrl.setBankAccountType(bankAccount.getType());
            depositWithdrawRequestBrl.setBankAccountNumberDigit(bankAccount.getAccountNumberDigit());
            depositWithdrawRequestBrl.setBankAccountDocumentType(bankAccount.getDocumentType());
            depositWithdrawRequestBrl.setBankAccountDocumentNumber(bankAccount.getDocumentNumber());

        } catch (BankAccountException e) {
            throw new DepositBrlException("Ocorreu um erro no servidor",500);
        } catch (NoResultException e) {
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Conta Bancária não encontrada", CODE_ERROR_BANK_ACCOUNT_NOT_FOUND));
            throw new DepositBrlException("Intenção de depósito inválida", errors, 400);
        }

        try {
            //save
            depositWithdrawRequestBrlRepository.save(depositWithdrawRequestBrl);

            //produce message topic
            kafkaTemplateDepositBrl.send(changeStatusToWaitingPhotoUploadTopic,depositWithdrawRequestBrl);
        } catch (Exception e) {
            logger.error("deposit error",e);
            throw new DepositBrlException("Ocorreu um erro no servidor",500);
        }

        //slack msg
        try {
            slackService.sendMessage("Nova solicitação de DEPÓSITO no valor de *" + StringUtils.toMoneyFormat(depositWithdrawRequestBrl.getAmount()) + "* cliente *" + depositWithdrawRequestBrl.getEmail() + "*");
        } catch (SlackException e) {
            //do nothing
            logger.error("slack msg error",e);
        }

        return depositWithdrawRequestBrl;
    }

    @Override
    public DepositWithdrawRequestBrl changeToWaitingApproval(Integer idDeposit, String email, String photoUploadUrl) throws DepositBrlException {

        //try find deposit
        DepositWithdrawRequestBrl depositWithdrawRequestBrl = null;
        try {
            depositWithdrawRequestBrl = findByIdAndEmail(idDeposit,email);
        } catch (NoResultException e) {
            logger.error("deposit not found",e);
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Depósito não encontrado",CODE_ERROR_DEPOSIT_WITHDRAWL_NOT_FOUND));
            throw new DepositBrlException("Ocorreu um erro ao mudar o status do depósito",errors,400);
        }

        //validation
        preChangeToWaitingApprovalValidation(depositWithdrawRequestBrl);

        try {

            depositWithdrawRequestBrl.setPhoto(photoUploadUrl);
            depositWithdrawRequestBrl.setStatus(StatusDepositWithdrawBrl.WAITING_APPROVAL);
            depositWithdrawRequestBrl.setLastUpdated(new Date());
            depositWithdrawRequestBrlRepository.save(depositWithdrawRequestBrl);

            //produce message topic
            kafkaTemplateDepositBrl.send(changeStatusToWaitingApprovalTopic,depositWithdrawRequestBrl);

        } catch (Exception e) {
            logger.error("deposit error",e);
            throw new DepositBrlException("Ocorreu um erro no servidor",null,500);
        }

        //slack msg
        try {
            slackService.sendMessage("NOVO UPLOAD de comprovante de depósito BRL ID *" + depositWithdrawRequestBrl.getIddepositWithdrawRequestBrl().toString() + "* cliente *" + depositWithdrawRequestBrl.getEmail()+"*");
        } catch (SlackException e) {
            //do nothing
            logger.error("slack msg error",e);
        }

        return depositWithdrawRequestBrl;
    }

    @Override
    public DepositWithdrawRequestBrl changeToConfirmed(Integer idDeposit) throws DepositBrlException {

        //try find deposit
        DepositWithdrawRequestBrl depositWithdrawRequestBrl = null;
        try {
            depositWithdrawRequestBrl = findById(idDeposit);
        } catch (NoResultException e) {
            logger.error("deposit not found",e);
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Depósito não encontrado",CODE_ERROR_DEPOSIT_WITHDRAWL_NOT_FOUND));
            throw new DepositBrlException("Depósito não encontrado",errors,400);
        }

        //pre validation
        preChangeToConfirmedValidation(depositWithdrawRequestBrl);

        try {

            depositWithdrawRequestBrl.setLastUpdated(new Date());
            depositWithdrawRequestBrl.setStatus(StatusDepositWithdrawBrl.CONFIRMED);
            depositWithdrawRequestBrlRepository.save(depositWithdrawRequestBrl);

            //notify kafka change status confirmed event
            kafkaTemplateDepositBrl.send(changeStatusToConfirmedTopic,depositWithdrawRequestBrl);

            //notify kafka topic add brl balance
            Map<String,Object> addBrlBalanceJSON = new HashMap<String,Object>();
            addBrlBalanceJSON.put("email",depositWithdrawRequestBrl.getEmail());
            addBrlBalanceJSON.put("balance",depositWithdrawRequestBrl.getAmount());
            kafkaTemplateMap.send(addBrlBalanceTopic,addBrlBalanceJSON);

        } catch (Exception e) {
            logger.error("deposit error",e);
            throw new DepositBrlException("Ocorreu um erro no servidor",500);
        }

        return depositWithdrawRequestBrl;
    }

    @Override
    public DepositWithdrawRequestBrl changeToDenied(Integer idDeposit,String deniedReason) throws DepositBrlException {

        //try find deposit
        DepositWithdrawRequestBrl depositWithdrawRequestBrl = null;
        try {
            depositWithdrawRequestBrl = findById(idDeposit);
        } catch (NoResultException e) {
            logger.error("deposit not found",e);
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Depósito não encontrado",CODE_ERROR_DEPOSIT_WITHDRAWL_NOT_FOUND));
            throw new DepositBrlException("Ocorreu um erro ao mudar o status do depósito",errors,400);
        }

        //pre validation
        preChangeToDeniedValidation(depositWithdrawRequestBrl);

        try {

            depositWithdrawRequestBrl.setDeniedReason(deniedReason);
            depositWithdrawRequestBrl.setLastUpdated(new Date());
            depositWithdrawRequestBrl.setStatus(StatusDepositWithdrawBrl.DENIED);
            depositWithdrawRequestBrlRepository.save(depositWithdrawRequestBrl);

            //notify kafka change status denied event
            kafkaTemplateDepositBrl.send(changeStatusToDeniedTopic,depositWithdrawRequestBrl);
        } catch (Exception e) {
            logger.error("deposit error",e);
            throw new DepositBrlException("Ocorreu um erro no servidor",500);
        }

        return depositWithdrawRequestBrl;
    }

    @Override
    public DepositWithdrawRequestBrl changeToCancelled(Integer idDeposit, String email) throws DepositBrlException {

        //try find deposit
        DepositWithdrawRequestBrl depositWithdrawRequestBrl = null;
        try {
            depositWithdrawRequestBrl = findById(idDeposit);
        } catch (NoResultException e) {
            logger.error("deposit not found",e);
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Depósito não encontrado",CODE_ERROR_DEPOSIT_WITHDRAWL_NOT_FOUND));
            throw new DepositBrlException("Ocorreu um erro ao mudar o status do depósito",errors,400);
        }

        //pre validation
        preChangeToCancelled(depositWithdrawRequestBrl);

        try {

            depositWithdrawRequestBrl.setLastUpdated(new Date());
            depositWithdrawRequestBrl.setStatus(StatusDepositWithdrawBrl.CANCELLED);
            depositWithdrawRequestBrlRepository.save(depositWithdrawRequestBrl);

            //notify topic cancelled event
            kafkaTemplateDepositBrl.send(changeStatusToCancelledTopic,depositWithdrawRequestBrl);
        } catch (Exception e) {
            logger.error("deposit error",e);
            throw new DepositBrlException("Ocorreu um erro no servidor",500);
        }

        //send slack msg
        try {
            slackService.sendMessage("Novo CANCELAMENTO de comprovante de depósito BRL ID *" + depositWithdrawRequestBrl.getIddepositWithdrawRequestBrl().toString() + "* cliente *" + depositWithdrawRequestBrl.getEmail()+"*");
        } catch (SlackException e) {
            //do nothing
            logger.error("slack msg error",e);
        }

        return depositWithdrawRequestBrl;
    }

    @Override
    public DepositWithdrawRequestBrl findById(Integer idDeposit) throws DepositBrlException, NoResultException {
        Optional<DepositWithdrawRequestBrl> optional = depositWithdrawRequestBrlRepository.findById(idDeposit);
        if(optional.isPresent())
            return optional.get();
        else
            throw new NoResultException("no deposit found");
    }

    @Override
    public DepositWithdrawRequestBrl findByIdAndEmail(Integer idDeposit, String email) throws DepositBrlException, NoResultException {
        DepositWithdrawRequestBrl depositWithdrawRequestBrl = depositWithdrawRequestBrlRepository.findByIddepositWithdrawRequestBrlAndEmail(idDeposit,email);
        if(depositWithdrawRequestBrl == null)
            throw new NoResultException("no deposit found");
        else
            return depositWithdrawRequestBrl;
    }

    @Override
    public void delete(Integer idDeposit, String email) throws DepositBrlException {
        //try find deposit
        DepositWithdrawRequestBrl depositWithdrawRequestBrl = null;
        try {
            depositWithdrawRequestBrl = findByIdAndEmail(idDeposit,email);
        } catch (NoResultException e) {
            logger.error("deposit not found",e);
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Depósito não encontrado",CODE_ERROR_DEPOSIT_WITHDRAWL_NOT_FOUND));
            throw new DepositBrlException("Depósito não encontrado",errors,400);
        }

        if(depositWithdrawRequestBrl.getStatus() == StatusDepositWithdrawBrl.CANCELLED || depositWithdrawRequestBrl.getStatus() == StatusDepositWithdrawBrl.DENIED)
            depositWithdrawRequestBrlRepository.delete(depositWithdrawRequestBrl);
        else throw new DepositBrlException("Não foi possível deletar a intenção de depósito, o mesmo não se encontra em status cancelado ou negado",500);
    }

    @Override
    public List<DepositWithdrawRequestBrl> list(String email, StatusDepositWithdrawBrl status) throws DepositBrlException {
        try {
            return depositWithdrawRequestBrlRepository.findAll(email,TypeDepositWithdraw.DEPOSIT,status);
        } catch (Exception e) {
            logger.error("deposit error",e);
            throw new DepositBrlException("Ocorreu um erro no servidor",500);
        }
    }

    @Override
    public BigDecimal sumAmountConfirmed(String email) throws DepositBrlException {
        try {
            return depositWithdrawRequestBrlRepository.sumAmountByEmailAndStatus(TypeDepositWithdraw.DEPOSIT,email,StatusDepositWithdrawBrl.CONFIRMED);
        } catch (Exception e) {
            logger.error("deposit error",e);
            throw new DepositBrlException("Ocorreu um erro no servidor",500);
        }
    }

    @Override
    public Long count(String email, StatusDepositWithdrawBrl status) throws DepositBrlException {
        try {
            return depositWithdrawRequestBrlRepository.countAllByEmailAndStatusAndType(email,status,TypeDepositWithdraw.DEPOSIT);
        } catch (Exception e) {
            logger.error("deposit error",e);
            throw new DepositBrlException("ocorreu um erro no servidor",500);
        }
    }

    /**
     * Check if account is verified
     * @param email
     * @throws DepositBrlException
     */
    private void checkAccountVerification(String email) throws DepositBrlException {
        try {
            if (!userService.isAccountVerified(email)) {

                List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
                errors.add(new ServiceItemError("Sua conta não está verificada! Vá em configurações e faça a verificação", CODE_ERROR_ACCOUNT_NOT_VERIFIED));

                throw new DepositBrlException("Sua conta não está verificada! Vá em configurações e faça a verificação", errors, 400);
            }
        } catch (UserException e) {
            logger.error("user error", e);
            throw new DepositBrlException("Ocorreu um erro no servidor", null, 500);
        }
    }

    /**
     * Pre Deposit Brl Add Validation
     * @param depositWithdrawRequestBrl
     * @return List of Errors
     */
    private void preAddValidation(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws DepositBrlException{
        List<ServiceItemError> errors = new ArrayList<ServiceItemError>();

        if(depositWithdrawRequestBrl.getEmail() == null || depositWithdrawRequestBrl.getEmail().equals(""))
            errors.add(new ServiceItemError("Email não pode ser vazio",CODE_ERROR_EMAIL_NOT_NULL));
        if(depositWithdrawRequestBrl.getAmount() == null || depositWithdrawRequestBrl.getAmount().compareTo(BigDecimal.ZERO) == 0)
            errors.add(new ServiceItemError("Valor não pode ser vazio ou zero",CODE_ERROR_AMOUNT_NOT_NULL));
        if(depositWithdrawRequestBrl.getAmount() != null && depositWithdrawRequestBrl.getAmount().compareTo(new BigDecimal(1000)) < 0)
            errors.add(new ServiceItemError("Valor mínimo para depósito é R$ 1.000,00",CODE_ERROR_INVALID_AMOUNT));
        if(depositWithdrawRequestBrl.getIdbankAccount() == null)
            errors.add(new ServiceItemError("Conta Bancária é obrigatório",CODE_ERROR_BANK_ACCOUNT_NOT_NULL));
        if(depositWithdrawRequestBrl.getType() == null)
            errors.add(new ServiceItemError("Tipo de operação é obrigatório",CODE_ERROR_TYPE_NOT_NULL));

        if (!errors.isEmpty())
            throw new DepositBrlException("Intenção de depósito inválida", errors, 400);
    }

    /**
     * Pre Change Status To Waiting Approval Validation
     * @param depositWithdrawRequestBrl
     * @throws DepositBrlException
     */
    private void preChangeToWaitingApprovalValidation(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws DepositBrlException {

        List<ServiceItemError> errors = new ArrayList<ServiceItemError>();

        if(depositWithdrawRequestBrl.getStatus() != StatusDepositWithdrawBrl.WAITING_PHOTO_UPLOAD) {
            errors.add(new ServiceItemError("Não foi possível atualizar o status, depósito em status " + StatusDepositWithdrawBrl.getHumanName(depositWithdrawRequestBrl.getStatus()),CODE_ERROR_INVALID_STATUS));
            throw new DepositBrlException("Ocorreu um erro ao mudar o status do depósito",errors,400);
        }

        if(depositWithdrawRequestBrl.getPhoto()== null || depositWithdrawRequestBrl.getPhoto().equals("")){
            errors.add(new ServiceItemError("Upload de confirmação de depósito é obrigatório",CODE_ERROR_PHOTO_NOT_NULL));
            throw new DepositBrlException("Ocorreu um erro ao mudar o status do depósito",errors, 400);
        }

        if (!errors.isEmpty())
            throw new DepositBrlException("Intenção de depósito inválida", errors, 400);
    }

    /**
     * Pre Change Status to Confirmed Validation
     * @param depositWithdrawRequestBrl
     * @throws DepositBrlException
     */
    private void preChangeToConfirmedValidation(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws DepositBrlException {

        List<ServiceItemError> errors = new ArrayList<ServiceItemError>();

        if(depositWithdrawRequestBrl.getStatus() != StatusDepositWithdrawBrl.WAITING_APPROVAL) {
            errors.add(new ServiceItemError("Não foi possível atualizar o status, depósito em status " + StatusDepositWithdrawBrl.getHumanName(depositWithdrawRequestBrl.getStatus()),CODE_ERROR_INVALID_STATUS));
            throw new DepositBrlException("Ocorreu um erro ao mudar o status do depósito",errors,400);
        }

        if (!errors.isEmpty())
            throw new DepositBrlException("Intenção de depósito inválida", errors, 400);
    }

    /**
     * Pre Change Status to Denied Validation
     * @param depositWithdrawRequestBrl
     * @throws DepositBrlException
     */
    private void preChangeToDeniedValidation(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws DepositBrlException {

        List<ServiceItemError> errors = new ArrayList<ServiceItemError>();

        if(depositWithdrawRequestBrl.getStatus() != StatusDepositWithdrawBrl.WAITING_APPROVAL) {
            errors.add(new ServiceItemError("Não foi possível atualizar o status, depósito em status " + StatusDepositWithdrawBrl.getHumanName(depositWithdrawRequestBrl.getStatus()),CODE_ERROR_INVALID_STATUS));
            throw new DepositBrlException("Ocorreu um erro ao mudar o status do depósito",errors,400);
        }

        if(depositWithdrawRequestBrl.getDeniedReason()== null || depositWithdrawRequestBrl.getDeniedReason().equals("")) {
            errors.add(new ServiceItemError("Motivo de recusa obrigatório",CODE_ERROR_DENIED_REASON_NOT_NULL));
            throw new DepositBrlException("Ocorreu um erro ao mudar o status do saque",errors,400);
        }

        if (!errors.isEmpty())
            throw new DepositBrlException("Intenção de depósito inválida", errors, 400);
    }

    /**
     * Pre Change Status to Cancelled Validation
     * @param depositWithdrawRequestBrl
     * @throws DepositBrlException
     */
    private void preChangeToCancelled(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws DepositBrlException {

        List<ServiceItemError> errors = new ArrayList<ServiceItemError>();

        if(depositWithdrawRequestBrl.getStatus() != StatusDepositWithdrawBrl.WAITING_APPROVAL && depositWithdrawRequestBrl.getStatus() != StatusDepositWithdrawBrl.WAITING_PHOTO_UPLOAD) {
            errors.add(new ServiceItemError("Não foi possível atualizar o status, depósito em status " + StatusDepositWithdrawBrl.getHumanName(depositWithdrawRequestBrl.getStatus()),CODE_ERROR_INVALID_STATUS));
            throw new DepositBrlException("Ocorreu um erro ao mudar o status do depósito",errors,400);
        }

        if (!errors.isEmpty())
            throw new DepositBrlException("Intenção de depósito inválida", errors, 400);
    }
}
