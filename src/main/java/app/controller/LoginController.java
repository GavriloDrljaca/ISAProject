package app.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.User;
import app.repository.UserRepository;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(method = RequestMethod.POST, value = "/loginUser", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity login(HttpServletRequest request, HttpServletResponse response){
		
		String user = request.getParameter("username");
		String password = request.getParameter("password");
		User userTry = userRepository.findByUsername(user);
		
		if(userTry==null){

			return new ResponseEntity("LOGIN_FAILED", HttpStatus.OK);
		}else{
			if(userTry.getPassword().equals(password)){
					final Collection<GrantedAuthority> authorities = new ArrayList<>();
					authorities.add(new SimpleGrantedAuthority(userTry.getRole().name()));
					final Authentication authentication = new PreAuthenticatedAuthenticationToken(userTry, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
					return new ResponseEntity(HttpStatus.OK);
			}else{
				return new ResponseEntity("LOGIN_FAILED", HttpStatus.OK);
			}
		}
		
		
		
	}
}
