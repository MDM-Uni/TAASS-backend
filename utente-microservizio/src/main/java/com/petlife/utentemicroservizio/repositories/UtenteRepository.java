package com.petlife.utentemicroservizio.repositories;

import com.petlife.utentemicroservizio.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente,Long> {
}
