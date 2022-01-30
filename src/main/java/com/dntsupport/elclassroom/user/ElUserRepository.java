package com.dntsupport.elclassroom.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface ElUserRepository extends JpaRepository<ElUser, String> {

    Optional<ElUser> findByEmail(String email);
}
