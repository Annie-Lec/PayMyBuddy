package fr.annielec.paymybuddy.web;

import java.util.List;

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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.annielec.paymybuddy.entities.AppUser;
import fr.annielec.paymybuddy.entities.BuddyAccount;
import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.entities.Transaction;
import fr.annielec.paymybuddy.entities.TypeContact;
import fr.annielec.paymybuddy.entities.TypeTransaction;
import fr.annielec.paymybuddy.exception.AlreadyExistsException;
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
	public String listeContactsU(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		String pseudo = buddyService.retrievePseudoWithIdUser(IdUser);

		List<BuddyUser> listBUC = buddyService.findBuddyUserContactForAPseudo(pseudo);
		Page<BuddyUser> pageBUC = buddyService.findBuddyUserContactForAPseudo(pseudo, PageRequest.of(page, size));
		model.addAttribute("listContacts", pageBUC.getContent());
		model.addAttribute("currentPage", pageBUC.getNumber() + 1);
		model.addAttribute("totalItems", listBUC.size());
		model.addAttribute("totalPages", Math.round(listBUC.size() / size) + 1);
		model.addAttribute("page", page);

		// Long idd = pageBUC.getContent().get(0).getContacts().get(0).getIdContact();
		// Long id = pageBUC.getContent().get(0).getContacts().get(0).getId();
		// System.out.println("idContact : " + idd + " id de l enreg du Contact : " +
		// id);

		model.addAttribute("pageSize", size);

		return "Contact";
	}

