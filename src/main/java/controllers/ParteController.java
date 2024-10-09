package controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entities.Parte;
import services.ParteService;

@RestController
@RequestMapping("api/partes")
public class ParteController {

    @Autowired
    private ParteService parteService;


    @PostMapping("/processos/{id}")
    public ResponseEntity<Parte> criarParte(@PathVariable Long id, @RequestBody Parte parte) {
        Parte novaParte = parteService.criarParte(id, parte);
        return new ResponseEntity<>(novaParte, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Parte>> listarPartes() {
        List<Parte> partes = parteService.listarPartes();
        return new ResponseEntity<>(partes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parte> obterParte(@PathVariable Long id) {
        Parte parte = parteService.obterParte(id);
        return new ResponseEntity<>(parte, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parte> editarParte(@PathVariable Long id, @RequestBody Parte parte) {
        Parte parteEditada = parteService.editarParte(id, parte);
        return new ResponseEntity<>(parteEditada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarParte(@PathVariable Long id) {
        parteService.deletarParte(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}