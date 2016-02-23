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
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Rezervacija {

	@Id
	@GeneratedValue
	private int id;
	
	@NotNull
	private String vreme;
	
	@NotNull
	private double trajanje;
	
	@NotNull
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinColumn
	private User user;
	
	@NotNull
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinColumn
	private Restoran restoran;
	
	@JsonIgnore
	@OneToMany(mappedBy = "rezervacija", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Poseta> posete;
	
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
	private Set<Sto> stolovi;

	public Rezervacija() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVreme() {
		return vreme;
	}

	public void setVreme(String vreme) {
		this.vreme = vreme;
	}

	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Restoran getRestoran() {
		return restoran;
	}

	public void setRestoran(Restoran restoran) {
		this.restoran = restoran;
	}

	public Set<Poseta> getPosete() {
		return posete;
	}

	public void setPosete(Set<Poseta> posete) {
		this.posete = posete;
	}
	

	public Set<Sto> getStolovi() {
		return stolovi;
	}

	public void setStolovi(Set<Sto> stolovi) {
		this.stolovi = stolovi;
	}


	public double getTrajanje() {
		return trajanje;
	}

	public void setTrajanje(double trajanje) {
		this.trajanje = trajanje;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((posete == null) ? 0 : posete.hashCode());
		result = prime * result + ((stolovi == null) ? 0 : stolovi.hashCode());
		long temp;
		temp = Double.doubleToLongBits(trajanje);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((vreme == null) ? 0 : vreme.hashCode());
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
		Rezervacija other = (Rezervacija) obj;
		if (id != other.id)
			return false;
		if (posete == null) {
			if (other.posete != null)
				return false;
		} else if (!posete.equals(other.posete))
			return false;
		if (stolovi == null) {
			if (other.stolovi != null)
				return false;
		} else if (!stolovi.equals(other.stolovi))
			return false;
		if (Double.doubleToLongBits(trajanje) != Double.doubleToLongBits(other.trajanje))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (vreme == null) {
			if (other.vreme != null)
				return false;
		} else if (!vreme.equals(other.vreme))
			return false;
		return true;
	}
	
	
	
	
	
	
	
}
