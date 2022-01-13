package com.petlife.ospedalemicroservizio.repositories;

import com.petlife.ospedalemicroservizio.models.Struttura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StrutturaRepository extends JpaRepository<Struttura, Long> {
}
