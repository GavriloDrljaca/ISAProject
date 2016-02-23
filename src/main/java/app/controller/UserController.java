package app.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.model.Poseta;
import app.model.Poseta.Status;
import app.model.Restoran;
import app.model.Rezervacija;
import app.model.User;
import app.model.User.Role;
import app.repository.PosetaRepository;
import app.repository.RezervacijaRepository;
import app.repository.UserRepository;
import app.services.SmtpMailSender;
import src.main.java.util.HrFileUtil;

@RestController
@RequestMapping("/users")
public class UserController {
	
	final String BASE_DIRECTORY = System.getProperty("user.home");
    final String LOCAL_PATH = "/ISA_Restoran/images/users/";
    final String IMAGE_PATH = BASE_DIRECTORY + LOCAL_PATH;
    final String IMAGE_FORMAT = "jpg";
    
	@Autowired
	UserRepository userRepository;

	@Autowired
	PosetaRepository posetaRepository;

	@Autowired
	RezervacijaRepository rezervacijaRepository;
	@Autowired
	private SmtpMailSender smtpMailSender;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity getUsers() {
		return new ResponseEntity(userRepository.findAll(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity getUser(@PathVariable("id") int id) {
		return new ResponseEntity(userRepository.findOne(id), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity login() {

		return new ResponseEntity("SHALALALAL", HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity updateUser(@PathVariable("id") int id, @RequestBody User update) {
		User user = userRepository.findOne(id);
		if (user == null || user.getId() != update.getId()) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		update.setFriends(user.getFriends());

		final Set<User> updatedFriends = update.getFriends();
		if(updatedFriends!= null){
			userRepository.save(updatedFriends);
		}
		
		final Set<Poseta> updatedPosete = update.getPosete();
		if(updatedPosete != null){
			updatedPosete.forEach(p -> p.setUser(update));
			posetaRepository.save(updatedPosete);
		}
		
		
		final Set<Rezervacija> rezervacije = update.getRezervacije();
		if(rezervacije != null){
			rezervacije.forEach(r -> r.setUser(update));
			rezervacijaRepository.save(rezervacije);
		}
		
		userRepository.save(update);

		return new ResponseEntity(HttpStatus.OK);

	}
	@PreAuthorize("hasRole('GOST')")
	@RequestMapping(method = RequestMethod.POST, value = "addFriend/{userId}/{friendId}")
	public ResponseEntity addFriend(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId) {
		User user = userRepository.findOne(userId);
		if (user == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		User friend = userRepository.findOne(friendId);
		if (friend == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		user.getFriends().add(friend);
		userRepository.save(user);
		// DOES FRIEND REQUEST should be from both sides automatically
		// friend.getFriends().add(user);
		// userRepository.save(friend);

		return new ResponseEntity(HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('GOST')")
	@RequestMapping(method = RequestMethod.DELETE, value = "deleteFriend/{userId}/{friendId}")
	public ResponseEntity deleteFriend(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId) {
		User user = userRepository.findOne(userId);
		if (user == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		User friend = userRepository.findOne(friendId);
		if (friend == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		user.getFriends().remove(friend);
		userRepository.save(user);
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.GET, value = "nonFriends/{id}")
	public ResponseEntity getNonFriends(@PathVariable("id") int id) {
		User user = userRepository.findOne(id);
		if (user == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		Set<User> userFriends = user.getFriends();
		List<User> allUsers = userRepository.findAll();
		Set<User> nonFriends = new HashSet<User>();

		for (User u : allUsers) {
			boolean inFriends = false;
			for (User f : userFriends) {
				if (f.getId() == u.getId() || u.getId() == user.getId()) {
					inFriends = true;
				}
			}
			if (!inFriends) {
				if(u.getRole().equals(Role.GOST) && u.getId() != user.getId()){
					nonFriends.add(u);
				}
				
			}
		}

		return new ResponseEntity(nonFriends, HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.GET, value = "friends/{id}")
	public ResponseEntity getFriends(@PathVariable("id") int id) {
		User user = userRepository.findOne(id);
		if (user == null || user.getFriends() == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity(user.getFriends(), HttpStatus.OK);

	}
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.GET, value = "rezervacije/{id}")
	public ResponseEntity getUserReservations(@PathVariable("id") int id) {
		User user = userRepository.findOne(id);

		if (user == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		Set<Rezervacija> retVal = new HashSet<Rezervacija>();
		ArrayList<Rezervacija> allRez = (ArrayList<Rezervacija>) rezervacijaRepository.findAll();

		for (Rezervacija rez : allRez) {
			if (rez.getUser().getId() == user.getId()) {
				retVal.add(rez);
			}

		}

		return new ResponseEntity(retVal, HttpStatus.OK);
	}
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.GET, value = "posete/{id}")
	public ResponseEntity getUserVisits(@PathVariable("id") int id) {
		User user = userRepository.findOne(id);

		if (user == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		Set<Poseta> retVal = new HashSet<Poseta>();
		ArrayList<Poseta> allPos = (ArrayList<Poseta>) posetaRepository.findAll();

		for (Poseta pos : allPos) {
			if (pos.getUser().getId() == user.getId()) {
				retVal.add(pos);
			}
		}
		return new ResponseEntity(retVal, HttpStatus.OK);

	}
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.PUT, value = "/poseta/")
	public ResponseEntity updatePoseta(@RequestBody Poseta update) {
		if (update == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);

		}
		Poseta poseta = posetaRepository.findOne(update.getId());
		if (poseta == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);

		}

		posetaRepository.save(update);

		return new ResponseEntity(HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.POST, value = "/poseta/{rezerId}/{userId}")
	public ResponseEntity createPoseta(@PathVariable("rezerId") int rezId, @PathVariable("userId") int userId) {
		User user = userRepository.findOne(userId);
		if (user == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		Rezervacija rezervacija = rezervacijaRepository.findOne(rezId);
		if (rezervacija == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		Poseta poseta = new Poseta();
		poseta.setStatus(Status.NEODLUCENO);
		poseta.setUser(user);
		poseta.setRezervacija(rezervacija);

		posetaRepository.save(poseta);

		// SEND MAIL TO friend
		String mailText = "<html><body> "
				+ "Postovani, " + user.getName() + ", dobili ste pozivnicu za rucak od : "
				+  rezervacija.getUser().getName()
				+ ". Posetite stranicu <a href = 'http://localhost:8080/#/user'>Vaseg naloga</a> i tab Pozivnice na nasem sajtu kako bi ste potvrdili ili odbili poziv. "
				+ "\n Hvala.  "
				+ "</body>"
				+ " </html>";
				
		try {
			smtpMailSender.send(user.getEmail(), "Pozivnica za posetu restoranu",mailText);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity("TRY_AGAIN_LATER", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(HttpStatus.OK);

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/isFriend/{userId}/{friendId}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity isFriend(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId) {
		User user = userRepository.findOne(userId);
		if (user == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		User friend = userRepository.findOne(friendId);
		if (friend == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		if (user.getFriends().contains(friend)) {
			return new ResponseEntity("FRIEND", HttpStatus.OK);
		} else {
			return new ResponseEntity("NOT_FRIEND", HttpStatus.OK);
		}

	}
	@PreAuthorize("hasRole('MENADZER_SISTEMA')")
	@RequestMapping(method = RequestMethod.POST, value = "/createManager/", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity createManager(@RequestBody User user) {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			return new ResponseEntity("USER_EXISTS", HttpStatus.BAD_REQUEST);
		}
		user.setActivated(true);
		user.setRole(Role.MENADZER_RESTORANA);
		Restoran restoran = user.getRestoran();
		if (restoran == null) {
			return new ResponseEntity("NO_RESTORAN", HttpStatus.BAD_REQUEST);
		}
		userRepository.save(user);
		return new ResponseEntity("MANAGER_CREATED", HttpStatus.OK);
	}
	
	 	@RequestMapping(value="/{id}/image", method=RequestMethod.POST)
	    public ResponseEntity uploadUserImage(@PathVariable("id")int id, @RequestParam("file") MultipartFile file){
	        final User user = userRepository.findOne(id);
	        if (user == null){
	            return new ResponseEntity(HttpStatus.NOT_FOUND);
	        }

	        try {
	            final BufferedImage image = ImageIO.read(file.getInputStream());
	            final BufferedImage scaledImage = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, 175, 205, Scalr.OP_ANTIALIAS);
	            final String fileName = user.getId() + "." + IMAGE_FORMAT;

	            //If not exist create folder for images
	            HrFileUtil.createFolderIfNotExist(IMAGE_PATH);
	            final File imageFile = new File(IMAGE_PATH + fileName);
	            if (!imageFile.exists()){
	                imageFile.createNewFile();
	            }
	            ImageIO.write(scaledImage, IMAGE_FORMAT, imageFile);

	            user.setPhotoUrl("users/" + user.getId() + "/image");
	            userRepository.save(user);
	            return new ResponseEntity(HttpStatus.OK);
	        } catch (IOException e) {
	            return new ResponseEntity(HttpStatus.BAD_REQUEST);
	        }
	    }
	 	
	 	@RequestMapping(value="/{id}/image", method=RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	    public ResponseEntity getProfileImage(@PathVariable("id") int id){
	        final String fileName = id + "." + IMAGE_FORMAT;
	        final File file = new File(IMAGE_PATH + fileName);

	        if (!file.exists()){
	            return new ResponseEntity(HttpStatus.NOT_FOUND);
	        }

	        try {
	            final BufferedImage image = ImageIO.read(file);
	            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	            ImageIO.write(image, IMAGE_FORMAT, byteArrayOutputStream);
	            return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), HttpStatus.OK);
	        } catch (IOException e) {
	            return new ResponseEntity(HttpStatus.NOT_FOUND);
	        }
	    }

}
