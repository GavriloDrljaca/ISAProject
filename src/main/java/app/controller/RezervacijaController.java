package app.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.Poseta;
import app.model.Poseta.Status;
import app.model.Restoran;
import app.model.Rezervacija;
import app.model.Sto;
import app.model.User;
import app.repository.PosetaRepository;
import app.repository.RestoranRepository;
import app.repository.RezervacijaRepository;
import app.repository.UserRepository;
import app.services.SmtpMailSender;

@RestController
@RequestMapping("rezervacije")
public class RezervacijaController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RestoranRepository restoranRepository;
	
	@Autowired
	RezervacijaRepository rezervacijaRepository;
	
	@Autowired
	PosetaRepository posetaRepository;
	
	private final long MINUTE_IN_MILLS = 60000;
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity deleteRezervacija(@PathVariable("id") int id){
		Rezervacija rez = rezervacijaRepository.findOne(id);
		if(rez == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}else{
			
			User user = userRepository.findOne(rez.getUser().getId());
			user.getRezervacije().remove(rez);
			Restoran restoran = restoranRepository.findOne(rez.getRestoran().getId());
			restoran.getRezervacije().remove(rez);
			
			userRepository.save(user);
			restoranRepository.save(restoran);
			rezervacijaRepository.delete(rez);
			return new ResponseEntity( HttpStatus.OK);
		}
	}
	
	@Transactional
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.POST, value = "{userId}/{restoranId}")
	public ResponseEntity createRezervacija(@PathVariable("userId") int userId, @PathVariable("restoranId") int restoranId, @RequestBody Rezervacija rezervacija){
		User user = userRepository.findOne(userId);
		if(user == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		Restoran restoran = restoranRepository.findOne(restoranId);
		if(restoran == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		if(rezervacija.getVreme() == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		//PROVERITI DA LI JE NEKI STO U MEDJUVREMENU ZAUZET
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date targetDateStart = null;
		
		try {
			targetDateStart = sdf.parse(rezervacija.getVreme());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Date today = new Date();
		ArrayList<Rezervacija> allRez = (ArrayList<Rezervacija>) rezervacijaRepository.findAll();
		ArrayList<Sto> zauzetiStolovi = new ArrayList<Sto>();
		for(Rezervacija rez: allRez){
			Date rezDateStart = null;
			try {
				rezDateStart = sdf.parse(rez.getVreme());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			if(rezDateStart == null){
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			
			if(isZauzet(rezDateStart, rez.getTrajanje(), targetDateStart, rezervacija.getTrajanje())){
				zauzetiStolovi.addAll(rez.getStolovi());
			}
		}
		
		for(Sto sto : rezervacija.getStolovi()){
			for(Sto sto2: zauzetiStolovi){
				if(sto.getId() == sto2.getId()){
					return new ResponseEntity("ZAUZETO", HttpStatus.BAD_REQUEST);
				}
			}
		}
		
		rezervacija.setRestoran(restoran);
		rezervacija.setUser(user);
		
		rezervacijaRepository.save(rezervacija);
		//Kreirati posetu korisnika po ovoj rezervaciji
		Poseta poseta = new Poseta();
		poseta.setStatus(Status.PRIHVACENO);
		poseta.setUser(user);
		poseta.setRezervacija(rezervacija);
		posetaRepository.save(poseta);

		return new ResponseEntity(HttpStatus.OK);
	}
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.GET, value = "zauzetiStolovi/{trajanje}/{vreme}")
	public ResponseEntity getZauzetiStolovi(@PathVariable("trajanje") double trajanje, @PathVariable("vreme") Date vreme){

		Date targetDateStart = vreme;

		
		if(targetDateStart == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		Date today = new Date();
		ArrayList<Rezervacija> allRez = (ArrayList<Rezervacija>) rezervacijaRepository.findAll();
		ArrayList<Sto> zauzetiStolovi = new ArrayList<Sto>();
		for(Rezervacija rez: allRez){
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date rezDateStart = null;
			try {
				rezDateStart = sdf.parse(rez.getVreme());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ResponseEntity(HttpStatus.BAD_REQUEST);

			}
			if(rezDateStart == null){
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			
			if(isZauzet(rezDateStart, rez.getTrajanje(), targetDateStart, trajanje)){
				zauzetiStolovi.addAll(rez.getStolovi());
			}
		}
		
		return new ResponseEntity(zauzetiStolovi, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getPosete/{id}")
	public ResponseEntity getPosete(@PathVariable("id") int rezId){
		Rezervacija rez = rezervacijaRepository.findOne(rezId);
		if(rez == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}else{
			return new ResponseEntity(rez.getPosete(), HttpStatus.OK);
		}
		
	}
	
	public boolean isZauzet(Date rezDateStart, double rezTrajanje, Date targetDateStart, double targetTrajanje){
		
		//rezDateStart is -1h from me
		long rezDateStartMs = rezDateStart.getTime();
		rezDateStart = new Date(rezDateStartMs + 60*MINUTE_IN_MILLS);
		rezDateStartMs = rezDateStart.getTime();
		Double min = (rezTrajanje *60* MINUTE_IN_MILLS);
		int minInt = min.intValue();
		Date rezDateEnd =new Date(rezDateStartMs +minInt);
		
		
		long targetDateStartMs = targetDateStart.getTime();
		Double targetMin = targetTrajanje*60*MINUTE_IN_MILLS;
		int targetMinInt = targetMin.intValue();
		Date targetEndDate = new Date(targetDateStartMs+targetMinInt);
		
		if(targetDateStart.after(rezDateStart) && targetDateStart.before(rezDateEnd)){
			return true;
		}
		if(targetEndDate.after(rezDateStart) && targetEndDate.before(rezDateEnd)){
			return true;
		}
		
		if(targetDateStart.equals(rezDateStart) && targetDateStart.before(rezDateEnd)){
			return true;
		}
		if(targetEndDate.after(rezDateStart) && targetEndDate.equals(rezDateEnd)){
			return true;
		}
		
		if(targetDateStart.equals(rezDateStart) && targetEndDate.equals(rezDateEnd)){
			return true;
		}
			
		
		return false;
		
	}
	
	
}
