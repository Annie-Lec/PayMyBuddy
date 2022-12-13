package fr.annielec.paymybuddy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.entities.Contact;

public interface BuddyUserRepository extends JpaRepository<BuddyUser, Long> {
	// public BuddyUser findBuddyUserByLastName(String lastName);
	public BuddyUser findBuddyUserByPseudo(String pseudo);
	public BuddyUser findBuddyUserById(Long id);
	
	


}
