package services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.Acao;
import entities.Parte;
import entities.Processo;
import enums.StatusProcesso;
import exception.AcaoNotFoundException;
import exception.ParteNotFoundException;
import exception.ProcessoNotFoundException;
import repositories.ProcessoRepository;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;

    public Processo criarProcesso(Processo processo) {
        return processoRepository.save(processo);
    }

    public List<Processo> listarProcessos() {
        return processoRepository.findAll();
    }
    
    public List<Processo> buscarPorStatus(StatusProcesso status) {
        return processoRepository.findByStatus(status);
    }

    public List<Processo> buscarPorDataAbertura(LocalDate dataAbertura) {
        return processoRepository.findByDataAbertura(dataAbertura);
    }

    public List<Processo> buscarPorCpfCnpj(String cpfCnpj) {
        return processoRepository.findByCpfCnpj(cpfCnpj);
    }

    public Processo obterProcesso(Long id) {
        return processoRepository.findById(id)
                .orElseThrow(() -> new ProcessoNotFoundException(id));
    }
    
    public Processo obterProcessoPorNumero(String numeroProcesso) {
        return processoRepository.findByNumeroProcesso(numeroProcesso);
    }

    public Processo editarProcesso(Long id, Processo processoAtualizado) {
        Processo processo = obterProcesso(id);

        processo.setNumeroProcesso(processoAtualizado.getNumeroProcesso());
        processo.setDataAbertura(processoAtualizado.getDataAbertura());
        processo.setDescricao(processoAtualizado.getDescricao());
        processo.setStatus(processoAtualizado.getStatus());

        atualizarPartes(processo, processoAtualizado.getPartes());
        atualizarAcoes(processo, processoAtualizado.getAcoes());

        return processoRepository.save(processo);
    }

    public void atualizarPartes(Processo processo, List<Parte> partesAtualizadas) {
        for (Parte parteAtualizada : partesAtualizadas) {
            if (parteAtualizada.getId() != null) { 
                Parte parteExistente = processo.getPartes().stream()
                    .filter(p -> p.getId().equals(parteAtualizada.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ParteNotFoundException(parteAtualizada.getId()));

                parteExistente.setNomeCompleto(parteAtualizada.getNomeCompleto());
                parteExistente.setCpfCnpj(parteAtualizada.getCpfCnpj());
                parteExistente.setTipo(parteAtualizada.getTipo());
                parteExistente.setEmail(parteAtualizada.getEmail());
                parteExistente.setTelefone(parteAtualizada.getTelefone());
            } else {
                processo.getPartes().add(parteAtualizada);
            }
        }
    }

    public void atualizarAcoes(Processo processo, List<Acao> acoesAtualizadas) {
        for (Acao acaoAtualizada : acoesAtualizadas) {
            if (acaoAtualizada.getId() != null) { 
                Acao acaoExistente = processo.getAcoes().stream()
                    .filter(a -> a.getId().equals(acaoAtualizada.getId()))
                    .findFirst()
                    .orElseThrow(() -> new AcaoNotFoundException(acaoAtualizada.getId()));

                acaoExistente.setTipo(acaoAtualizada.getTipo());
                acaoExistente.setDataRegistro(acaoAtualizada.getDataRegistro());
                acaoExistente.setDescricao(acaoAtualizada.getDescricao());
            } else {
                processo.getAcoes().add(acaoAtualizada);
            }
        }
    }

    public void arquivarProcesso(Long id) {
        Processo processo = obterProcesso(id);
        processo.setStatus(StatusProcesso.ARQUIVADO); 
        processoRepository.save(processo);
    }

    public void deletarProcesso(Long id) {
        Processo processo = obterProcesso(id); 
        processoRepository.delete(processo);
    }
}
