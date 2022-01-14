package com.petlife.utentemicroservizio.repositories;

import com.petlife.utentemicroservizio.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente,Long> {
    Optional<Utente> findById(Long id);

    Optional<Utente> findByEmail(String email);

    Boolean existsByEmail(String email);
}
