package com.mali.crypfy.moneymanager.core.impl;

import com.mali.crypfy.moneymanager.core.BankAccountService;
import com.mali.crypfy.moneymanager.core.WithdrawBrlService;
import com.mali.crypfy.moneymanager.core.exception.*;
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
import com.mali.crypfy.moneymanager.utils.DateUtils;
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
 * Implementation of Withdrawal Service
 */
@Service
public class WithdrawBrlServiceImpl implements WithdrawBrlService {

    final static Logger logger = LoggerFactory.getLogger(WithdrawBrlServiceImpl.class);

    public static final BigDecimal WITHDRAW_BRL_FEE = new BigDecimal(17);
    public static final BigDecimal WITHDRAW_BRL_MIN_AMOUNT = new BigDecimal(30);
    public static final BigDecimal WITHDRAW_BRL_MAX_AMOUNT = new BigDecimal(10000);

    public static final int CODE_ERROR_AMOUNT_NOT_NULL = 1000;
    public static final int CODE_ERROR_BANK_ACCOUNT_NOT_NULL = 1001;
    public static final int CODE_ERROR_TYPE_NOT_NULL = 1003;
    public static final int CODE_ERROR_DEPOSIT_WITHDRAWL_NOT_FOUND = 1005;
    public static final int CODE_ERROR_INVALID_STATUS = 1006;
    public static final int CODE_ERROR_EMAIL_NOT_NULL = 1007;
    public static final int CODE_ERROR_BANK_ACCOUNT_NOT_FOUND = 1008;
    public static final int CODE_ERROR_UNAVAILABLE_AMOUNT = 1009;
    public static final int CODE_ERROR_ACCOUNT_NOT_VERIFIED = 1012;
    public static final int CODE_ERROR_MAX_AMOUNT = 1013;
    public static final int CODE_ERROR_DAILY_LIMIT = 1014;
    public static final int CODE_ERROR_DENIED_REASON_NOT_NULL = 1015;
    public static final int CODE_ERROR_MIN_AMOUNT = 1016;
    public static final int CODE_ERROR_GENERIC = 9000;

    //queue topics config
    @Value("${app.integrations.topics.withdraw-brl.change-status-to-confirmed}")
    private String changeStatusToConfirmedTopic;
    @Value("${app.integrations.topics.withdraw-brl.change-status-to-denied}")
    private String changeStatusToDeniedTopic;
    @Value("${app.integrations.topics.withdraw-brl.change-status-to-cancelled}")
    private String changeStatusToCancelledTopic;
    @Value("${app.integrations.topics.withdraw-brl.change-status-to-waiting-approval}")
    private String changeStatusToWaitingApprovalTopic;
    @Value("${app.integrations.topics.withdraw-brl.change-status-to-processing}")
    private String changeStatusToProcessingTopic;
    @Value("${app.integrations.topics.auth.add-brl-balance}")
    private String queueTopicAuthAddBrlBalance;

    @Autowired
    private DepositWithdrawRequestBrlRepository depositWithdrawRequestBrlRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private SlackService slackService;
    @Autowired
    private KafkaTemplate<String, DepositWithdrawRequestBrl> kafkaTemplateWithdrawal;
    @Autowired
    private KafkaTemplate<String, Map<String,Object>> kafkaTemplateMap;

