package com.petlife.negoziomicroservizio.controller.utils;

import com.petlife.negoziomicroservizio.model.Carrello;
import com.petlife.negoziomicroservizio.model.Utente;
import com.petlife.negoziomicroservizio.repo.CarrelloRepository;
import com.petlife.negoziomicroservizio.repo.UtenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component
public class UtenteChecker {
    @Autowired
    UtenteRepository utenteRepository;
    @Autowired
    CarrelloRepository carrelloRepository;

    Logger logger = LoggerFactory.getLogger(UtenteChecker.class);

    /**
     * Restituisce un optional Utente.
     * Se l'utente non è presente nel database del microservizio negozio, si effettua una chiamata al microservizio utente,
     * che verifica se l'utente è presente. Se non lo è il metodo solleva un eccezione, altrimenti restituisce l'utente.
     * Dopo aver ricevuto l'utente, il metodo aggiunge l'utente al database e restituisce true.
     * Inserisce in automatico il carrello.
     * @param id dell'utente
     * @return optional utente
     */
    public Optional<Utente> getUtente(long id) {
        Optional<Utente> utenteOpt = utenteRepository.findById(id);
        if (utenteOpt.isPresent())
            return utenteOpt;

        ResponseEntity<Boolean> response = WebClient.builder().build()
                .get()
                .uri("http://host.docker.internal:8080/utente/user/{id}",id)
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                })
                .retrieve()
                .toEntity(Boolean.class)
                .retry(3)
                .block();
        if (response == null || response.getStatusCode() != HttpStatus.OK || response.getBody() == null)
            throw new RuntimeException("Errore nella richiesta");

        boolean utentePresente = response.getBody();

        if (utentePresente) {
            Utente utente = new Utente();
            utente.setId(id);
            utente.setCarrello(carrelloRepository.save(new Carrello()));
            utenteRepository.save(utente);
            logger.info("Utente id=" + id + " trovato nel microservizio utente. Salvato sul database negozio");
            return Optional.of(utente);
        } else
            logger.info("Utente id=" + id + " non trovato");
            return Optional.empty();
    }
}
