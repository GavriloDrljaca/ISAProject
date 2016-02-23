package app.controller;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.User;
import app.repository.UserRepository;
import app.services.SmtpMailSender;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	private SmtpMailSender smtpMailSender;
	private SecureRandom random = new SecureRandom();

	@RequestMapping(method = RequestMethod.POST, value = "/loginUser/{username}/{password}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity login(@PathVariable("username") String username, @PathVariable("password") String password) {

		System.out.println("user:" + username);
		User userTry = userRepository.findByUsername(username);

		if (userTry == null) {
			return new ResponseEntity("NO_USER", HttpStatus.BAD_REQUEST);
		} else {
			if (userTry.getPassword().equals(password)) {
				if (!userTry.isActivated()) {
					return new ResponseEntity("USER_NOT_ACTIVATED", HttpStatus.BAD_REQUEST);
				}
				final Collection<GrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority(userTry.getRole().name()));
				final Authentication authentication = new PreAuthenticatedAuthenticationToken(userTry, null,
						authorities);
				SecurityContextHolder.getContext().setAuthentication(authentication);

				return new ResponseEntity(HttpStatus.OK);
			} else {
				return new ResponseEntity("BAD_PASSWORD", HttpStatus.BAD_REQUEST);
			}
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/registerUser", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity registerUser(@RequestBody User user){
		if(userRepository.findByUsername(user.getUsername()) !=null){
			return new ResponseEntity("USER_EXISTS", HttpStatus.BAD_REQUEST);
		}
		
		//Generate token
		String token = new BigInteger(130, random).toString(32);
		user.setActivated(false);
		user.setRegistrationKey(token);
		String textMail = "<html><body>"
				+ "Postovani, "+user.getName()
				+", molimo vas da aktivirate vas nalog sa korisnickim imenom : "+user.getUsername()+
				" i registracionim tokenom: "+user.getRegistrationKey() 
				+"  .Pratite <a href = 'http://localhost:8080/#/login'>LINK</a> i izaberite opciju Aktiviraj nalog"
				+ " .Hvala, Uzivajte u nasoj aplikaciji!"
				+ "</body></html>";
		try {
			smtpMailSender.send(user.getEmail(), "Jedite Sa Nama aktivacioni token", textMail);			
			userRepository.save(user);
			return new ResponseEntity(HttpStatus.CREATED);
		} catch (MessagingException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity("TRY_AGAIN_LATER", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
	}

	@RequestMapping(method = RequestMethod.POST, value = "/activateUser/{username}/{activationToken}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity activateUser(@PathVariable("username") String username,
			@PathVariable("activationToken") String token) {
		User foundUser = userRepository.findByUsername(username);

		if (foundUser == null) {
			return new ResponseEntity("NO_USERNAME", HttpStatus.BAD_REQUEST);
		}

		if (!foundUser.getRegistrationKey().equals(token)) {
			return new ResponseEntity("INVALID_TOKEN", HttpStatus.BAD_REQUEST);
		}

		foundUser.setActivated(true);
		userRepository.save(foundUser);
		return new ResponseEntity("USER_ACTIVATED", HttpStatus.OK);
	}

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(method = RequestMethod.GET, value = "/me")
	public ResponseEntity getProfile() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return new ResponseEntity<>(authentication.getPrincipal(), HttpStatus.OK);
	}
}
