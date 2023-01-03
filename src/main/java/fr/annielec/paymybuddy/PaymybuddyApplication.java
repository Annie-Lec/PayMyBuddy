package fr.annielec.paymybuddy;


import java.util.Date;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.annielec.paymybuddy.entities.AppRole;
import fr.annielec.paymybuddy.entities.BuddyAccount;
import fr.annielec.paymybuddy.entities.BuddyUser;
//import fr.annielec.paymybuddy.entities.Transaction;
import fr.annielec.paymybuddy.service.PayMyBuddyService;
import fr.annielec.paymybuddy.service.SecurityService;

@SpringBootApplication
public class PaymybuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymybuddyApplication.class, args);
	}
	
	//BCrypt pour cryptage du mot de passe
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	@Bean
	CommandLineRunner start(SecurityService securityService, PayMyBuddyService buddyService) {
		return args-> {
			
			BuddyAccount buddyAccount1 =  new BuddyAccount();
			BuddyAccount buddyAccount2 =  new BuddyAccount();
			BuddyAccount buddyAccount3 =  new BuddyAccount();
			BuddyAccount buddyAccount4 =  new BuddyAccount();
			BuddyAccount buddyAccount5 =  new BuddyAccount();
			BuddyAccount buddyAccount6 =  new BuddyAccount();
			buddyAccount1.setBalance(120);
			buddyAccount6.setBalance(250);
			buddyService.saveBuddyAccount(buddyAccount1);
			buddyService.saveBuddyAccount(buddyAccount2);
			buddyService.saveBuddyAccount(buddyAccount3);
			buddyService.saveBuddyAccount(buddyAccount4);
			buddyService.saveBuddyAccount(buddyAccount5);
			buddyService.saveBuddyAccount(buddyAccount6);
							
			BuddyUser buddyUser1 = new BuddyUser();
			buddyUser1.setDateDenaissance(new Date());
			buddyUser1.setPseudo("Roger");
			buddyUser1.setFirstName("Roger");
			buddyUser1.setLastName("Rabbit");
			buddyUser1.setBuddyAccount(buddyAccount1);
			buddyService.saveBuddyUser(buddyUser1);
			
			BuddyUser buddyUser2 = new BuddyUser();
			buddyUser2.setDateDenaissance(new Date());
			buddyUser2.setPseudo("Bugs");
			buddyUser2.setFirstName("Bugs");
			buddyUser2.setLastName("Bunny");
			buddyUser2.setBuddyAccount(buddyAccount2);
			buddyService.saveBuddyUser(buddyUser2);

			
			BuddyUser buddyUser3 = new BuddyUser();
			buddyUser3.setDateDenaissance(new Date());
			buddyUser3.setPseudo("Mickey");
			buddyUser3.setFirstName("Mike");
			buddyUser3.setLastName("Mouse");
			buddyUser3.setBuddyAccount(buddyAccount3);
			buddyService.saveBuddyUser(buddyUser3);
			
			BuddyUser buddyUser4 = new BuddyUser();
			buddyUser4.setDateDenaissance(new Date());
			buddyUser4.setPseudo("Donald");
			buddyUser4.setFirstName("Donald");
			buddyUser4.setLastName("Duck");
			buddyUser4.setBuddyAccount(buddyAccount4);
			buddyService.saveBuddyUser(buddyUser4);
			
			BuddyUser buddyUser5 = new BuddyUser();
			buddyUser5.setDateDenaissance(new Date());
			buddyUser5.setPseudo("Dingo");
			buddyUser5.setFirstName("Dingolito");
			buddyUser5.setLastName("Dog");
			buddyUser5.setBuddyAccount(buddyAccount5);
			buddyService.saveBuddyUser(buddyUser5);

			
			BuddyUser buddyUser6 = new BuddyUser();
			buddyUser6.setDateDenaissance(new Date());
			buddyUser6.setPseudo("Minie");
			buddyUser6.setFirstName("Minnie");
			buddyUser6.setLastName("Mouse");
			buddyUser6.setBuddyAccount(buddyAccount6);
			buddyService.saveBuddyUser(buddyUser6);

					
			securityService.addNewRole(new AppRole(1L,"USER"));
			securityService.saveNewUser("roger@gmail.com", "1234", "1234");
			securityService.saveNewUser("buddy2@email.fr", "1234", "1234");
			securityService.saveNewUser("mickey@yahoo.fr", "1234", "1234");
			securityService.saveNewUser("dodo@gmail.com", "1234", "1234");
			securityService.saveNewUser("dingue@email.fr", "1234", "1234");
			securityService.saveNewUser("minie@yahoo.fr", "1234", "1234");
			
			securityService.addRoleToUser("roger@gmail.com", "USER");
			securityService.addRoleToUser("buddy2@email.fr", "USER");
			securityService.addRoleToUser("mickey@yahoo.fr", "USER");
			securityService.addRoleToUser("dodo@gmail.com", "USER");
			securityService.addRoleToUser("dingue@email.fr", "USER");
			securityService.addRoleToUser("minie@yahoo.fr", "USER");
			
			securityService.AddBuddyUserToUser("roger@gmail.com", "Roger");
			securityService.AddBuddyUserToUser("buddy2@email.fr", "Bugs");
			securityService.AddBuddyUserToUser("mickey@yahoo.fr", "Mickey");
			securityService.AddBuddyUserToUser("dodo@gmail.com", "Donald");
			securityService.AddBuddyUserToUser("dingue@email.fr", "Dingo");
			securityService.AddBuddyUserToUser("minie@yahoo.fr", "Minie");
			
			buddyService.addBuddyAccountToBuddyUser(1L);
			buddyService.addBuddyAccountToBuddyUser(2L);
			buddyService.addBuddyAccountToBuddyUser(3L);
			buddyService.addBuddyAccountToBuddyUser(4L);
			buddyService.addBuddyAccountToBuddyUser(5L);
			buddyService.addBuddyAccountToBuddyUser(6L);
			

			buddyService.addContactsToBuddyUser("Roger", "Roger");
			buddyService.addContactsToBuddyUser("Roger", "Bugs");
			buddyService.addContactsToBuddyUser("Bugs", "Bugs");
			buddyService.addContactsToBuddyUser("Roger", "Mickey");
			buddyService.addContactsToBuddyUser("Roger", "Minie");
			buddyService.addContactsToBuddyUser("Roger", "Donald");
			buddyService.addContactsToBuddyUser("Bugs", "Minie");
			buddyService.addContactsToBuddyUser("Minie", "Minie");
			buddyService.addContactsToBuddyUser("Minie", "Donald");
			buddyService.addContactsToBuddyUser("Minie", "Dingo");
			buddyService.addContactsToBuddyUser("Minie", "Mickey");
			buddyService.addContactsToBuddyUser("Minie", "Bugs");
			

			buddyService.addTransfersToBuddyUser("Minie", "Dingo", 10, "Minnie vers Dingo");
			buddyService.addTransfersToBuddyUser("Minie", "Mickey", 100, "Minnie vers Mickey");
//			Transaction transaction1 = new Transaction();
//			transaction1.setAmount(10);
//			transaction1.setTransmitter(buddyUser6);
//			transaction1.setDescription("bof bof");
//			transaction1.setBeneficiary(buddyUser5);
//			buddyService.saveTransaction(transaction1);
//
//			Transaction transaction2 = new Transaction();
//			transaction2.setAmount(95);
//			transaction2.setTransmitter(buddyUser6);
//			transaction2.setDescription("bof bof2");
//			transaction2.setBeneficiary(buddyUser4);
//			buddyService.saveTransaction(transaction2);


		};
		
	}
//	@Bean
	CommandLineRunner startBuddy(PayMyBuddyService buddyService) {
		return args-> {
			
			
			
		};
	}

}
