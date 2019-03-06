package com.mali.crypfy.moneymanager.core.impl;

import com.mali.crypfy.moneymanager.core.BankService;
import com.mali.crypfy.moneymanager.core.exception.BankException;
import com.mali.crypfy.moneymanager.persistence.entity.Bank;
import com.mali.crypfy.moneymanager.persistence.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankRepository bankRepository;

    @Override
    public List<Bank> list() throws BankException {
        try {
            return bankRepository.findAll();
        } catch (Exception e) {
            throw new BankException("Ocorreu um erro no servidor",null,500);
        }
    }

    @Override
    public Bank findById(Integer idBank) throws BankException, NoResultException {
        Optional<Bank> optional = bankRepository.findById(idBank);
        if(optional.isPresent())
            return optional.get();
        else
            throw new NoResultException();
    }
}
