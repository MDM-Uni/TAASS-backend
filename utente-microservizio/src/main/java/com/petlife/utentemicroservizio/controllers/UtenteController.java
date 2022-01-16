package com.petlife.utentemicroservizio.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import com.petlife.utentemicroservizio.models.Animale;
import com.petlife.utentemicroservizio.models.Utente;
import com.petlife.utentemicroservizio.repositories.AnimaleRepository;
import com.petlife.utentemicroservizio.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
public class UtenteController {

    @Autowired(required = false)
    private UtenteRepository UtenteRepository;

    @Autowired(required = false)
    private AnimaleRepository AnimaleRepository;

    @PostMapping(value = "/user/create")
    public Utente postCustomer(@RequestBody Utente utente) {
        return UtenteRepository.save(new Utente(utente.getNome(),utente.getEmail(),utente.getAnimali()));
    }

    @GetMapping("/users")
    public List<Utente> getAllUsers() {
        List<Utente> users = new ArrayList<>();
        UtenteRepository.findAll().forEach(users::add);
        return users;
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<String> deleteUser(@RequestBody Utente utente) {
        UtenteRepository.delete(utente);
        return new ResponseEntity<>("User" + utente.getNome() + "è stato eliminato correttamente", HttpStatus.OK);
    }

    @PutMapping("/addAnimal/{id}")
    public ResponseEntity<Utente> addAnimal(@PathVariable("id") long id, @RequestBody Animale animale) {
        Optional<Utente> user = UtenteRepository.findById(id);
        if (user.isPresent()) {
            Utente _utente = user.get();
            _utente.addAnimale(animale);
            return new ResponseEntity<>(UtenteRepository.save(_utente), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/removeAnimal/{id}")
    public ResponseEntity<Utente> removeAnimal(@PathVariable("id") long id, @RequestBody Animale animale) {
        Optional<Utente> user = UtenteRepository.findById(id);
        if (user.isPresent()) {
            Utente _utente = user.get();
            _utente.removeAnimale(animale);
            return new ResponseEntity<>(UtenteRepository.save(_utente), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateAnimal/{idU}/{idA}")
    public Animale updateAnimal(@PathVariable("idU") long id, @PathVariable("idA") long idA, @RequestBody Animale animale) {
        Optional<Utente> user = UtenteRepository.findById(id);
        Optional<Animale> animal = AnimaleRepository.findById(id);
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
                return AnimaleRepository.save(_animale);
            }
        }
        return null;
    }

    @GetMapping(value = "user/email/{email}")
    public Utente findByAge(@PathVariable String email) {
        return UtenteRepository.findByEmail(email);
    }

    @GetMapping(value = "customers/id/{userid}")
    public Optional<Utente> findByAge(@PathVariable Long userid) {
        return UtenteRepository.findById(userid);
    }

    @GetMapping("/")
    public String helloWorld(){
        return "Autenticarsi prima";
    }

    @GetMapping("/autenticazionegoogle")
    public String user() throws JsonProcessingException, ParseException {
        return "Autenticato";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal) {
        String username = Collections.singletonMap("name", principal.getAttribute("name")).get("name").toString();
        String email = Collections.singletonMap("email", principal.getAttribute("email")).get("email").toString();
        return "Il tuo nome é: " + username + " La tua email é: " + email;
    }





}
