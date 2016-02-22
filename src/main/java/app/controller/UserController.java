package app.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.Poseta;
import app.model.Rezervacija;
import app.model.User;
import app.repository.PosetaRepository;
import app.repository.RezervacijaRepository;
import app.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PosetaRepository posetaRepository;
	
	@Autowired
	RezervacijaRepository rezervacijaRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity getUsers(){
		return new ResponseEntity(userRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity getUser(@PathVariable("id") int id){
		return new ResponseEntity(userRepository.findOne(id), HttpStatus.OK);
	}
	
	
	@RequestMapping(method = RequestMethod.POST , value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity login(){
		
		return new ResponseEntity("SHALALALAL", HttpStatus.OK);
	}
	@RequestMapping(method = RequestMethod.PUT, value="/{id}")
	public ResponseEntity updateUser(@PathVariable("id") int id, @RequestBody User update){
		User user = userRepository.findOne(id);
		if(user == null || user.getId() != update.getId()){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		
		update.setFriends(user.getFriends());
		
		final Set<User> updatedFriends = update.getFriends();
		userRepository.save(updatedFriends);
		
		final Set<Poseta> updatedPosete = update.getPosete();
		updatedPosete.forEach(p-> p.setUser(update));
		posetaRepository.save(updatedPosete);
		
		final Set<Rezervacija> rezervacije = update.getRezervacije();
		rezervacije.forEach(r -> r.setUser(update));
		rezervacijaRepository.save(rezervacije);
		
		userRepository.save(update);
		
		return new ResponseEntity(HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "addFriend/{userId}/{friendId}")
	public ResponseEntity addFriend(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId){
		User user = userRepository.findOne(userId);
		if(user == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		User friend = userRepository.findOne(friendId);
		if(friend == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		user.getFriends().add(friend);
		userRepository.save(user);
		//DOES FRIEND REQUEST should be from both sides automatically
		//friend.getFriends().add(user);
		//userRepository.save(friend);
				
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "deleteFriend/{userId}/{friendId}")
	public ResponseEntity deleteFriend(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId){
		User user = userRepository.findOne(userId);
		if(user == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		User friend = userRepository.findOne(friendId);
		if(friend == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		user.getFriends().remove(friend);
		userRepository.save(user);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "nonFriends/{id}")
	public ResponseEntity getNonFriends(@PathVariable("id") int id){
		User user = userRepository.findOne(id);
		if(user == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		Set<User> userFriends = user.getFriends();
		List<User> allUsers = userRepository.findAll();
		Set<User> nonFriends = new HashSet<User>();
		
		for(User u: allUsers){
			boolean inFriends = false;
			for(User f: userFriends){
				if(f.getId() == u.getId() || u.getId() == user.getId()){
					inFriends = true;
				}
			}
			if(!inFriends){
				nonFriends.add(u);
			}
		}
		
		return new ResponseEntity(nonFriends, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "friends/{id}")
	public ResponseEntity getFriends(@PathVariable("id") int id){
		User user = userRepository.findOne(id);
		if(user == null || user.getFriends() == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity(user.getFriends(), HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "rezervacije/{id}")
	public ResponseEntity getUserReservations(@PathVariable("id") int id){
		User user = userRepository.findOne(id);
		
		if(user == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		Set<Rezervacija> retVal = new HashSet<Rezervacija>();
		ArrayList<Rezervacija> allRez = (ArrayList<Rezervacija>) rezervacijaRepository.findAll();
		
		for(Rezervacija rez : allRez){
			if(rez.getUser().getId() == user.getId());
			retVal.add(rez);
		}
		
		return new ResponseEntity(retVal, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "posete/{id}")
	public ResponseEntity getUserVisits(@PathVariable("id") int id){
		User user = userRepository.findOne(id);
		
		if(user == null){
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		Set<Poseta> retVal = new HashSet<Poseta>();
		ArrayList<Poseta> allPos = (ArrayList<Poseta>) posetaRepository.findAll();
		
		for(Poseta pos: allPos){
			if(pos.getUser().getId() == user.getId()){
				retVal.add(pos);
			}
		}
		return new ResponseEntity(retVal, HttpStatus.OK);

	}
	
}
