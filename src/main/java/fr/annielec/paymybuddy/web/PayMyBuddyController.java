package fr.annielec.paymybuddy.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import fr.annielec.paymybuddy.entities.AppUser;
import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.service.PayMyBuddyService;
import fr.annielec.paymybuddy.service.SecurityService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PayMyBuddyController {

	SecurityService securityService;
	PayMyBuddyService buddyService;

	@GetMapping("/")
	public String home() {

		return "Home";
	}

	// affiche liste des contacts avec pagination
	@GetMapping("/contacts")
	public String listeContactsU(Model model, 
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "3") int size,
			@RequestParam(name = "keyword", defaultValue = "") String keyword) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		String pseudo = buddyService.retrievePseudoWithIdUser(IdUser);
		
		Page<BuddyUser> pageBUC = buddyService.findBuddyUserContactByPseudoContains(pseudo, keyword, PageRequest.of(page, size));
		model.addAttribute("listContacts", pageBUC.getContent());
		// stocker le nb de page
		model.addAttribute("pages", new int[pageBUC.getTotalPages()]);
		// stocke la page courante : pour la mise en forme du cout on selectionne en
		// cours
		model.addAttribute("currentPage", page);
		model.addAttribute("pseudo", pseudo);
		return "Contact";
	}
	
	
	@GetMapping("/register")
	public String save(Model model, 
			@Valid AppUser appUser, 
			BindingResult bindingResult,  
			@RequestParam(defaultValue = "") String username, 
			@RequestParam(defaultValue = "") String password,
			@RequestParam(defaultValue = "") String verifyPwd) {
	
	
		if (bindingResult.hasErrors()) {
			return "/register";
		} else {
			System.out.println("0");
			 appUser= 	securityService.saveNewUser( username,  password,  verifyPwd);
			 securityService.AddBuddyUserToUser(username);
			 					
			return "redirect:/login";
		}
	}
	
	@GetMapping("/login")
	public String connect(Model model, 
			@Valid AppUser appUser, 
			BindingResult bindingResult,  
			@RequestParam(defaultValue = "")String username, 
			@RequestParam(defaultValue = "") String password) {
	
	
		if (bindingResult.hasErrors()) {
			return "/login";
		} else {
			 
			return "redirect:/contacts";
		}
	}
	
	
	@GetMapping(value="/logout")
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/login?logout";
	}
	
	// affiche mes info de profil
	@GetMapping("/updatemyprofile")
	public String monProfil(Model model) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		
		
		BuddyUser bu = buddyService.findBuddyUserById(IdUser);
		model.addAttribute("buddyUser", bu);

//		String pseudo = buddyService.retrievePseudoWithIdUser(IdUser);
		
		
		//	buddyService.saveBuddyUser(bu);
					
			return "Profile";
		
	}
	
	// affiche mes info de profil
	@GetMapping("/savemyprofile")
	public String savemonProfil(Model model, @Valid BuddyUser buddyUser, BindingResult bindingResult) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		
		
		if (bindingResult.hasErrors()) {
			return "Profile";
		} else {
			buddyService.saveBuddyUser(buddyUser);
					
			return "redirect:/contacts";
		}
		
		
			
					
			
		
	}
	
	
	



}
