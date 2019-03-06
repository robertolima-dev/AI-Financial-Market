package com.mali.crypfy.auth.persistence.entity;

import com.mali.crypfy.auth.persistence.enumeration.DocumentType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class User implements Serializable {

    private String email;
    private String name;
    private String password;
    private Date created;
    private Date lastLogin;
    private String confirmPassword;


    @Id
    @Column(name = "email",length = 100)
    @Size(max = 100,message = "Email não pode ser maior que 100 caracteres")
    @NotNull(message = "Email não pode ser vazio")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "name",length = 200)
    @Size(max = 200, message = "Nome não pode ser maior que 200 caracteres")
    @NotNull(message = "Nome não pode ser vazio")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "password",length = 200)
    @Size(max = 200, message = "Senha não pode ser maior que 200 caracteres")
    @NotNull(message = "Senha não pode ser vazio")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Transient
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