//**************************************************************************************************/
	@GetMapping("/register")
	public String save(Model model, @Valid AppUser appUser, BindingResult bindingResult,
			@RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "") String password,
			@RequestParam(defaultValue = "") String verifyPwd) {

		if (bindingResult.hasErrors()) {
			return "/register";
		} else {

			appUser = securityService.saveNewUser(username, password, verifyPwd);
			securityService.AddBuddyUserToUser(username);

			return "redirect:/login";
		}
	}

	@GetMapping("/login")
	public String connect(Model model, @Valid AppUser appUser, BindingResult bindingResult,
			@RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "") String password) {

		if (bindingResult.hasErrors()) {
			return "/login";
		} else {
			return "redirect:/contacts";
		}
	}

	@GetMapping("/newContact")
	public String newContact() {

		return "newContact";

	}

	// ajout d 'un contact
	
	@PostMapping("/addnewContact")
	public String addnewContact(Model model, @RequestParam(defaultValue = "") String pseudoContact,
			@RequestParam(defaultValue = "") String emailContact)  {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		String pseudoBU = buddyService.retrievePseudoWithIdUser(IdUser);
		String exists = TypeContact.NEW_CONTACT.toString();

		model.addAttribute("emailContact", emailContact);
		model.addAttribute("pseudoContact", pseudoContact);
		
		model.addAttribute("exists", exists);

		if (!pseudoContact.isEmpty()) {
			exists = buddyService.addContactsToBuddyUser(pseudoBU, pseudoContact);
		} else if (!emailContact.isEmpty()) {
			exists = buddyService.addContactsToBuddyUserByEMail(pseudoBU, emailContact);
		}

		
		if (exists.equals(TypeContact.ALREADY_EXISTS.toString())) {
			model.addAttribute("contactDetailResponse", "Already");
		} else if (exists.equals(TypeContact.NOT_IN_BDD.toString())){
			model.addAttribute("contactDetailResponse", "NoData");
		} 
		
		
		if (exists.equals(TypeContact.NEW_CONTACT.toString()))
			return "redirect:/contacts";
		else
			return "newContact";
	}


	@GetMapping(value = "/logout")
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	// affiche mes info de profil
	@GetMapping("/watchmyprofile")
	public String monProfil(Model model) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);

		BuddyUser bu = buddyService.findBuddyUserById(IdUser);
		model.addAttribute("buddyUser", bu);

		return "Profile";

	}

	// sauve mes info de profil
	@PostMapping("/savemyprofile")
	public String savemonProfil(Model model, @Valid BuddyUser buddyUser, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "Profile";
		} else {
			buddyService.saveBuddyUser(buddyUser);

			return "redirect:/contacts";
		}

	}

	// affiche mes info de profil : buddy Account
	@GetMapping("/watchmybudyAccount")
	public String myBuddyAccount(Model model) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);

		String pseudo = buddyService.retrievePseudoWithIdUser(IdUser);

		BuddyAccount ba = buddyService.findAccountByPseudo(pseudo);
		model.addAttribute("buddyAccount", ba);
		
		if (ba.getBankName()==null || ba.getIban()==null) {
			model.addAttribute("bankDetailResponse", "NoData");
		} else {
			model.addAttribute("bankDetailResponse", "Data");
		}
		
		return "buddyAccount";

	}

	@GetMapping("/chargebuddyAccount")
	public String chargemyBuddyAccount(Model model) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);

		String pseudo = buddyService.retrievePseudoWithIdUser(IdUser);

		BuddyAccount ba = buddyService.findAccountByPseudo(pseudo);
		model.addAttribute("buddyAccount", ba);
	
		return "chargebuddyAccount";
		

	}

	@GetMapping("/chargebankAccount")
	public String chargemyBankAccount(Model model) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);

		String pseudo = buddyService.retrievePseudoWithIdUser(IdUser);

		BuddyAccount ba = buddyService.findAccountByPseudo(pseudo);
		model.addAttribute("buddyAccount", ba);
		return "chargebankAccount";

	}

	// sauve mes info de compte
	@PostMapping("/savemybudyAccount")
	public String savemyBuddyAccount(Model model, @Valid BuddyAccount buddyAccount, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "buddyAccount";
		} else {
			buddyService.saveBuddyAccount(buddyAccount);
			return "redirect:/watchmybudyAccount";
		}
	}

	@PostMapping("/creditMyBuddyAccount")
	public String creditMyAccount(Model model, @Valid BuddyAccount buddyAccount, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "chargebuddyAccount";
		} else {
			double resultat = buddyAccount.getBalance() + buddyAccount.getAmountToCharge();
			buddyService.addTransfersToBuddyUserFromAccount(buddyAccount.getId(),buddyAccount.getAmountToCharge() , "credit from my bank", TypeTransaction.SELFSUPPLY);
			
			buddyAccount.setBalance(resultat);
			buddyAccount.setAmountToCharge(0);
			buddyService.saveBuddyAccount(buddyAccount);

			return "redirect:/watchmybudyAccount";
		}

	}

	@PostMapping("/debitMyBuddyAccount")
	public String debitMyAccount(Model model, @Valid BuddyAccount buddyAccount, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "chargebankAccount";
		} else {
			double resultat = buddyAccount.getBalance() - buddyAccount.getAmountToCharge();
			buddyService.addTransfersToBuddyUserForBankAccount(buddyAccount.getId(), buddyAccount.getAmountToCharge(), "Transfer to my bank account", TypeTransaction.SELFTRANSFER);
			buddyAccount.setBalance(resultat);
			buddyAccount.setAmountToCharge(0);
			buddyService.saveBuddyAccount(buddyAccount);

			return "redirect:/watchmybudyAccount";
		}

	}

	// affiche liste des transactions avec pagination
	@GetMapping("/transfers")
	public String listeTransfertsU(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		String pseudo = buddyService.retrievePseudoWithIdUser(IdUser);

		List<Transaction> listBUT = buddyService.findBuddyUserTransactionForAPseudo(pseudo);
		Page<Transaction> pageBUT = buddyService.findBuddyUserTransactionForAPseudoPage(pseudo,
				PageRequest.of(page, size));
		model.addAttribute("listTransactions", pageBUT.getContent());
		model.addAttribute("currentPage", pageBUT.getNumber() + 1);
		model.addAttribute("totalItems", listBUT.size());
		model.addAttribute("totalPages", Math.round(listBUT.size() / size) + 1);
		model.addAttribute("page", page);

		model.addAttribute("pageSize", size);

		return "Transfert";
	}

	@GetMapping("/newTransfer")
	public String newTransfert(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		String pseudoBU = buddyService.retrievePseudoWithIdUser(IdUser);

		List<String> listPseudoContactBU = buddyService.findPseudoBuddyUserContactForAPseudo(pseudoBU);
		model.addAttribute("listContacts", listPseudoContactBU);

		return "newTransfert";

	}

	// sauve mes info de profil
	@PostMapping("/addnewTransfer")
	public String addnewTransfert(Model model, @RequestParam(defaultValue = "") String pseudoContact,
			@RequestParam(defaultValue = "") String amount, @RequestParam(defaultValue = "") String description) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		String pseudoBU = buddyService.retrievePseudoWithIdUser(IdUser);

		boolean isBalanceSufficient = false;

		model.addAttribute("pseudoContact", pseudoContact);
		model.addAttribute("amount", amount);
		model.addAttribute("description", description);

		try {

			isBalanceSufficient = buddyService.addTransfersToBuddyUser(pseudoBU, pseudoContact,
					Double.parseDouble(amount), description);
			if (!isBalanceSufficient) {
				return "newTransfert";
			} else {
				return "redirect:/transfers";
			}
		} catch (Exception e) {
			return "newTransfert";
		}

	}

}
