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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class UtenteController {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private AnimaleRepository animaleRepository;

    @PostMapping(value = "/user/create")
    public Utente postCustomer(@RequestBody Utente utente) {
        return utenteRepository.save(new Utente(utente.getNome(),utente.getEmail(),utente.getAnimali()));
    }

    @GetMapping("/users")
    public List<Utente> getAllUsers() {
        List<Utente> users = new ArrayList<>();
        utenteRepository.findAll().forEach(users::add);
        return users;
    }

    @GetMapping("/animals/{id}")
    public List<Animale> getAnimalsUser(@PathVariable("id") long id) {
        Optional<Utente> user = utenteRepository.findById(id);
        if(user.isPresent()) {
            return user.get().getAnimali();
        }
        return new ArrayList<Animale>();
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<String> deleteUser(@RequestBody Utente utente) {
        utenteRepository.delete(utente);
        return new ResponseEntity<>("User" + utente.getNome() + "è stato eliminato correttamente", HttpStatus.OK);
    }

    @PostMapping("/addAnimal/{id}")
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


    @PostMapping("/addAnimalTest/{id}/{nome}")
    public Utente addAnimalTest(@PathVariable("id") long id, @PathVariable("nome") String nome) {
        Optional<Utente> user = utenteRepository.findById(id);
        if (user.isPresent()) {
            Utente _utente = user.get();
            Animale animale = new Animale(nome);
            _utente.addAnimale(animale);
            addAnimal(nome);
            return utenteRepository.save(_utente);
        } else {
            return null;
        }
    }

    @GetMapping("/addAnimal/{nome}")
    public ResponseEntity<Animale> addAnimal(@PathVariable("nome") String nome) {
        return new ResponseEntity<Animale>(animaleRepository.save(new Animale("Pluto")),HttpStatus.OK);
    }

    @DeleteMapping("/removeAnimal/{id}/{idA}")
    public ResponseEntity<String> removeAnimal(@PathVariable("id") long id, @PathVariable("idA") long idA) {
        Optional<Utente> user = utenteRepository.findById(id);
        Optional<Animale> animal = animaleRepository.findById(idA);
        if (user.isPresent() && animal.isPresent()) {
            Utente _utente = user.get();
            _utente.removeAnimale(animal.get());
            animaleRepository.delete(animal.get());
            return new ResponseEntity<>("Animale eliminato", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateAnimal/{idU}/{idA}")
    public ResponseEntity<Animale> updateAnimal(@PathVariable("idU") long id, @PathVariable("idA") long idA, @RequestBody Animale animale) {
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
                _animale.setPeloLungo(animale.getPeloLungo());
                _animale.setRazza(animale.getRazza());
                return new ResponseEntity<>(animaleRepository.save(_animale), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "user/{email}/{nome}")
    public Utente getUser(@PathVariable String email, @PathVariable String nome) {
        Optional<Utente> user = utenteRepository.findByEmail(email);
        List<Animale> animali = new ArrayList<Animale>();
        return user.orElseGet(() -> utenteRepository.save(new Utente(nome, email, animali)));
    }

    /*

    @GetMapping("/")
    public String helloWorld(){
        return "Autenticarsi prima";
    }

    @GetMapping("/autenticazionegoogle")
    public Utente user(@AuthenticationPrincipal OAuth2User principal) throws JsonProcessingException, ParseException {
        String nome = Collections.singletonMap("name", principal.getAttribute("name")).get("name").toString();
        String email = Collections.singletonMap("email", principal.getAttribute("email")).get("email").toString();
        List<Animale> animali = new ArrayList<>();
        return utenteRepository.save(new Utente(nome,email,animali));
    }

    @GetMapping("/userTest")
    public String userTest(@AuthenticationPrincipal OAuth2User principal) {
        String username = Collections.singletonMap("name", principal.getAttribute("name")).get("name").toString();
        String email = Collections.singletonMap("email", principal.getAttribute("email")).get("email").toString();
        return "Il tuo nome è: " + username + "la tua email è: " + email;
    }

    */

}
