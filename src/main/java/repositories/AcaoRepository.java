package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entities.Acao;

@Repository
public interface AcaoRepository extends JpaRepository<Acao, Long> {
   
}
