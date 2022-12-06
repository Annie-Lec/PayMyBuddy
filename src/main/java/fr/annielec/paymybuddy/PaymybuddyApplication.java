package fr.annielec.paymybuddy;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.annielec.paymybuddy.entities.AppRole;
import fr.annielec.paymybuddy.entities.AppUser;
import fr.annielec.paymybuddy.entities.BuddyUser;
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
			
			BuddyUser buddyUser1 = new BuddyUser();
			buddyUser1.setDateDenaissance(new Date());
			buddyUser1.setPseudo("Roger");
			buddyUser1.setFirstName("Roger");
			buddyUser1.setLastName("Rabbit");
			buddyService.saveBuddyUser(buddyUser1);
			
			BuddyUser buddyUser2 = new BuddyUser();
			buddyUser2.setDateDenaissance(new Date());
			buddyUser2.setPseudo("Bugs");
			buddyUser2.setFirstName("Bugs");
			buddyUser2.setLastName("Bunny");
			buddyService.saveBuddyUser(buddyUser2);

			
			BuddyUser buddyUser3 = new BuddyUser();
			buddyUser3.setDateDenaissance(new Date());
			buddyUser3.setPseudo("Mickey");
			buddyUser3.setFirstName("Mike");
			buddyUser3.setLastName("Mouse");
			buddyService.saveBuddyUser(buddyUser3);

					
			securityService.addNewRole(new AppRole(1L,"USER"));
			securityService.saveNewUser("roger@gmail.com", "1234", "1234");
			securityService.saveNewUser("buddy2@email.fr", "1234", "1234");
			securityService.saveNewUser("minie@yahoo.fr", "1234", "1234");
			
//			securityService.addNewUser(new AppUser(null, "roger@gmail.com", "1234",true, new ArrayList<>(),null));
//			securityService.addNewUser(new AppUser(null, "buddy2@email.fr", "1234",true, new ArrayList<>(),null));
//			securityService.addNewUser(new AppUser(null, "minie@yahoo.fr", "1234", true, new ArrayList<>(),null));
			
			securityService.addRoleToUser("roger@gmail.com", "USER");
			securityService.addRoleToUser("buddy2@email.fr", "USER");
			securityService.addRoleToUser("minie@yahoo.fr", "USER");
			
			securityService.AddBuddyUserToUser("roger@gmail.com", "Roger");
			securityService.AddBuddyUserToUser("buddy2@email.fr", "Bugs");
			securityService.AddBuddyUserToUser("minie@yahoo.fr", "Mickey");
			
		};
		
	}
	//@Bean
	CommandLineRunner startBuddy(PayMyBuddyService buddyService) {
		return args-> {
			buddyService.saveBuddyUser(new BuddyUser());
			
		};
	}

}
