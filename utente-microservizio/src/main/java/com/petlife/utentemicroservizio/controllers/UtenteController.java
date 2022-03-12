package com.petlife.utentemicroservizio.controllers;

import com.petlife.utentemicroservizio.models.Animale;
import com.petlife.utentemicroservizio.models.Utente;
import com.petlife.utentemicroservizio.repositories.AnimaleRepository;
import com.petlife.utentemicroservizio.repositories.UtenteRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
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

    @GetMapping("/exists_user/{id}")
    public ResponseEntity<String> existUser(@PathVariable("id") long id) {
        Optional<Utente> user = utenteRepository.findById(id);
        if(user.isPresent()) {
            return new ResponseEntity<>("true",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("false",HttpStatus.NOT_FOUND);
        }
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

    @RequestMapping(value="/aggiungiAnimale/{nome}", method = RequestMethod.POST)
    public ResponseEntity<Animale> addAnimal(@PathVariable("nome") String nome) {
        Animale animale = new Animale();
        animale.setNome(nome);
        return new ResponseEntity<Animale>(animaleRepository.save(animale),HttpStatus.OK);
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
            rabbitTemplate.convertAndSend(RabbitMQCOnfig.EXCHANGE,RabbitMQCOnfig.ROUTINGKEY_A,animal.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/updateAnimal/{idU}/{idA}" , method = RequestMethod.PUT)
    public Animale updateAnimal(@PathVariable("idU") long id, @PathVariable("idA") long idA, @RequestBody Animale animale) {
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
                return animaleRepository.save(_animale);
            }
        }
        return null;
    }

    @GetMapping(value = "user/{email}/{nome}")
    public Utente getUser(@PathVariable String email, @PathVariable String nome) {
        Optional<Utente> user = utenteRepository.findByEmail(email);
        List<Animale> animali = new ArrayList<Animale>();
        if(user.isPresent()) {
            return user.get();
        } else {
            Utente utente = utenteRepository.save(new Utente(nome, email, animali));
            rabbitTemplate.convertAndSend(RabbitMQCOnfig.EXCHANGE,RabbitMQCOnfig.ROUTINGKEY_B,utente);
            return utente;
        }
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
