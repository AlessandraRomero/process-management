package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.Parte;
import entities.Processo;
import exception.ParteNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import repositories.ParteRepository;

@Service
public class ParteService {

    @Autowired
    private ParteRepository parteRepository;
    
    @Autowired
    private ProcessoService processoService;

    public Parte criarParte(Long id, Parte parte) {
        Processo processo = processoService.obterProcesso(id);
        if (processo == null) {
            throw new EntityNotFoundException("Processo não encontrado com o número: " + id);
        }

        parte.setProcesso(processo);
        return parteRepository.save(parte);
    }

    public List<Parte> listarPartes() {
        return parteRepository.findAll();
    }

    public Parte obterParte(Long id) {
        return parteRepository.findById(id).orElseThrow(() -> new ParteNotFoundException(id)); 
    }

    public Parte editarParte(Long id, Parte parteAtualizada) {
        Parte parte = parteRepository.findById(id)
            .orElseThrow(() -> new ParteNotFoundException(id)); 
        
        parte.setNomeCompleto(parteAtualizada.getNomeCompleto());
        parte.setCpfCnpj(parteAtualizada.getCpfCnpj());
        parte.setTipo(parteAtualizada.getTipo());
        parte.setEmail(parteAtualizada.getEmail());
        parte.setTelefone(parteAtualizada.getTelefone());
        
        return parteRepository.save(parte);
    }

    public void deletarParte(Long id) {
        Parte parte = parteRepository.findById(id)
            .orElseThrow(() -> new ParteNotFoundException(id)); 
        parteRepository.delete(parte);
    }
}
