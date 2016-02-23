package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Rezervacija;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Integer> {
	


}
