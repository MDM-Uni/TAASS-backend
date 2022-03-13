package com.petlife.negoziomicroservizio.repo;

import com.petlife.negoziomicroservizio.model.Prodotto;
import org.springframework.data.repository.CrudRepository;

public interface ProdottoRepository extends CrudRepository<Prodotto, Long> {
}
