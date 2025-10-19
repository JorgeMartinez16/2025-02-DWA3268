package com.parcial.dos.parcialdos.account.dto;

import java.math.BigDecimal;

public class AccountResponseDTO {
    private Long id;
    private String numeroCuenta;
    private String dueno;
    private BigDecimal balanceActual;
    private Boolean active;
    
    public AccountResponseDTO() {}
    
    public AccountResponseDTO(Long id, String numeroCuenta, String dueno, BigDecimal balanceActual, Boolean active) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.dueno = dueno;
        this.balanceActual = balanceActual;
        this.active = active;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNumeroCuenta() {
        return numeroCuenta;
    }
    
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
    
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
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
}