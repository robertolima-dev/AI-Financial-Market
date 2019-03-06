package com.mali.crypfy.moneymanager.persistence.enumeration;

public enum StatusDepositWithdrawBrl {
    CONFIRMED,WAITING_APPROVAL,DENIED,WAITING_PHOTO_UPLOAD,PROCESSING,CANCELLED;


    public static String getHumanName(StatusDepositWithdrawBrl status) {
        switch (status) {
            case DENIED:
                return "Negado";
            case CANCELLED:
                return "Cancelado";
            case CONFIRMED:
                return "Confirmado";
            case PROCESSING:
                return "Processando";
            case WAITING_APPROVAL:
                return "Aguardando Aprovação";
            case WAITING_PHOTO_UPLOAD:
                return "Aguardando Upload Depósito";
            default:
                return "";
        }
    }
}
