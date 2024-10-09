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

import entities.Acao;
import services.AcaoService;

@RestController
@RequestMapping("api/acoes")
public class AcaoController {

    @Autowired
    private AcaoService acaoService;

 
    @PostMapping("/processos/{id}")
    public ResponseEntity<Acao> criarAcao(@PathVariable Long id, @RequestBody Acao acao) {
        Acao novaAcao = acaoService.criarAcao(id, acao);
        return new ResponseEntity<>(novaAcao, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Acao>> listarAcoes() {
        List<Acao> acoes = acaoService.listarAcoes();
        return new ResponseEntity<>(acoes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Acao> obterAcao(@PathVariable Long id) {
        Acao acao = acaoService.obterAcao(id);
        return new ResponseEntity<>(acao, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Acao> editarAcao(@PathVariable Long id, @RequestBody Acao acao) {
        Acao acaoEditada = acaoService.editarAcao(id, acao);
        return new ResponseEntity<>(acaoEditada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAcao(@PathVariable Long id) {
        acaoService.deletarAcao(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
