package com.petlife.utentemicroservizio.controllers;
import com.petlife.utentemicroservizio.models.Animale;
import com.petlife.utentemicroservizio.repositories.AnimaleRepository;
import com.petlife.utentemicroservizio.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtenteController {

    @GetMapping("/")
    public String helloWorld(){
        return "Autenticarsi prima";
    }

    @GetMapping("/autenticazionegoogle")
    public String riservata(){
        return "Autenticato";
    }



}
