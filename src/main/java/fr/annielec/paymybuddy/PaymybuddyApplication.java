package fr.annielec.paymybuddy;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.annielec.paymybuddy.entities.BuddyAccount;
import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.service.BuddyAccountService;
import fr.annielec.paymybuddy.service.BuddyUserService;
//import fr.annielec.paymybuddy.entities.Transaction;
import fr.annielec.paymybuddy.service.PayMyBuddyService;
import fr.annielec.paymybuddy.service.SecurityService;

@SpringBootApplication
public class PaymybuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymybuddyApplication.class, args);
	}

	// BCrypt pour cryptage du mot de passe
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner start(SecurityService securityService, PayMyBuddyService buddyService, BuddyUserService buddyUserService, BuddyAccountService accountService) {
		return args -> {
			
			securityService.saveNewRole("1PROFIL_TO_DEF", "profil a renseigner");
			securityService.saveNewRole("2CONTACT_TO_DEF", "contact a renseigner");
			securityService.saveNewRole("3USER", "utilisateur ayant acces à toutes les fonctionnalites");


			BuddyAccount buddyAccountBank = new BuddyAccount();
			accountService.saveBuddyAccount(buddyAccountBank);
			BuddyUser bank = new BuddyUser();
			bank.setPseudo("Bank");
			bank.setFirstName("");
			bank.setLastName("");
			bank.setBuddyAccount(buddyAccountBank);
			buddyUserService.saveBuddyUser(bank);
			securityService.saveNewUser("bank@bank.com", "1234", "1234");
			securityService.AddBuddyUserToUser("bank@bank.com", "Bank");

			
			BuddyAccount buddyAccount1 = new BuddyAccount();
			BuddyAccount buddyAccount2 = new BuddyAccount();
			BuddyAccount buddyAccount3 = new BuddyAccount();
			BuddyAccount buddyAccount4 = new BuddyAccount();
			BuddyAccount buddyAccount5 = new BuddyAccount();
			BuddyAccount buddyAccount6 = new BuddyAccount();
			buddyAccount1.setBalance(120);
			buddyAccount6.setBalance(250);
			accountService.saveBuddyAccount(buddyAccount1);
			accountService.saveBuddyAccount(buddyAccount2);
			accountService.saveBuddyAccount(buddyAccount3);
			accountService.saveBuddyAccount(buddyAccount4);
			accountService.saveBuddyAccount(buddyAccount5);
			accountService.saveBuddyAccount(buddyAccount6);
			
			BuddyUser buddyUser1 = new BuddyUser();
			buddyUser1.setDateDenaissance(new Date());
			buddyUser1.setPseudo("roger@gmail.com");
			buddyUser1.setFirstName("Roger");
			buddyUser1.setLastName("Rabbit");
			buddyUser1.setBuddyAccount(buddyAccount1);
			buddyUserService.saveBuddyUser(buddyUser1);

			BuddyUser buddyUser2 = new BuddyUser();
			buddyUser2.setDateDenaissance(new Date());
			buddyUser2.setPseudo("buddy2@email.fr");
			buddyUser2.setFirstName("Bugs");
			buddyUser2.setLastName("Bunny");
			buddyUser2.setBuddyAccount(buddyAccount2);
			buddyUserService.saveBuddyUser(buddyUser2);

			BuddyUser buddyUser3 = new BuddyUser();
			buddyUser3.setDateDenaissance(new Date());
			buddyUser3.setPseudo("mickey@yahoo.fr");
			buddyUser3.setFirstName("Mike");
			buddyUser3.setLastName("Mouse");
			buddyUser3.setBuddyAccount(buddyAccount3);
			buddyUserService.saveBuddyUser(buddyUser3);

			BuddyUser buddyUser4 = new BuddyUser();
			buddyUser4.setDateDenaissance(new Date());
			buddyUser4.setPseudo("dodo@gmail.com");
			buddyUser4.setFirstName("Donald");
			buddyUser4.setLastName("Duck");
			buddyUser4.setBuddyAccount(buddyAccount4);
			buddyUserService.saveBuddyUser(buddyUser4);

			BuddyUser buddyUser5 = new BuddyUser();
			buddyUser5.setDateDenaissance(new Date());
			buddyUser5.setPseudo("dingue@email.fr");
			buddyUser5.setFirstName("Dingolito");
			buddyUser5.setLastName("Dog");
			buddyUser5.setBuddyAccount(buddyAccount5);
			buddyUserService.saveBuddyUser(buddyUser5);

			BuddyUser buddyUser6 = new BuddyUser();
			buddyUser6.setDateDenaissance(new Date());
			buddyUser6.setPseudo("minie@yahoo.fr");
			buddyUser6.setFirstName("Minnie");
			buddyUser6.setLastName("Mouse");
			buddyUser6.setBuddyAccount(buddyAccount6);
			buddyUserService.saveBuddyUser(buddyUser6);

			securityService.saveNewUser("roger@gmail.com", "1234", "1234");
			securityService.saveNewUser("buddy2@email.fr", "1234", "1234");
			securityService.saveNewUser("mickey@yahoo.fr", "1234", "1234");
			securityService.saveNewUser("dodo@gmail.com", "1234", "1234");
			securityService.saveNewUser("dingue@email.fr", "1234", "1234");
			securityService.saveNewUser("minie@yahoo.fr", "1234", "1234");

			securityService.addRoleToUser("roger@gmail.com", "2CONTACT_TO_DEF");
			securityService.addRoleToUser("buddy2@email.fr", "1PROFIL_TO_DEF");
			securityService.addRoleToUser("mickey@yahoo.fr", "1PROFIL_TO_DEF");
			securityService.addRoleToUser("dodo@gmail.com", "1PROFIL_TO_DEF");
			securityService.addRoleToUser("dingue@email.fr", "1PROFIL_TO_DEF");
			securityService.addRoleToUser("minie@yahoo.fr", "3USER");

			securityService.AddBuddyUserToUser("roger@gmail.com", "roger@gmail.com");
			securityService.AddBuddyUserToUser("buddy2@email.fr", "buddy2@email.fr");
			securityService.AddBuddyUserToUser("mickey@yahoo.fr", "mickey@yahoo.fr");
			securityService.AddBuddyUserToUser("dodo@gmail.com", "dodo@gmail.com");
			securityService.AddBuddyUserToUser("dingue@email.fr", "dingue@email.fr");
			securityService.AddBuddyUserToUser("minie@yahoo.fr", "minie@yahoo.fr");
			
			buddyService.addContactsToBuddyUserByEMail("buddy2@email.fr", "minie@yahoo.fr");
			buddyService.addContactsToBuddyUserByEMail("minie@yahoo.fr", "dodo@gmail.com");
			buddyService.addContactsToBuddyUserByEMail("minie@yahoo.fr", "dingue@email.fr");

			buddyService.addTransfersToBuddyUser("minie@yahoo.fr", "dingue@email.fr", 10, "Minnie vers Dingo");
			buddyService.addTransfersToBuddyUser("minie@yahoo.fr", "dodo@gmail.com", 100, "Minnie vers Mickey");

		};

	}

//	@Bean
	CommandLineRunner startBuddy(PayMyBuddyService buddyService) {
		return args -> {

		};
	}

}
