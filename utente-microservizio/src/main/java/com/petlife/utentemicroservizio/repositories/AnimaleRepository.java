package com.petlife.utentemicroservizio.repositories;

import com.petlife.utentemicroservizio.models.Animale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimaleRepository extends JpaRepository<Animale,Long> {
}
