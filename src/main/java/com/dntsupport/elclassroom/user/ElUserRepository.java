package com.dntsupport.elclassroom.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface ElUserRepository extends JpaRepository<ElUser, String> {

    Optional<ElUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE ElUser el SET el.isEnabled = TRUE WHERE el.email = ?1")
    int enableUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE ElUser el SET el.isEnabled = FALSE WHERE el.email = ?1")
    int disableUser(String email);
}
