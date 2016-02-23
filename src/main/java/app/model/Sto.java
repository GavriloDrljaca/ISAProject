package app.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Sto {

	@Id
	@GeneratedValue
	private int id;

	private boolean pusacki;
	
	private boolean dostupan;

	private int brojStolova;
	
	private int redniBroj;
	
	@NotNull
	@ManyToOne
	@JoinColumn
	@JsonIgnore
	private Restoran restoran;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Rezervacija> rezervacije;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isPusacki() {
		return pusacki;
	}

	public void setPusacki(boolean pusacki) {
		this.pusacki = pusacki;
	}

	public int getBrojStolova() {
		return brojStolova;
	}

	public void setBrojStolova(int brojStolova) {
		this.brojStolova = brojStolova;
	}

	public Restoran getRestoran() {
		return restoran;
	}

	public void setRestoran(Restoran restoran) {
		this.restoran = restoran;
	}
	
	public Set<Rezervacija> getRezervacije() {
		return rezervacije;
	}

	public void setRezervacije(Set<Rezervacija> rezervacije) {
		this.rezervacije = rezervacije;
	}
	
	

	public boolean isDostupan() {
		return dostupan;
	}

	public void setDostupan(boolean dostupan) {
		this.dostupan = dostupan;
	}
	
	
	public int getRedniBroj() {
		return redniBroj;
	}

	public void setRedniBroj(int redniBroj) {
		this.redniBroj = redniBroj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + brojStolova;
		result = prime * result + (dostupan ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + (pusacki ? 1231 : 1237);
		result = prime * result + redniBroj;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sto other = (Sto) obj;
		if (brojStolova != other.brojStolova)
			return false;
		if (dostupan != other.dostupan)
			return false;
		if (id != other.id)
			return false;
		if (pusacki != other.pusacki)
			return false;
		if (redniBroj != other.redniBroj)
			return false;
		return true;
	}

}
