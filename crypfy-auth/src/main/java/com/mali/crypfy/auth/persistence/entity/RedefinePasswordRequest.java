package com.mali.crypfy.auth.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class RedefinePasswordRequest implements Serializable {

    private Integer idredefinePasswordRequest;
    private String email;
    private String token;
    private Date created;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idredefine_password_request", unique = true, nullable = false)
    public Integer getIdredefinePasswordRequest() {
        return idredefinePasswordRequest;
    }

    public void setIdredefinePasswordRequest(Integer idredefinePasswordRequest) {
        this.idredefinePasswordRequest = idredefinePasswordRequest;
    }

    @Column(name = "email",length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "token",length = 500)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
