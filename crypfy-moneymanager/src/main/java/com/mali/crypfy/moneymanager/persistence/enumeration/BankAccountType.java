package com.mali.crypfy.moneymanager.persistence.enumeration;

/**
 * Enum Responsible for manage Type of Bank Accounts (CONTA CORRENTE, CONTA POUPANCA)
 */
public enum BankAccountType {

    CONTA_CORRENTE,CONTA_POUPANCA;

    /**
     * Get Account Type Humam Name as String
     * @param type
     * @return
     */
    public static String getHumanName(BankAccountType type) {
        if(type == CONTA_CORRENTE)
            return "CONTA CORRENTE";
        else
            return "CONTA POUPANÇA";
    }
}
