package com.mali.crypfy.moneymanager.core.impl;

import com.mali.crypfy.moneymanager.core.BankAccountService;
import com.mali.crypfy.moneymanager.core.exception.BankAccountException;
import com.mali.crypfy.moneymanager.core.exception.NoResultException;
import com.mali.crypfy.moneymanager.core.validator.ServiceItemError;
import com.mali.crypfy.moneymanager.integration.auth.User;
import com.mali.crypfy.moneymanager.integration.auth.UserService;
import com.mali.crypfy.moneymanager.integration.auth.exception.UserException;
import com.mali.crypfy.moneymanager.persistence.entity.Bank;
import com.mali.crypfy.moneymanager.persistence.entity.BankAccount;
import com.mali.crypfy.moneymanager.persistence.repository.BankAccountRepository;
import com.mali.crypfy.moneymanager.persistence.repository.BankRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Bank Account Service Implementation
 */
@Service
public class BankAccountServiceImpl implements BankAccountService {

    final static Logger logger = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    public static final int CODE_ERROR_EMAIL_NOT_NULL = 1000;
    public static final int CODE_ERROR_BANK_NOT_NULL = 1001;
    public static final int CODE_ERROR_ACCOUNT_NUMBER_NOT_NULL = 1002;
    public static final int CODE_ERROR_AGENCY_NOT_NULL = 1003;
    public static final int CODE_ERROR_TYPE_BANK_ACCOUNT_NOT_NULL = 1004;
    public static final int CODE_ERROR_ACCOUNT_NUMBER_DIGIT_NOT_NULL = 1005;

    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private UserService userService;

    @Override
    public BankAccount add(BankAccount bankAccount) throws BankAccountException {

        User user = null;

        //first layer validation
        if(bankAccount.getEmail() != null && !bankAccount.getEmail().equals("")) {
            try {
                //check if account is verified
                boolean isAccVerified = userService.isAccountVerified(bankAccount.getEmail());
                if(!isAccVerified)
                    throw new BankAccountException("Sua conta não está verificada! Por favor faça a verificação",null,400);
                //get user info
                user = userService.getInfo(bankAccount.getEmail());
            } catch (UserException e) {
                logger.error("error on check if acc is verified");
                throw new BankAccountException("ocorreu um erro no servidor",null,500);
            }
        }

        //second layer validation
        List<ServiceItemError> errors = validate(bankAccount);
        if (!errors.isEmpty())
            throw new BankAccountException("Ocorreu um erro ao cadastrar conta bancária", errors, 400);
        BankAccount duplicatedBankAccount = bankAccountRepository.findByAgencyAndAccountNumberAndAccountNumberDigitAndTypeAndIdbank(bankAccount.getAgency(), bankAccount.getAccountNumber(),bankAccount.getAccountNumberDigit(), bankAccount.getType(), bankAccount.getIdbank());
        if (duplicatedBankAccount != null)
            throw new BankAccountException("Já existe uma conta bancária cadastrada com esses dados", null, 400);

        Bank bank = bankRepository.findById(bankAccount.getIdbank()).orElse(null);

        if (bank == null)
            throw new BankAccountException("Banco não encontrado", null, 400);

        bankAccount.setCreated(new Date());
        bankAccount.setBank(bank);
        bankAccount.setDocumentType(user.getDocumentType());
        bankAccount.setDocumentNumber(user.getDocumentNumber());

        try {
            return bankAccountRepository.save(bankAccount);
        } catch (Exception e) {
            logger.error("error on add bank account", e);
            throw new BankAccountException("Ocorreu um erro no servidor", null, 500);
        }
    }

