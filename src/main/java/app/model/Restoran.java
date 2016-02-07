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
import javax.validation.constraints.NotNull;

@Entity
public class Restoran {

	@Id
	@GeneratedValue
	private int id;
	
	@NotNull
	private String naziv;
	
	private String opis;
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "restoran_jelovnik", joinColumns = @JoinColumn(name="Restoran.id"), inverseJoinColumns = @JoinColumn(name = "Jelo.id"))
	private Set<Jelo> jelovnik;
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
	
	
	
	
}
