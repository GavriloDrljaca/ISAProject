package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Sto;

public interface StoRepository extends JpaRepository<Sto, Integer>{

}
