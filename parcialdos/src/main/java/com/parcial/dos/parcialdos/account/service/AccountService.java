package com.parcial.dos.parcialdos.account.service;

import com.parcial.dos.parcialdos.account.dto.AccountRequestDTO;
import com.parcial.dos.parcialdos.account.dto.AccountResponseDTO;
import com.parcial.dos.parcialdos.account.dto.AccountOwnerBalanceDTO;
import com.parcial.dos.parcialdos.account.entity.Account;
import com.parcial.dos.parcialdos.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService implements IAccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Override
    public AccountResponseDTO create(AccountRequestDTO request) {
        Account account = new Account(
            request.getNumeroCuenta(),
            request.getDueno(),
            request.getBalanceActual(),
            true
        );
        
        Account savedAccount = accountRepository.save(account);
        return mapToResponseDTO(savedAccount);
    }
    
    @Override
    public List<AccountResponseDTO> getAll() {
        return accountRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public AccountResponseDTO getById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            return mapToResponseDTO(account.get());
        }
        throw new RuntimeException("Cuenta no encontrada");
    }
    
    @Override
    public String update(Long id, AccountRequestDTO request) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            BigDecimal balanceAnterior = account.getBalance();
            account.setBalance(request.getBalanceActual());
            accountRepository.save(account);
            
            return String.format("La cuenta %s fue actualizada: balanceAnterior=%.2f, balanceActual=%.2f",
                    account.getAccountNumber(),
                    balanceAnterior,
                    request.getBalanceActual());
        }
        return "Cuenta no encontrada";
    }
    
    @Override
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }
    
    @Override
    public AccountOwnerBalanceDTO findByNumeroCuenta(String numeroCuenta) {
        Optional<Account> account = accountRepository.findByAccountNumber(numeroCuenta);
        if (account.isPresent()) {
            Account acc = account.get();
            return new AccountOwnerBalanceDTO(acc.getOwnerName(), acc.getBalance());
        }
        throw new RuntimeException("Cuenta no encontrada");
    }
    
    private AccountResponseDTO mapToResponseDTO(Account account) {
        return new AccountResponseDTO(
            account.getId(),
            account.getAccountNumber(),
            account.getOwnerName(),
            account.getBalance(),
            account.getActive()
        );
    }
}