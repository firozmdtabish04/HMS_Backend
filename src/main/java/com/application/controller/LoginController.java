package com.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.application.model.AuthRequest;
import com.application.model.Doctor;
import com.application.model.User;
import com.application.service.DoctorRegistrationService;
import com.application.service.UserRegistrationService;
import com.application.util.JwtUtils;

@RestController
@CrossOrigin(origins = "*") // ✅ IMPORTANT
public class LoginController {

	@Autowired
	private UserRegistrationService userRegisterService;

	@Autowired
	private JwtUtils jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private DoctorRegistrationService doctorRegisterService;

	// ================= HOME =================
	@GetMapping("/")
	public String welcomeMessage() {
		return "Welcome to HealthCare Management system !!!";
	}

	// ================= JWT LOGIN =================
	@PostMapping("/authenticate")
	public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) {

		try {
			List<User> users = userRegisterService.getAllUsers();
			String currentEmail = "";

			for (User obj : users) {
				if (obj.getEmail().equalsIgnoreCase(authRequest.getEmail())) {
					currentEmail = obj.getUsername();
				}
			}

			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(currentEmail, authRequest.getPassword()));

		} catch (Exception ex) {
			return new ResponseEntity<>("Invalid Username/password", HttpStatus.UNAUTHORIZED);
		}

		String token = jwtUtil.generateToken(authRequest.getEmail());
		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	// ================= USER LOGIN =================
	@PostMapping("/loginuser")
	public ResponseEntity<?> loginUser(@RequestBody User user) {

		User userObj = userRegisterService
				.fetchUserByEmailAndPassword(user.getEmail(), user.getPassword());

		if (userObj == null) {
			return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>(userObj, HttpStatus.OK);
	}

	// ================= DOCTOR LOGIN =================
	@PostMapping("/logindoctor")
	public ResponseEntity<?> loginDoctor(@RequestBody Doctor doctor) {

		Doctor doctorObj = doctorRegisterService
				.fetchDoctorByEmailAndPassword(doctor.getEmail(), doctor.getPassword());

		if (doctorObj == null) {
			return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<>(doctorObj, HttpStatus.OK);
	}
}