package com.petlife.utentemicroservizio.repositories;

import com.petlife.utentemicroservizio.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UtenteRepository extends CrudRepository<Utente,Long> {
    Optional<Utente> findByEmail(String email);
}


