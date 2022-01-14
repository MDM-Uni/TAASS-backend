package com.petlife.ospedalemicroservizio.repositories;

import com.petlife.ospedalemicroservizio.models.Visita;
import org.hibernate.boot.JaccPermissionDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {
}
