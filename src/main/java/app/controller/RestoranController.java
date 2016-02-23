package app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.Jelo;
import app.model.Poseta;
import app.model.Restoran;
import app.model.Rezervacija;
import app.model.Sto;
import app.model.User;
import app.repository.JeloRepository;
import app.repository.PosetaRepository;
import app.repository.RestoranRepository;
import app.repository.RezervacijaRepository;
import app.repository.StoRepository;
import app.repository.UserRepository;

@RestController
@RequestMapping("/restaurants")
public class RestoranController {

	@Autowired
	RestoranRepository restoranRepository;
	
	@Autowired
	JeloRepository jeloRepository;
	
	@Autowired
	StoRepository stoRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RezervacijaRepository rezervacijaRepository;
	
	@Autowired
	PosetaRepository posetaRepository;
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity getRestorani(){
		return new ResponseEntity(restoranRepository.findAll(), HttpStatus.OK);
	}
	
	//@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity getRestoran(@PathVariable("id") int id){
		Restoran restoran = restoranRepository.findOne(id);
		if(restoran == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}else{
			return new ResponseEntity(restoran, HttpStatus.OK);
			
		}
		
	}
	
	@PreAuthorize("hasRole('MENADZER_SISTEMA')")
	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createRestoran(@RequestBody Restoran restoran){
       /* Set<Jelo> jelovnik = addJela(restoran);
        restoran.setJelovnik(jelovnik);
*/
        restoranRepository.save(restoran);
        /*final Set<Jelo> updatedJelovnik = restoran.getJelovnik();
		updatedJelovnik.forEach(jelo-> jelo.setRestoran(restoran));
		jeloRepository.save(updatedJelovnik);
		
		final Set<Sto> updateStolovi = restoran.getStolovi();
		updateStolovi.forEach(sto-> sto.setRestoran(restoran));
		stoRepository.save(updateStolovi);*/
        return new ResponseEntity<>(restoran, HttpStatus.CREATED);
    }
	
	@PreAuthorize("hasAnyRole('MENADZER_SISTEMA','MENADZER_RESTORANA')")
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	public ResponseEntity updateRestoran(@RequestBody Restoran update, @PathVariable("id") int id){
		final Restoran restoran = restoranRepository.findOne(id);
		if(restoran.getId() != id){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		final Set<Jelo> updatedJelovnik = update.getJelovnik();
		updatedJelovnik.forEach(jelo-> jelo.setRestoran(restoran));
		jeloRepository.save(updatedJelovnik);
		
		final Set<Sto> updateStolovi = update.getStolovi();
		updateStolovi.forEach(sto-> sto.setRestoran(restoran));
		stoRepository.save(updateStolovi);
		
		restoranRepository.save(update);
		return new ResponseEntity(HttpStatus.OK);
		
	}
	@PreAuthorize("hasRole('MENADZER_RESTORANA')")
	@RequestMapping(method = RequestMethod.DELETE, value = "/jelo/{id}")
	public ResponseEntity deleteJelo(@PathVariable("id") int id){
		Jelo jelo = jeloRepository.findOne(id);
		jeloRepository.delete(jelo);
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('MENADZER_RESTORANA')")
	@RequestMapping(method = RequestMethod.POST, value = "/createTables/{id}")
	public ResponseEntity createTables(@PathVariable("id") int id, @RequestBody ArrayList<Sto> stolovi){
		Restoran r = restoranRepository.findOne(id);
		
		if(r==null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		stolovi.forEach(sto->sto.setRestoran(r));
		stoRepository.save(stolovi);
		restoranRepository.save(r);
		
		
		return new ResponseEntity(HttpStatus.OK);
		
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/ocena/{restId}/{userId}")
	public ResponseEntity ocenaRestorana(@PathVariable("restId") int restId, @PathVariable("userId") int userId){
		Restoran restoran = restoranRepository.findOne(restId);
		if(restoran==null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		User user = userRepository.findOne(userId);
		if(user == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		
		ArrayList<Rezervacija> allRezervacije = (ArrayList<Rezervacija>) rezervacijaRepository.findAll();
		ArrayList<Rezervacija> restRezervacije = new ArrayList<Rezervacija>();
		ArrayList<Poseta> posete = new ArrayList<Poseta>();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		for(Rezervacija rez: allRezervacije){
			Date rezDate = null;
			try {
				rezDate = sdf.parse(rez.getVreme());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(rezDate==null){
				break;
			}
			if(rez.getRestoran().getId() == restoran.getId() && rezDate.before(date)){
				restRezervacije.add(rez);
				posete.addAll(rez.getPosete());
			}
		}
		
		int ukupnaOcena = 0;
		int brojOcena =0;
		int ukupnaOcenaPrijatelji = 0;
		int brojOcenaPrijatelji = 0;
		
		for(Poseta p : posete){
			if(p.getOcena()>0){
				ukupnaOcena+=p.getOcena();
				brojOcena++;
				
				if(user.getFriends().contains(p.getUser())){
					ukupnaOcenaPrijatelji+=p.getOcena();
					brojOcenaPrijatelji++;
				}
			}
		}
		//Prva je ukupna druga je preko prijatelja
		ArrayList<Double> ocene = new ArrayList<Double>();
		
		Double uO = new Double(ukupnaOcena);
		Double bo = new Double(brojOcena);
		Double uPo = new Double(ukupnaOcenaPrijatelji);
		Double uPoB = new Double(brojOcenaPrijatelji);
		if(ukupnaOcena <=0 || brojOcena <=0){
			ocene.add((double)0);
		}else{
			double retVal =0;
			retVal = uO/bo;
			ocene.add(retVal);
		}
		if(ukupnaOcenaPrijatelji <=0 || brojOcenaPrijatelji <=0){
			ocene.add((double)0);
		}else{
			double retVal =0;
			retVal = uPo/uPoB;
			ocene.add(retVal);
		}
		
		return new ResponseEntity(ocene, HttpStatus.OK);
		
	}

	
	private Set<Jelo> addJela(Restoran restoran){
		
		Set<Jelo> jelovnik = new HashSet<>();
		
		for(Jelo jelo : restoran.getJelovnik()){
			if(jelo.getId() != 0){
				final Jelo pronadjenoJelo =	jeloRepository.findOne(jelo.getId());
				
				if(pronadjenoJelo != null){
					jelovnik.add(pronadjenoJelo);
				}
			}else{
				jelovnik.add(jelo);
				
			}
		}
		return jelovnik;
	}
}
