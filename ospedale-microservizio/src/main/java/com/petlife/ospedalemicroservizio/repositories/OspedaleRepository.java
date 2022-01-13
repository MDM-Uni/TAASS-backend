package com.petlife.ospedalemicroservizio.repositories;

import com.petlife.ospedalemicroservizio.models.Ospedale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OspedaleRepository extends JpaRepository<Ospedale, Long> {
}
