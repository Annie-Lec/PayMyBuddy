package fr.annielec.paymybuddy.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import fr.annielec.paymybuddy.configuration.UtilityConfig;
import fr.annielec.paymybuddy.entities.AppUser;
import fr.annielec.paymybuddy.entities.BuddyAccount;
import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.entities.Transaction;
import fr.annielec.paymybuddy.entities.TypeContact;
import fr.annielec.paymybuddy.entities.TypeTransaction;
import fr.annielec.paymybuddy.service.BuddyAccountService;
import fr.annielec.paymybuddy.service.BuddyUserService;
import fr.annielec.paymybuddy.service.PayMyBuddyService;
import fr.annielec.paymybuddy.service.SecurityService;
import fr.annielec.paymybuddy.service.UserDetailsServiceImpl;
import fr.annielec.paymybuddy.util.Operations;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@AllArgsConstructor
public class PayMyBuddyController {

	SecurityService securityService;
	PayMyBuddyService buddyService;
	BuddyUserService buddyUserService;
	BuddyAccountService accountService;
	UserDetailsServiceImpl userDetailsServiceImpl;
	UtilityConfig utilityConfig;
	private Operations ope;

	@GetMapping("/")
	public String home() {

		return "Home";
	}

	// affiche liste des contacts avec pagination
	@GetMapping("/contacts")
	public String listContactsU(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		String pseudo = buddyUserService.retrievePseudoWithIdUser(IdUser);

		List<BuddyUser> listBUC = buddyService.findBuddyUserContactForAPseudo(pseudo);
		Page<BuddyUser> pageBUC = buddyService.findBuddyUserContactForAPseudo(pseudo, PageRequest.of(page, size));
		model.addAttribute("listContacts", pageBUC.getContent());
		model.addAttribute("currentPage", pageBUC.getNumber() + 1);
		model.addAttribute("totalItems", listBUC.size());
		model.addAttribute("totalPages", Math.round(listBUC.size() / size) + 1);
		model.addAttribute("page", page);

		model.addAttribute("pageSize", size);
		
		log.info("Display Page Contact");

		return "Contact";
	}

