package app.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Poseta {
	 public enum Status {
	        PRIHVACENO, ODBIJENO, NEODLUCENO
	    }

	@Id
	@GeneratedValue
	private int id;

	private int ocena;
	
	private String komentar;
	
	@NotNull
    @Enumerated(EnumType.STRING)
	private  Status status = Status.NEODLUCENO;
	
	@NotNull
	@ManyToOne
	@JoinColumn
	private User user;
	
	@NotNull
	@ManyToOne
	@JoinColumn
	private Rezervacija rezervacija;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOcena() {
		return ocena;
	}

	public void setOcena(int ocena) {
		this.ocena = ocena;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Rezervacija getRezervacija() {
		return rezervacija;
	}

	public void setRezervacija(Rezervacija rezervacija) {
		this.rezervacija = rezervacija;
	}
	
	

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	

	public String getKomentar() {
		return komentar;
	}

	public void setKomentar(String komentar) {
		this.komentar = komentar;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((komentar == null) ? 0 : komentar.hashCode());
		result = prime * result + ocena;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Poseta other = (Poseta) obj;
		if (id != other.id)
			return false;
		if (komentar == null) {
			if (other.komentar != null)
				return false;
		} else if (!komentar.equals(other.komentar))
			return false;
		if (ocena != other.ocena)
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
	
	
	
	
	
}