    @Override
    public DepositWithdrawRequestBrl add(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws WithdrawBrlException {
        try {

            //check account verification
            checkAccountVerification(depositWithdrawRequestBrl.getEmail());
            //pre add validation
            preAddValidation(depositWithdrawRequestBrl);

            depositWithdrawRequestBrl.setType(TypeDepositWithdraw.WITHDRAW);
            depositWithdrawRequestBrl.setStatus(StatusDepositWithdrawBrl.WAITING_APPROVAL);
            depositWithdrawRequestBrl.setLastUpdated(new Date());
            depositWithdrawRequestBrl.setCreated(new Date());

            //link bank account info
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
            depositWithdrawRequestBrl.setFee(WITHDRAW_BRL_FEE);

        } catch (BankAccountException e) {
            throw new WithdrawBrlException("Ocorreu um erro no servidor",500);
        } catch (NoResultException e) {
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Conta Bancária não encontrada", CODE_ERROR_BANK_ACCOUNT_NOT_FOUND));
            throw new WithdrawBrlException("Intenção de saque inválida", errors, 400);
        }

        try {

            depositWithdrawRequestBrlRepository.save(depositWithdrawRequestBrl);

            //notify add brl balance topic
            Map<String,Object> addBrlBalanceJSON = new HashMap<String,Object>();
            addBrlBalanceJSON.put("email",depositWithdrawRequestBrl.getEmail());
            addBrlBalanceJSON.put("balance",depositWithdrawRequestBrl.getAmount().negate());
            kafkaTemplateMap.send(queueTopicAuthAddBrlBalance,addBrlBalanceJSON);

            //notify topic withdrawal change status to waiting approval
            kafkaTemplateWithdrawal.send(changeStatusToWaitingApprovalTopic,depositWithdrawRequestBrl);
        } catch (Exception e) {
            logger.error("withdrawal error",e);
            throw new WithdrawBrlException("Ocorreu um erro no servidor",500);
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
    public DepositWithdrawRequestBrl changeToProcessing(Integer idWithdraw) throws WithdrawBrlException {

        //try find withdrawl
        DepositWithdrawRequestBrl depositWithdrawRequestBrl = null;
        try {
            depositWithdrawRequestBrl = findById(idWithdraw);
        } catch (NoResultException e) {
            logger.error("withdrawl not found",e);
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Saque não encontrado",CODE_ERROR_DEPOSIT_WITHDRAWL_NOT_FOUND));
            throw new WithdrawBrlException("Saque não encontrado",errors,400);
        }

        preChangeStatusToProcessingValidation(depositWithdrawRequestBrl);

        try {

            depositWithdrawRequestBrl.setStatus(StatusDepositWithdrawBrl.PROCESSING);
            depositWithdrawRequestBrl.setLastUpdated(new Date());
            depositWithdrawRequestBrlRepository.save(depositWithdrawRequestBrl);

            //notify kafka change status processing event
            kafkaTemplateWithdrawal.send(changeStatusToProcessingTopic,depositWithdrawRequestBrl);

        } catch (Exception e) {
            logger.error("withdrawal error",e);
            throw new WithdrawBrlException("Ocorreu um erro no servidor",null,500);
        }

        return depositWithdrawRequestBrl;
    }

    @Override
    public DepositWithdrawRequestBrl changeToConfirmed(Integer idWithdraw) throws WithdrawBrlException {

        //try find withdrawl
        DepositWithdrawRequestBrl depositWithdrawRequestBrl = null;
        try {
            depositWithdrawRequestBrl = findById(idWithdraw);
        } catch (NoResultException e) {
            logger.error("withdrawl not found",e);
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Saque não encontrado",CODE_ERROR_DEPOSIT_WITHDRAWL_NOT_FOUND));
            throw new WithdrawBrlException("Saque não encontrado",errors,400);
        }

        preChangeStatusToConfirmedValidation(depositWithdrawRequestBrl);

        try {

            depositWithdrawRequestBrl.setStatus(StatusDepositWithdrawBrl.CONFIRMED);
            depositWithdrawRequestBrl.setLastUpdated(new Date());
            depositWithdrawRequestBrlRepository.save(depositWithdrawRequestBrl);

            //notify kafka change status confirmed event
            kafkaTemplateWithdrawal.send(changeStatusToConfirmedTopic,depositWithdrawRequestBrl);

        } catch (Exception e) {
            logger.error("withdrawal error",e);
            throw new WithdrawBrlException("Ocorreu um erro no servidor",null,500);
        }

        return depositWithdrawRequestBrl;
    }

    @Override
    public DepositWithdrawRequestBrl changeToDenied(Integer idWithdraw, String deniedReason) throws WithdrawBrlException {

        //try find withdrawl
        DepositWithdrawRequestBrl depositWithdrawRequestBrl = null;
        try {
            depositWithdrawRequestBrl = findById(idWithdraw);
            depositWithdrawRequestBrl.setDeniedReason(deniedReason);
        } catch (NoResultException e) {
            logger.error("withdrawl not found",e);
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Saque não encontrado",CODE_ERROR_DEPOSIT_WITHDRAWL_NOT_FOUND));
            throw new WithdrawBrlException("Saque não encontrado",errors,400);
        }

        preChangeStatusToDeniedValidation(depositWithdrawRequestBrl);

        try {

            depositWithdrawRequestBrl.setStatus(StatusDepositWithdrawBrl.DENIED);
            depositWithdrawRequestBrl.setLastUpdated(new Date());
            depositWithdrawRequestBrlRepository.save(depositWithdrawRequestBrl);

            //notify kafka change status denied event
            kafkaTemplateWithdrawal.send(changeStatusToDeniedTopic,depositWithdrawRequestBrl);

        } catch (Exception e) {
            logger.error("withdrawal error",e);
            throw new WithdrawBrlException("Ocorreu um erro no servidor",null,500);
        }

        return depositWithdrawRequestBrl;
    }

    @Override
    public DepositWithdrawRequestBrl changeToCancelled(Integer idWithdraw, String email) throws WithdrawBrlException {

        //try find withdrawl
        DepositWithdrawRequestBrl depositWithdrawRequestBrl = null;

        try {
            depositWithdrawRequestBrl = findByIdAndEmail(idWithdraw,email);
        } catch (NoResultException e) {
            logger.error("withdrawl not found",e);
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Saque não encontrado",CODE_ERROR_DEPOSIT_WITHDRAWL_NOT_FOUND));
            throw new WithdrawBrlException("Saque não encontrado",errors,400);
        }

        preChangeStatusToCancelledValidation(depositWithdrawRequestBrl);

        try {

            depositWithdrawRequestBrl.setStatus(StatusDepositWithdrawBrl.CANCELLED);
            depositWithdrawRequestBrl.setLastUpdated(new Date());
            depositWithdrawRequestBrlRepository.save(depositWithdrawRequestBrl);

            //notify kafka change status cancelled event
            kafkaTemplateWithdrawal.send(changeStatusToCancelledTopic,depositWithdrawRequestBrl);

        } catch (Exception e) {
            logger.error("withdrawal error",e);
            throw new WithdrawBrlException("Ocorreu um erro no servidor",null,500);
        }

        return depositWithdrawRequestBrl;
    }

    @Override
    public DepositWithdrawRequestBrl findById(Integer idWithdraw) throws WithdrawBrlException, NoResultException {
        Optional<DepositWithdrawRequestBrl> optional = depositWithdrawRequestBrlRepository.findById(idWithdraw);
        if(optional.isPresent())
            return optional.get();
        else
            throw new NoResultException("Withdrawal not found");
    }

    @Override
    public DepositWithdrawRequestBrl findByIdAndEmail(Integer idWithdraw, String email) throws WithdrawBrlException, NoResultException {
        DepositWithdrawRequestBrl depositWithdrawRequestBrl = depositWithdrawRequestBrlRepository.findByIddepositWithdrawRequestBrlAndEmail(idWithdraw,email);
        if(depositWithdrawRequestBrl == null)
            throw new NoResultException("Withdrawl not found");
        else
            return depositWithdrawRequestBrl;
    }

    @Override
    public void delete(Integer idWithdraw, String email) throws WithdrawBrlException {
        DepositWithdrawRequestBrl depositWithdrawRequestBrl = null;
        try {
            depositWithdrawRequestBrl = findByIdAndEmail(idWithdraw,email);
        } catch (NoResultException e) {
            logger.error("withdrawal not found",e);
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Saque não encontrado",CODE_ERROR_DEPOSIT_WITHDRAWL_NOT_FOUND));
            throw new WithdrawBrlException("Saque não encontrado",errors,400);
        }
        if(depositWithdrawRequestBrl.getStatus() == StatusDepositWithdrawBrl.CANCELLED || depositWithdrawRequestBrl.getStatus() == StatusDepositWithdrawBrl.DENIED)
            depositWithdrawRequestBrlRepository.delete(depositWithdrawRequestBrl);
        else throw new WithdrawBrlException("Não foi possível deletar a intenção de saque, a mesma não se encontra em status cancelado ou negado",500);
    }

    @Override
    public List<DepositWithdrawRequestBrl> list(String email, StatusDepositWithdrawBrl status) throws WithdrawBrlException {
        try {
            return depositWithdrawRequestBrlRepository.findAll(email,TypeDepositWithdraw.WITHDRAW,status);
        } catch (Exception e) {
            logger.error("withdrawal error",e);
            throw new WithdrawBrlException("Ocorreu um erro no servidor",500);
        }
    }

    @Override
    public BigDecimal sumAmountConfirmed(String email) throws WithdrawBrlException {
        try {
            return depositWithdrawRequestBrlRepository.sumAmountByEmailAndStatus(TypeDepositWithdraw.WITHDRAW,email,StatusDepositWithdrawBrl.CONFIRMED);
        } catch (Exception e) {
            logger.error("withdrawal error",e);
            throw new WithdrawBrlException("Ocorreu um erro no servidor",500);
        }
    }

    @Override
    public Long count(String email, StatusDepositWithdrawBrl status) throws WithdrawBrlException {
        try {
            return depositWithdrawRequestBrlRepository.countAllByEmailAndStatusAndType(email,status,TypeDepositWithdraw.WITHDRAW);
        } catch (Exception e) {
            logger.error("withdrawal error",e);
            throw new WithdrawBrlException("Ocorreu um erro no servidor",500);
        }
    }

    /**
     * Check if account is verified
     * @param email
     * @throws WithdrawBrlException
     */
    private void checkAccountVerification(String email) throws WithdrawBrlException {
        try {
            if (!userService.isAccountVerified(email)) {

                List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
                errors.add(new ServiceItemError("Sua conta não está verificada! Vá em configurações e faça a verificação", CODE_ERROR_ACCOUNT_NOT_VERIFIED));

                throw new WithdrawBrlException("Sua conta não está verificada! Vá em configurações e faça a verificação", errors, 400);
            }
        } catch (UserException e) {
            logger.error("user error", e);
            throw new WithdrawBrlException("Ocorreu um erro no servidor", null, 500);
        }
    }

    /**
     * Pre Add Withdrawal Validation
     *
     * @param depositWithdrawRequestBrl
     * @throws WithdrawBrlException
     */
    private void preAddValidation(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws WithdrawBrlException {

        List<ServiceItemError> errors = new ArrayList<ServiceItemError>();

        if (depositWithdrawRequestBrl.getEmail() == null || depositWithdrawRequestBrl.getEmail().equals(""))
            errors.add(new ServiceItemError("Email não pode ser vazio", CODE_ERROR_EMAIL_NOT_NULL));
        if (depositWithdrawRequestBrl.getAmount() == null || depositWithdrawRequestBrl.getAmount().compareTo(BigDecimal.ZERO) == 0)
            errors.add(new ServiceItemError("Valor não pode ser vazio ou zero", CODE_ERROR_AMOUNT_NOT_NULL));
        if (depositWithdrawRequestBrl.getIdbankAccount() == null)
            errors.add(new ServiceItemError("Conta Bancária é obrigatório", CODE_ERROR_BANK_ACCOUNT_NOT_NULL));
        if (depositWithdrawRequestBrl.getType() == null)
            errors.add(new ServiceItemError("Tipo de operação é obrigatório", CODE_ERROR_TYPE_NOT_NULL));
        if (depositWithdrawRequestBrl.getAmount() != null && depositWithdrawRequestBrl.getAmount().compareTo(BigDecimal.ZERO) != 0 && depositWithdrawRequestBrl.getAmount().compareTo(WITHDRAW_BRL_MAX_AMOUNT) > 0)
            errors.add(new ServiceItemError("Valor Limite Diário é de R$ 10.000,00", CODE_ERROR_MAX_AMOUNT));
        if (depositWithdrawRequestBrl.getAmount() != null && depositWithdrawRequestBrl.getAmount().compareTo(WITHDRAW_BRL_MIN_AMOUNT) < 0)
            errors.add(new ServiceItemError("Valor Mínimo para saque é de R$ 30,00", CODE_ERROR_MIN_AMOUNT));

        //first layer validation
        if (!errors.isEmpty())
            throw new WithdrawBrlException("Intenção de Saque Inválida", errors, 400);

        //check daily limit withdraw
        Date limit = DateUtils.addHoursFromNow(-24);
        List<DepositWithdrawRequestBrl> depositWithdrawRequestBrls = depositWithdrawRequestBrlRepository.findAllByEmailAndCreatedGreaterThanAndTypeAndStatusNotIn(depositWithdrawRequestBrl.getEmail(), limit, depositWithdrawRequestBrl.getType(), StatusDepositWithdrawBrl.CANCELLED);
        if (!depositWithdrawRequestBrls.isEmpty())
            errors.add(new ServiceItemError("Você já possuí uma intenção de saque cadastrada recentemente! Espere a mesma completar 24 horas ou cancele se ainda não estiver em status processamento!", CODE_ERROR_DAILY_LIMIT));

        //check if user has enough balance
        if (depositWithdrawRequestBrl.getEmail() != null && !depositWithdrawRequestBrl.getEmail().equals("") && depositWithdrawRequestBrl.getAmount() != null && depositWithdrawRequestBrl.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            try {
                BigDecimal currentBalanceBrl = userService.getAvailableBalanceBrl(depositWithdrawRequestBrl.getEmail());
                if (currentBalanceBrl.compareTo(depositWithdrawRequestBrl.getAmount()) < 0)
                    errors.add(new ServiceItemError("Você não possuí saldo disponível para este valor", CODE_ERROR_UNAVAILABLE_AMOUNT));
            } catch (UserException e) {
                logger.error("error on get user current balance", e);
                errors.add(new ServiceItemError("Ocorreu um erro no servidor", CODE_ERROR_GENERIC));
            }
        }

        if (!errors.isEmpty())
            throw new WithdrawBrlException("Intenção de Saque Inválida", errors, 400);
    }

    /**
     * Pre Change Status to Processing Validation
     * @param depositWithdrawRequestBrl
     * @throws WithdrawBrlException
     */
    private void preChangeStatusToProcessingValidation(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws WithdrawBrlException{
        if(depositWithdrawRequestBrl.getStatus() != StatusDepositWithdrawBrl.WAITING_APPROVAL) {
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Não foi possível atualizar o status, saque em status " + StatusDepositWithdrawBrl.getHumanName(depositWithdrawRequestBrl.getStatus()),CODE_ERROR_INVALID_STATUS));
            throw new WithdrawBrlException("Ocorreu um erro ao mudar o status do saque",errors,400);
        }
    }

    /**
     * Pre Change Status to Confirmed Validation
     * @param depositWithdrawRequestBrl
     * @throws WithdrawBrlException
     */
    private void preChangeStatusToConfirmedValidation(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws WithdrawBrlException{
        if(depositWithdrawRequestBrl.getStatus() != StatusDepositWithdrawBrl.PROCESSING) {
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Não foi possível atualizar o status, saque em status " + StatusDepositWithdrawBrl.getHumanName(depositWithdrawRequestBrl.getStatus()),CODE_ERROR_INVALID_STATUS));
            throw new WithdrawBrlException("Ocorreu um erro ao mudar o status do saque",errors,400);
        }
    }

    /**
     * Pre Change Status to Denied Validation
     * @param depositWithdrawRequestBrl
     * @throws WithdrawBrlException
     */
    private void preChangeStatusToDeniedValidation(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws WithdrawBrlException {

        List<ServiceItemError> errors = new ArrayList<ServiceItemError>();

        if(depositWithdrawRequestBrl.getStatus() != StatusDepositWithdrawBrl.PROCESSING)
            errors.add(new ServiceItemError("Não foi possível atualizar o status, saque em status " + StatusDepositWithdrawBrl.getHumanName(depositWithdrawRequestBrl.getStatus()),CODE_ERROR_INVALID_STATUS));

        if(depositWithdrawRequestBrl.getDeniedReason() == null || depositWithdrawRequestBrl.getDeniedReason().equals(""))
            errors.add(new ServiceItemError("Motivo de recusa obrigatório",CODE_ERROR_DENIED_REASON_NOT_NULL));

        if(!errors.isEmpty())
            throw new WithdrawBrlException("Intenção de Saque Inválida",errors,400);
    }

    /**
     * Pre Change Status to Cancelled Validation
     * @param depositWithdrawRequestBrl
     * @throws WithdrawBrlException
     */
    private void preChangeStatusToCancelledValidation(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws WithdrawBrlException {
        if(depositWithdrawRequestBrl.getStatus() != StatusDepositWithdrawBrl.WAITING_APPROVAL) {
            List<ServiceItemError> errors = new ArrayList<ServiceItemError>();
            errors.add(new ServiceItemError("Não foi possível atualizar o status, saque em status " + StatusDepositWithdrawBrl.getHumanName(depositWithdrawRequestBrl.getStatus()),CODE_ERROR_INVALID_STATUS));
            throw new WithdrawBrlException("Ocorreu um erro ao mudar o status do saque",errors,400);
        }
    }
}