	@GetMapping("/register")
	public String addnewUser(Model model, @Valid AppUser appUser, BindingResult bindingResult,
			@RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "") String password,
			@RequestParam(defaultValue = "") String verifyPwd) {

		AppUser appUserEnBase = null;
		try {
			appUserEnBase = securityService.loadUserByUsername(username);
			if (appUserEnBase != null) {
				model.addAttribute("emailexistbdd", "yes");
				log.error("Display Page register : user already exists");

				return "/register";
			}
		} catch (NullPointerException e) {
			log.info("Mapping register OK");
			System.out.println("app user pas encore en base, tant mieux");
		}

		if (bindingResult.hasErrors()) {
			log.error("Error during register : problem of data validation");
			return "/register";
		} else if (!password.equals(verifyPwd)) {
			log.error("Error during register : problem of password");
			model.addAttribute("verifyPwdReponse", "ko");
			return "/register";

		} else {
			appUser = securityService.saveNewUser(username, password, verifyPwd);
			securityService.AddBuddyUserToUser(username);
			log.info("Mapping register OK : redirect on login page");
			return "redirect:/login";
		}

	}

	@GetMapping("/login")
	public String connect(Model model, @Valid AppUser appUser, BindingResult bindingResult,
			@RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "") String password) {
		log.info("Display login page");
		return "/login";
	}


	@GetMapping("/newContact")
	public String newContact() {
		log.info("Display newContact page");
		return "newContact";

	}

	// ajout d 'un contact
	@PostMapping("/addnewContact")
	public String addnewContact(Model model, @RequestParam(defaultValue = "") String pseudoContact,
			@RequestParam(defaultValue = "") String emailContact) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		String pseudoBU = buddyUserService.retrievePseudoWithIdUser(IdUser);
		String exists = TypeContact.NEW_CONTACT.toString();

		model.addAttribute("emailContact", emailContact);

		model.addAttribute("exists", exists);
		/**************************
		 * Mise à jour du Role et rafraichissement du context *********
		 ***/
		if (!emailContact.isEmpty()) {
			exists = buddyService.addContactsToBuddyUserByEMail(pseudoBU, emailContact);
			model.addAttribute("exists", exists);
			if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("3USER"))) {
				securityService.addRoleToUser(currentUserName, "3USER");

				utilityConfig.refreshContextWithNewRole(currentUserName, authentication, userDetailsServiceImpl);
			}

		}

		if (exists.equals(TypeContact.ALREADY_EXISTS.toString())) {
			log.error("Add A contact : " + exists );
			model.addAttribute("contactDetailResponse", "Already");
		} else if (exists.equals(TypeContact.NOT_IN_BDD.toString())) {
			log.error("Add A contact : " + exists );
			model.addAttribute("contactDetailResponse", "NoData");
		}
		if (exists.equals(TypeContact.NEW_CONTACT.toString())) {
			log.info("Add a new contact OK : Redirect:/contacts page ");
			return "redirect:/contacts";
		}
		else {
			log.info("Display newContact page");
			return "newContact";
		}
	}

	@GetMapping(value = "/logout")
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		log.info("Display logout page ");
		return "redirect:/login?logout";
	}

	// affiche mes info de profil
	@GetMapping("/watchmyprofile")
	public String myProfile(Model model) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);

		BuddyUser bu = buddyUserService.findBuddyUserById(IdUser);
		BuddyAccount ba = accountService.findBuddyAccountById(IdUser);

		model.addAttribute("buddyUser", bu);
		model.addAttribute("buddyAccount", ba);

		if (bu.getDateDenaissance() == null || bu.getFirstName() == null || bu.getLastName() == null) {
			log.error("Profile : missing data");
			model.addAttribute("profilDetailResponse", "noData");
		} else {
			model.addAttribute("profilDetailResponse", "data");
			log.info("Profile ok");
		}
		log.info("Display Profile page");
		return "Profile";

	}

	// sauve mes info de profil
	@PostMapping("/savemyprofile")
	public String savemyProfileWithAccount(Model model, @Valid BuddyUser buddyUser, BindingResult bindingResult,
			BuddyAccount buddyAccount) {

		if (bindingResult.hasErrors()) {
			log.error("Profile - data KO in buddyUser : Display Profile page");
			return "Profile";
		} else {
			buddyUser.setBuddyAccount(buddyAccount);
			buddyUserService.saveBuddyUser(buddyUser);
			log.info("Profile - Data OK : Display Profile page");
			return "redirect:/watchmyprofile";
		}

	}

	// affiche mes info de profil : buddy Account
	@GetMapping("/watchmybudyAccount")
	public String myBuddyAccount(Model model) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);

		BuddyAccount ba = accountService.findBuddyAccountById(IdUser);
		model.addAttribute("buddyAccount", ba);

		if (ba.getBankName() == null || ba.getIban() == null) {
			model.addAttribute("bankDetailResponse", "NoData");
			log.error("BuddyAccount : bank details missing");
		} else {
			model.addAttribute("bankDetailResponse", "Data");
		}
		log.info("Display buddyAccount page");
		return "buddyAccount";

	}

	@GetMapping("/chargebuddyAccount")
	public String chargemyBuddyAccount(Model model) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);

		BuddyAccount ba = accountService.findBuddyAccountById(IdUser);
		model.addAttribute("buddyAccount", ba);
		log.info("Display chargebuddyAccount page");
		return "chargebuddyAccount";

	}

	@GetMapping("/chargemybankAccount")
	public String chargemyBankAccount(Model model) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);

		BuddyAccount ba = accountService.findBuddyAccountById(IdUser);

		model.addAttribute("buddyAccount", ba);

		if ((ba.getBalance() - (1 + 0.05) * ba.getAmountToCharge()) < 0) {
			log.error("balance insufficient");
			model.addAttribute("reponseBalance", "ko");
		} else {
			model.addAttribute("reponseBalance", "ok");
		}
		log.info("Display chargebankAccount page");
		return "chargebankAccount";

	}

	// sauve mes info de compte
	@PostMapping("/savemybudyAccount")
	public String savemyBuddyAccount(Model model, @Valid BuddyAccount buddyAccount, BindingResult bindingResult) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = buddyUserService.retrievePseudoWithIdUser(buddyAccount.getId());

		if (bindingResult.hasErrors()) {
			log.error("BuddyAccount - impossible to Save : erreur validation data");
			return "buddyAccount";
		} else {
			accountService.saveBuddyAccount(buddyAccount);
			/******************* Mise à jour du Role et Gestion dynamique du context ****/
			if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("2CONTACT_TO_DEF"))) {

				securityService.addRoleToUser(username, "2CONTACT_TO_DEF");
				utilityConfig.refreshContextWithNewRole(username, authentication, userDetailsServiceImpl);

			}
			log.info("Display buddyAccount page");
			return "redirect:/watchmybudyAccount";
		}
	}

	@PostMapping("/creditMyBuddyAccount")
	public String creditMyAccount(Model model, @Valid BuddyAccount buddyAccount, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error("BuddyAccount - impossible to credit : erreur validation data");
			return "chargebuddyAccount";
		} else {
			double resultat = Math.round((buddyAccount.getBalance() + buddyAccount.getAmountToCharge()) * 100.0)
					/ 100.0;
			buddyService.addTransfersToBuddyUserFromAccount(buddyAccount.getId(), buddyAccount.getAmountToCharge(),
					"credit from my bank", TypeTransaction.SELFSUPPLY);

			buddyAccount.setBalance(resultat);
			buddyAccount.setAmountToCharge(0);
			accountService.saveBuddyAccount(buddyAccount);
			log.info("Display buddyAccount page");
			return "redirect:/watchmybudyAccount";
		}

	}

	@PostMapping("/debitMyBuddyAccount")
	public String debitMyAccount(Model model, @Valid BuddyAccount buddyAccount, BindingResult bindingResult) {

		if (bindingResult.hasErrors() || (Math.round((buddyAccount.getBalance() - buddyAccount.getAmountToCharge()
				- ope.calculFees(buddyAccount.getAmountToCharge())) * 100.0) / 100.0) < 0) {
			
			log.error("BuddyAccount - impossible to debit : erreur validation data");

			accountService.saveBuddyAccount(buddyAccount);
			return "redirect:/chargemybankAccount";

		} else {
			double resultat = Math.round((buddyAccount.getBalance() - buddyAccount.getAmountToCharge()
					- ope.calculFees(buddyAccount.getAmountToCharge())) * 100.0) / 100.0;
			buddyService.addTransfersForBuddyUserToBankAccount(buddyAccount.getId(), buddyAccount.getAmountToCharge(),
					"Transfer to my bank account", TypeTransaction.SELFTRANSFER);
			buddyAccount.setBalance(resultat);
			buddyAccount.setAmountToCharge(0);
			accountService.saveBuddyAccount(buddyAccount);
			log.info("Display buddyAccount page");
			return "redirect:/watchmybudyAccount";
		}

	}

	// affiche liste des transactions avec pagination
	@GetMapping("/transfers")
	public String listTransfertsU(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {
		// on recupere le username courant
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		String pseudo = buddyUserService.retrievePseudoWithIdUser(IdUser);

		List<Transaction> listBUT = buddyService.findBuddyUserTransactionForAPseudo(pseudo);
		Page<Transaction> pageBUT = buddyService.findBuddyUserTransactionForAPseudoPage(pseudo,
				PageRequest.of(page, size));
		model.addAttribute("listTransactions", pageBUT.getContent());
		model.addAttribute("currentPage", pageBUT.getNumber() + 1);
		model.addAttribute("totalItems", listBUT.size());
		model.addAttribute("totalPages", Math.round(listBUT.size() / size) + 1);
		model.addAttribute("page", page);

		model.addAttribute("pageSize", size);
		log.info("Display Transfert page");
		return "Transfert";
	}

	@GetMapping("/newTransfer")
	public String newTransfert(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		String pseudoBU = buddyUserService.retrievePseudoWithIdUser(IdUser);

		List<String> listPseudoContactBU = buddyService.findPseudoBuddyUserContactForAPseudo(pseudoBU);
		model.addAttribute("listContacts", listPseudoContactBU);
		log.info("Display NewTransfert page");
		return "newTransfert";

	}

	// sauve mes info de profil
	@PostMapping("/addnewTransfer")
	public String addnewTransfert(Model model, @RequestParam(defaultValue = "") String pseudoContact,
			@RequestParam(defaultValue = "") String amount, @RequestParam(defaultValue = "") String description) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserName = authentication.getName();
		Long IdUser = securityService.getId(currentUserName);
		String pseudoBU = buddyUserService.retrievePseudoWithIdUser(IdUser);

		boolean isBalanceSufficient = true;

		model.addAttribute("pseudoContact", pseudoContact);
		model.addAttribute("amount", amount);
		model.addAttribute("description", description);
		model.addAttribute("balanceResponse", "balanceOK");

		if (Double.parseDouble(amount) > 0) {
			try {

				isBalanceSufficient = buddyService.addTransfersToBuddyUser(pseudoBU, pseudoContact, Double.parseDouble(amount), description);
				if (!isBalanceSufficient) {
					model.addAttribute("balanceResponse", "balanceKO");
					List<String> listPseudoContactBU = buddyService.findPseudoBuddyUserContactForAPseudo(pseudoBU);
					model.addAttribute("listContacts", listPseudoContactBU);
					log.error("Transfer - Balance insufficent : Display newTransfert page");
					return "newTransfert";
				} else {
					log.info("Transfer completed");
					return "redirect:/transfers";
				}
			} catch (Exception e) {
				log.error("Transfer - Problem with beneficiary account : Display newTransfert page");
				return "newTransfert";
			}
		} else {
			log.error("Transfer - Amount ko : Display newTransfert page");
			return "newTransfert";
		}

	}

}
