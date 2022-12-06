package fr.annielec.paymybuddy.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import fr.annielec.paymybuddy.service.SecurityService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PayMyBuddyController {
	
	SecurityService securityService;

	@GetMapping("/")
	public String home() {

		return "Home";
	}

}
