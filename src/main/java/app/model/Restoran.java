package app.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Restoran {

	@Id
	@GeneratedValue
	private int id;
	
	@NotNull
	private String naziv;
	
	private String opis;
	
	private int brojRedova;
	private int brojKolona;
	
    @OneToMany(mappedBy = "restoran", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Jelo> jelovnik;
	
    @JsonIgnore
    @OneToMany(mappedBy = "restoran", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Rezervacija> rezervacije;
    
    @OneToMany(mappedBy = "restoran", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Sto> stolovi;
    
	public Restoran() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public Set<Jelo> getJelovnik() {
		return jelovnik;
	}

	public void setJelovnik(Set<Jelo> jelovnik) {
		this.jelovnik = jelovnik;
	}

	public Set<Rezervacija> getRezervacije() {
		return rezervacije;
	}

	public void setRezervacije(Set<Rezervacija> rezervacije) {
		this.rezervacije = rezervacije;
	}
	

	public Set<Sto> getStolovi() {
		return stolovi;
	}

	public void setStolovi(Set<Sto> stolovi) {
		this.stolovi = stolovi;
	}
	

	public int getBrojRedova() {
		return brojRedova;
	}

	public void setBrojRedova(int brojRedova) {
		this.brojRedova = brojRedova;
	}

	public int getBrojKolona() {
		return brojKolona;
	}

	public void setBrojKolona(int brojKolona) {
		this.brojKolona = brojKolona;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((jelovnik == null) ? 0 : jelovnik.hashCode());
		result = prime * result + ((naziv == null) ? 0 : naziv.hashCode());
		result = prime * result + ((opis == null) ? 0 : opis.hashCode());
		result = prime * result + ((rezervacije == null) ? 0 : rezervacije.hashCode());
		//result = prime * result + ((stolovi == null) ? 0 : stolovi.hashCode());
		result = prime * result + ((brojKolona ==0) ? 0 : brojKolona);
		result = prime * result + ((brojRedova ==0) ? 0 : brojRedova);
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
		Restoran other = (Restoran) obj;
		if (id != other.id)
			return false;
		if (jelovnik == null) {
			if (other.jelovnik != null)
				return false;
		} else if (!jelovnik.equals(other.jelovnik))
			return false;
		if (naziv == null) {
			if (other.naziv != null)
				return false;
		} else if (!naziv.equals(other.naziv))
			return false;
		if (opis == null) {
			if (other.opis != null)
				return false;
		} else if (!opis.equals(other.opis))
			return false;
		if (rezervacije == null) {
			if (other.rezervacije != null)
				return false;
		} else if (!rezervacije.equals(other.rezervacije))
			return false;
		if (stolovi == null) {
			if (other.stolovi != null)
				return false;
		} else if (!stolovi.equals(other.stolovi)){
			return false;
			
		}else if(brojRedova!= other.brojRedova){
			return false;
		}else if(brojKolona != other.brojKolona)
			return false;
			
		return true;
	}
	
	
	
	
	
}
