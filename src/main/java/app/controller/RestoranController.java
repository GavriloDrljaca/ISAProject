package app.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.Jelo;
import app.model.Restoran;
import app.model.Sto;
import app.repository.JeloRepository;
import app.repository.RestoranRepository;
import app.repository.StoRepository;

@RestController
@RequestMapping("/restaurants")
public class RestoranController {

	@Autowired
	RestoranRepository restoranRepository;
	
	@Autowired
	JeloRepository jeloRepository;
	
	@Autowired
	StoRepository stoRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity getRestorani(){
		return new ResponseEntity(restoranRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createRestoran(@RequestBody Restoran restoran){
        Set<Jelo> jelovnik = addJela(restoran);
        restoran.setJelovnik(jelovnik);

        restoranRepository.save(restoran);
        final Set<Jelo> updatedJelovnik = restoran.getJelovnik();
		updatedJelovnik.forEach(jelo-> jelo.setRestoran(restoran));
		jeloRepository.save(updatedJelovnik);
		
		final Set<Sto> updateStolovi = restoran.getStolovi();
		updateStolovi.forEach(sto-> sto.setRestoran(restoran));
		stoRepository.save(updateStolovi);
        return new ResponseEntity<>(restoran.getId(), HttpStatus.CREATED);
    }
	
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
	@RequestMapping(method = RequestMethod.DELETE, value = "/jelo/{id}")
	public ResponseEntity deleteJelo(@PathVariable("id") int id){
		Jelo jelo = jeloRepository.findOne(id);
		jeloRepository.delete(jelo);
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
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
