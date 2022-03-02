package com.petlife.utentemicroservizio.repositories;

import com.petlife.utentemicroservizio.models.Animale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface AnimaleRepository extends CrudRepository<Animale,Long> {
}
