package com.itsqmet.repository;

import com.itsqmet.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // BUSCAR CUENTA POR USERNAME (PARA LOGIN)
    Optional<Account> findByUsername(String username);

    // BUSCAR CUENTA POR USERNAME Y PASSWORD (LOGIN ALTERNATIVO)
    Account findByUsernameAndPassword(String username, String password);
}