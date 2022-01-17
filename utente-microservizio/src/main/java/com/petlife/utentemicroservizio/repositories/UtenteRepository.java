package com.petlife.utentemicroservizio.repositories;

import com.petlife.utentemicroservizio.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface UtenteRepository extends CrudRepository<Utente,Long> {
    Utente findByEmail(String email);
}


