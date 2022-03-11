package com.petlife.utentemicroservizio.controllers;

import com.petlife.utentemicroservizio.models.Animale;
import com.petlife.utentemicroservizio.models.Utente;
import com.petlife.utentemicroservizio.repositories.AnimaleRepository;
import com.petlife.utentemicroservizio.repositories.UtenteRepository;
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
        List<Utente> users = new ArrayList<>();
        utenteRepository.findAll().forEach(users::add);
        return users;
    }

    @GetMapping("/animals")
    public ResponseEntity<List<Animale>> getAnimalsUser(HttpSession session) {
        Optional<Utente> user = utenteRepository.findById((Long) session.getAttribute("id"));
        return user.map(utente -> new ResponseEntity<>(utente.getAnimali(), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value="/addAnimal", method = RequestMethod.POST)
    public ResponseEntity<Utente> addAnimal(HttpSession session, @RequestBody Animale animale) {
        Optional<Utente> user = utenteRepository.findById((Long) session.getAttribute("id"));
        if (user.isPresent()) {
            Utente _utente = user.get();
            _utente.addAnimale(animale);
            animaleRepository.save(animale);
            return new ResponseEntity<>(utenteRepository.save(_utente), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/removeAnimal/{idA}" , method = RequestMethod.DELETE)
    public ResponseEntity<String> removeAnimal(HttpSession session, @PathVariable("idA") long idA) {
        Optional<Utente> user = utenteRepository.findById((Long) session.getAttribute("id"));
        Optional<Animale> animal = animaleRepository.findById(idA);
        if (user.isPresent() && animal.isPresent()) {
            Utente _utente = user.get();
            _utente.removeAnimale(animal.get());
            animaleRepository.delete(animal.get());
            utenteRepository.save(_utente);
            rabbitTemplate.convertAndSend(RabbitMQCOnfig.EXCHANGE,RabbitMQCOnfig.ROUTINGKEY_A,animal.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/updateAnimal/{idA}" , method = RequestMethod.PUT)
    public ResponseEntity<Animale> updateAnimal(HttpSession session, @PathVariable("idA") Long idA, @RequestBody Animale animale) {
        Optional<Utente> user = utenteRepository.findById((Long) session.getAttribute("id"));
        Optional<Animale> animal = animaleRepository.findById(idA);
        if (user.isPresent() && animal.isPresent()) {
            Utente _utente = user.get();
            Animale _animale = animal.get();
            if(_utente.getAnimali().contains(_animale)){
                _animale.setNome(animale.getNome());
                _animale.setDataDiNascita(animale.getDataDiNascita());
                _animale.setPatologie(animale.getPatologie());
                _animale.setPeso(animale.getPeso());
                _animale.setPeloLungo(animale.getPeloLungo());
                _animale.setRazza(animale.getRazza());
                return new ResponseEntity<>(animaleRepository.save(_animale),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "user/{email}/{nome}")
    public Utente getUser(HttpSession session, @PathVariable String email, @PathVariable String nome) {
        Optional<Utente> user = utenteRepository.findByEmail(email);
        List<Animale> animali = new ArrayList<Animale>();
        if(user.isPresent()) {
            Utente utente = user.get();
            session.setAttribute("id",utente.getId());
            return utente;
        } else {
            Utente utente = utenteRepository.save(new Utente(nome, email, animali));
            session.setAttribute("id",utente.getId());
            return utente;
        }
    }

}
