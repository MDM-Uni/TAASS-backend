package com.petlife.ospedalemicroservizio.repositories;

import com.petlife.ospedalemicroservizio.models.Ospedale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OspedaleRepository extends JpaRepository<Ospedale, Long> {
}
