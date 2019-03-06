package com.mali.crypfy.gateway.services.moneymanager.json;

public class ChangeDepositStatusToWaitingApprovalJSON {

    public ChangeDepositStatusToWaitingApprovalJSON(String photo) {
        this.photo = photo;
    }

    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
