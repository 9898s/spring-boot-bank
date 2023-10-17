package com.example.bank.domain.account;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  // @Query("SELECT ac from Account ac join fetch ac.user u where ac.number = :number")
  Optional<Account> findByNumber(Long number);

  List<Account> findByUser_Id(Long id);
}