    @Override
    @Transactional
    public BankAccount update(BankAccount bankAccount) throws BankAccountException {
        List<ServiceItemError> errors = validate(bankAccount);

        if (!errors.isEmpty())
            throw new BankAccountException("Ocorreu um erro ao cadastrar conta bancária", errors, 400);
        BankAccount duplicatedBankAccount = bankAccountRepository.findByAgencyAndAccountNumberAndAccountNumberDigitAndTypeAndIdbank(bankAccount.getAgency(), bankAccount.getAccountNumber(),bankAccount.getAccountNumberDigit(),bankAccount.getType(), bankAccount.getIdbank());
        if (duplicatedBankAccount != null) {
            if (!duplicatedBankAccount.getIdbankAccount().equals(bankAccount.getIdbankAccount()))
                throw new BankAccountException("Já existe uma conta bancária cadastrada com esses dados", null, 400);
        }

        Bank bank = bankRepository.findById(bankAccount.getIdbank()).orElse(null);

        if (bank == null)
            throw new BankAccountException("Banco não encontrado", null, 400);

        BankAccount updatedBankAccount = bankAccountRepository.findByEmailAndIdbankAccount(bankAccount.getEmail(),bankAccount.getIdbankAccount());

        if (updatedBankAccount == null)
            throw new BankAccountException("Conta bancária não encontrada", null, 400);

        updatedBankAccount.setAccountNumber(bankAccount.getAccountNumber());
        updatedBankAccount.setAccountNumberDigit(bankAccount.getAccountNumberDigit());
        updatedBankAccount.setAgency(bankAccount.getAgency());
        updatedBankAccount.setBank(bank);
        updatedBankAccount.setType(bankAccount.getType());
        updatedBankAccount.setEmail(bankAccount.getEmail());

        try {
            bankAccountRepository.save(updatedBankAccount);
            updatedBankAccount.setBank(bank);
            return bankAccountRepository.findById(bankAccount.getAccountNumber()).orElse(null);
        } catch (Exception e) {
            logger.error("error on add bank account", e);
            throw new BankAccountException("Ocorreu um erro no servidor", null, 500);
        }
    }

    @Override
    public void delete(String email, Integer idbankAccount) throws BankAccountException {
        BankAccount bankAccount = null;
        try {
            bankAccount = bankAccountRepository.findByEmailAndIdbankAccount(email, idbankAccount);
        } catch (Exception e) {
            logger.error("error on delete bank account", e);
            throw new BankAccountException("Não foi possível excluir a conta bancária", null, 500);
        }

        if (bankAccount == null)
            throw new BankAccountException("Conta bancária não encontrada", null, 400);

        try {
            bankAccountRepository.delete(bankAccount);
        } catch (Exception e) {
            logger.error("error on delete bank account", e);
            throw new BankAccountException("Não foi possível excluir a conta bancária", null, 500);
        }
    }

    @Override
    public List<BankAccount> list(String email) throws BankAccountException {
        try {
            return this.bankAccountRepository.findByEmailOrderByCreatedDesc(email);
        } catch (Exception e) {
            logger.error("error on list bank accounts",e);
            throw new BankAccountException("Ocorreu um erro no servidor",null,500);
        }

    }

    @Override
    public BankAccount findByIdAndEmail(String email, Integer id) throws BankAccountException {
        BankAccount bankAccount = this.bankAccountRepository.findByEmailAndIdbankAccount(email,id);
        if(bankAccount == null)
            throw new BankAccountException("Conta Bancária não encontrada",null,400);
        return bankAccount;
    }

    @Override
    public BankAccount findById(Integer id) throws BankAccountException,NoResultException {
        Optional<BankAccount> optional = bankAccountRepository.findById(id);
        if(optional.isPresent())
            return optional.get();
        else throw new NoResultException("No bank account found");

    }

    private List<ServiceItemError> validate(BankAccount bankAccount) {
        List<ServiceItemError> errors = new ArrayList<ServiceItemError>();

        if (bankAccount.getEmail() == null || bankAccount.getEmail().equals(""))
            errors.add(new ServiceItemError("Email é obrigatório", CODE_ERROR_EMAIL_NOT_NULL));
        if (bankAccount.getIdbank() == null || bankAccount.getIdbank() == 0)
            errors.add(new ServiceItemError("Banco é obrigatório", CODE_ERROR_BANK_NOT_NULL));
        if (bankAccount.getAccountNumber() == null || bankAccount.getAccountNumber() == 0)
            errors.add(new ServiceItemError("Conta Bancária é obrigatório", CODE_ERROR_ACCOUNT_NUMBER_NOT_NULL));
        if (bankAccount.getAgency() == null || bankAccount.getAgency() == 0)
            errors.add(new ServiceItemError("Agência é obrigatório", CODE_ERROR_AGENCY_NOT_NULL));
        if (bankAccount.getType() == null)
            errors.add(new ServiceItemError("Tipo de conta é obrigatório", CODE_ERROR_TYPE_BANK_ACCOUNT_NOT_NULL));
        if (bankAccount.getType() == null)
            errors.add(new ServiceItemError("Tipo de conta é obrigatório", CODE_ERROR_TYPE_BANK_ACCOUNT_NOT_NULL));
        if (bankAccount.getAccountNumberDigit() == null)
            errors.add(new ServiceItemError("Digito da conta é obrigatório", CODE_ERROR_ACCOUNT_NUMBER_DIGIT_NOT_NULL));

        return errors;
    }
}
