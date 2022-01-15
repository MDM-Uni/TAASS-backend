package com.petlife.utentemicroservizio.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import com.petlife.utentemicroservizio.repositories.AnimaleRepository;
import com.petlife.utentemicroservizio.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;


@RestController
public class UtenteController {

    @Autowired(required = false)
    private UtenteRepository UtenteRepository;

    @Autowired(required = false)
    private AnimaleRepository AnimaleRepository;

    @GetMapping("/")
    public String helloWorld(){
        return "Autenticarsi prima";
    }

    @GetMapping("/autenticazionegoogle")
    public String riservata(){
        return "Autenticato";
    }

    @GetMapping("/userinfo")
    public Principal user(Principal principal) throws JsonProcessingException, ParseException {
        //parsificare il principal
        return principal;
    }


}
