package com.petlife.utentemicroservizio.repositories;

import com.petlife.utentemicroservizio.models.Animale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimaleRepository extends JpaRepository<Animale,Long> {
}
