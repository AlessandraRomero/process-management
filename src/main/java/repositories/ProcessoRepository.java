package repositories;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import entities.Processo;
import enums.StatusProcesso;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Long> {
    Processo findByNumeroProcesso(String numeroProcesso);
    List<Processo> findByStatus(StatusProcesso status);
    List<Processo> findByDataAbertura(LocalDate dataAbertura);
    @Query("SELECT p FROM Processo p JOIN p.partes pa WHERE pa.cpfCnpj = :cpfCnpj")
    List<Processo> findByCpfCnpj(@Param("cpfCnpj") String cpfCnpj);

}
