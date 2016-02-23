package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Poseta;

public interface PosetaRepository extends JpaRepository<Poseta, Integer> {
	
	
}
