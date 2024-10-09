package controllers;

import java.time.LocalDate;
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
import entities.Parte;
import entities.Processo;
import enums.StatusProcesso;
import services.ProcessoService;

@RestController
@RequestMapping("api/processos")
public class ProcessoController {

	@Autowired
	private ProcessoService processoService;

	@PostMapping
	public ResponseEntity<Processo> criarProcesso(@RequestBody Processo processo) {
	    if (processo.getStatus() == null) {
	        processo.setStatus(StatusProcesso.ATIVO); 
	    }
	    if (processo.getPartes() != null) {
	        for (Parte parte : processo.getPartes()) {
	            parte.setProcesso(processo);
	        }
	    }
	    if (processo.getAcoes() != null) {
	        for (Acao acao : processo.getAcoes()) {
	            acao.setProcesso(processo);
	        }
	    }
	    Processo novoProcesso = processoService.criarProcesso(processo);
	    return ResponseEntity.status(HttpStatus.CREATED).body(novoProcesso);
	}


	@GetMapping
	public ResponseEntity<List<Processo>> listarProcessos() {
		List<Processo> processos = processoService.listarProcessos();
		return new ResponseEntity<>(processos, HttpStatus.OK);
	}
	
	 @GetMapping("/status/{status}")
	    public ResponseEntity<List<Processo>> buscarPorStatus(@PathVariable StatusProcesso status) {
	        List<Processo> processos = processoService.buscarPorStatus(status);
	        return ResponseEntity.ok(processos);
	    }

	    @GetMapping("/data-abertura/{dataAbertura}")
	    public ResponseEntity<List<Processo>> buscarPorDataAbertura(@PathVariable LocalDate dataAbertura) {
	        List<Processo> processos = processoService.buscarPorDataAbertura(dataAbertura);
	        return ResponseEntity.ok(processos);
	    }

	    @GetMapping("/cpf-cnpj/{cpfCnpj}")
	    public ResponseEntity<List<Processo>> buscarPorCpfCnpj(@PathVariable String cpfCnpj) {
	        List<Processo> processos = processoService.buscarPorCpfCnpj(cpfCnpj);
	        return ResponseEntity.ok(processos);
	    }

	@GetMapping("/{id}")
	public ResponseEntity<Processo> obterProcesso(@PathVariable Long id) {
		Processo processo = processoService.obterProcesso(id);
		return new ResponseEntity<>(processo, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Processo> editarProcesso(@PathVariable Long id, @RequestBody Processo processo) {
		Processo processoEditado = processoService.editarProcesso(id, processo);
		return new ResponseEntity<>(processoEditado, HttpStatus.OK);
	}

	@PutMapping("/{id}/arquivar")
	public ResponseEntity<Void> arquivarProcesso(@PathVariable Long id) {
		processoService.arquivarProcesso(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarProcesso(@PathVariable Long id) {
		processoService.deletarProcesso(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}