package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity getUsers(){
		return new ResponseEntity(userRepository.findAll(), HttpStatus.OK);
	}
	
	
	@RequestMapping(method = RequestMethod.POST , value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity login(){
		
		return new ResponseEntity("SHALALALAL", HttpStatus.OK);
	}
}
