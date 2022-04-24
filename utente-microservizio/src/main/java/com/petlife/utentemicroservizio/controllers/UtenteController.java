package com.petlife.utentemicroservizio.controllers;

import com.petlife.utentemicroservizio.models.Animale;
import com.petlife.utentemicroservizio.models.Utente;
import com.petlife.utentemicroservizio.repositories.AnimaleRepository;
import com.petlife.utentemicroservizio.repositories.UtenteRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@SessionAttributes("id")
@RestController
@Slf4j
public class UtenteController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private AnimaleRepository animaleRepository;

    @PostMapping(value = "/user/create")
    public Utente postUser(@RequestBody Utente utente) {
        return utenteRepository.save(new Utente(utente.getNome(),utente.getEmail(),utente.getAnimali()));
    }

    @GetMapping("/users")
    public List<Utente> getAllUsers() {
        log.info("chiamo getAllUsers");
        List<Utente> users = new ArrayList<>();
        utenteRepository.findAll().forEach(users::add);
        log.info("restituisco tutti gli utenti");
        return users;
    }

    @GetMapping("/animals/{id}")
    public ResponseEntity<List<Animale>> getAnimalsUser(@PathVariable("id") long id) {
        log.info("chiamo getAnimalsUser");
        Optional<Utente> user = utenteRepository.findById(id);;
        return user.map(utente -> new ResponseEntity<>(utente.getAnimali(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value="/addAnimal/{id}", method = RequestMethod.POST)
    public ResponseEntity<Utente> addAnimal(@PathVariable("id") long id, @RequestBody Animale animale) {
        Optional<Utente> user = utenteRepository.findById(id);
        if (user.isPresent()) {
            Utente _utente = user.get();
            _utente.addAnimale(animale);
            animaleRepository.save(animale);
            return new ResponseEntity<>(utenteRepository.save(_utente), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/removeAnimal/{id}/{idA}" , method = RequestMethod.DELETE)
    public ResponseEntity<String> removeAnimal(@PathVariable("id") long id, @PathVariable("idA") long idA) {
        Optional<Utente> user = utenteRepository.findById(id);
        Optional<Animale> animal = animaleRepository.findById(idA);
        if (user.isPresent() && animal.isPresent()) {
            Utente _utente = user.get();
            _utente.removeAnimale(animal.get());
            animaleRepository.delete(animal.get());
            utenteRepository.save(_utente);
            try {
                rabbitTemplate.convertAndSend(RabbitMQCOnfig.EXCHANGE, RabbitMQCOnfig.ROUTINGKEY_A, animal.get().getId());
            }
            catch (AmqpException e) {
                log.error("Errore nell'invio dell'id dell'animale eliminato nella coda");
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/updateAnimal/{id}/{idA}" , method = RequestMethod.PUT)
    public ResponseEntity<Animale> updateAnimal(@PathVariable("id") long id, @PathVariable("idA") Long idA, @RequestBody Animale animale) {
        Optional<Utente> user = utenteRepository.findById(id);
        Optional<Animale> animal = animaleRepository.findById(idA);
        if (user.isPresent() && animal.isPresent()) {
            Utente _utente = user.get();
            Animale _animale = animal.get();
            if(_utente.getAnimali().contains(_animale)){
                _animale.setNome(animale.getNome());
                _animale.setDataDiNascita(animale.getDataDiNascita());
                _animale.setPatologie(animale.getPatologie());
                _animale.setPeso(animale.getPeso());
                _animale.setPeloLungo(animale.isPeloLungo());
                _animale.setRazza(animale.getRazza());
                return new ResponseEntity<>(animaleRepository.save(_animale),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "user/{email}/{nome}")
    public Utente getUser(@PathVariable String email, @PathVariable String nome) {
        log.info("Ricevuta richiesta di ricerca/salvataggio utente con email: " + email + " e nome: " + nome);
        Optional<Utente> user = utenteRepository.findByEmail(email);
        List<Animale> animali = new ArrayList<Animale>();
        if(user.isPresent()) {
            Utente utente = user.get();
            log.info("Utente trovato");
            return utente;
        } else {
            Utente utente = utenteRepository.save(new Utente(nome, email, animali));
            log.info("Utente salvato");
            return utente;
        }
    }

}
