package com.parcial.dos.parcialdos.account.dto;

import java.math.BigDecimal;

public class AccountOwnerBalanceDTO {
    private String dueno;
    private BigDecimal balanceActual;

    public AccountOwnerBalanceDTO() {}

    public AccountOwnerBalanceDTO(String dueno, BigDecimal balanceActual) {
        this.dueno = dueno;
        this.balanceActual = balanceActual;
    }
    
    // Getters y Setters
    public String getDueno() {
        return dueno;
    }
    
    public void setDueno(String dueno) {
        this.dueno = dueno;
    }
    
    public BigDecimal getBalanceActual() {
        return balanceActual;
    }
    
    public void setBalanceActual(BigDecimal balanceActual) {
        this.balanceActual = balanceActual;
    }
}
