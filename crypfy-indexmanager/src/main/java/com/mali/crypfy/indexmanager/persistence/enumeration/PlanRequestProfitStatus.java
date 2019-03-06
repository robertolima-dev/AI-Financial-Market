package com.mali.crypfy.indexmanager.persistence.enumeration;

public enum PlanRequestProfitStatus {
    WAITING,PROCESSING,DONE,FAILED,CANCELLED;

    public static String getHumanName(PlanRequestProfitStatus status) {
        if(status == WAITING)
            return "Aguardando Processamento";
        if(status == PROCESSING)
            return "Processando";
        if(status == DONE)
            return  "Confirmado";
        if(status == FAILED)
            return "Falha";
        if(status == CANCELLED)
            return "Cancelado";
        return null;
    }
}
