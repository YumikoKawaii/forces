package com.yumiko.forces.repositories;

import com.yumiko.forces.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepo extends JpaRepository<Account, String> {

    @Query(value = "select case when exists(select * from accounts a where a.email = email) then true else false end as is_exist", nativeQuery = true)
    int existsByEmail(String email);

    @Query(value = "select case when exists(select * from accounts a where a.email = email and a.password = password) then true else false end as is_exist", nativeQuery = true)
    int existsByEmailAndPassword(String email, String password);

    @Query(value = "select * from accounts a where a.email = email", nativeQuery = true)
    Account findByEmail(String email);
    boolean existsById(String id);

}
