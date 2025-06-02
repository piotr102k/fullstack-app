package com.example.pasir_swiszcz_piotr.controller;

import com.example.pasir_swiszcz_piotr.DTO.InfoDTO;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    @GetMapping("/api/info")
    public InfoDTO info() {
        return new InfoDTO("Aplikacja Budżetowa","1.0","Witaj w aplikacji budżetowej stworzonej ze Spring Boot!");
    }
}
