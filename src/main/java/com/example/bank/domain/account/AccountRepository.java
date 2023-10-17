package com.example.bank.domain.account;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByNumber(Long number);

  List<Account> findByUser_Id(Long id);
}
