package com.petlife.utentemicroservizio.repositories;

import com.petlife.utentemicroservizio.models.EventoPersonalizzato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoPersonalizzatoRepository extends JpaRepository<EventoPersonalizzato, Long> {
}
