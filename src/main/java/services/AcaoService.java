package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entities.Acao;
import entities.Processo;
import exception.AcaoNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import repositories.AcaoRepository;
import repositories.ProcessoRepository;

@Service
public class AcaoService {

    @Autowired
    private AcaoRepository acaoRepository;

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private ProcessoService processoService;

    public Acao criarAcao(Long id, Acao acao) {
        Processo processo = processoService.obterProcesso(id);
        
        if (processo == null) {
            throw new EntityNotFoundException("Processo não encontrado com o número: " + id);
        }

        acao.setProcesso(processo);
        return acaoRepository.save(acao);
    }

    public List<Acao> listarAcoes() {
        return acaoRepository.findAll();
    }

    public Acao obterAcao(Long id) {
        return acaoRepository.findById(id)
            .orElseThrow(() -> new AcaoNotFoundException(id));
    }

    public Acao editarAcao(Long id, Acao acaoAtualizada) {
        Acao acao = obterAcao(id);
        acao.setTipo(acaoAtualizada.getTipo());
        acao.setDataRegistro(acaoAtualizada.getDataRegistro());
        acao.setDescricao(acaoAtualizada.getDescricao());
        return acaoRepository.save(acao);
    }

    public void deletarAcao(Long id) {
        Acao acao = obterAcao(id);
        acaoRepository.delete(acao);
    }
}
