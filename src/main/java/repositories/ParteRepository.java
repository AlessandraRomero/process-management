package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entities.Parte;



@Repository
public interface ParteRepository extends JpaRepository<Parte, Long> {
}