package app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Jelo {

	@Id
	@GeneratedValue
	private int id;
	
	@NotNull
	private String naziv;
	
	private String opis;
	
	private double cena;

	public Jelo(int id, String naziv, String opis, double cena) {
		super();
		this.id = id;
		this.naziv = naziv;
		this.opis = opis;
		this.cena = cena;
	}

	public Jelo() {
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

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}
	
	
	
	
}
