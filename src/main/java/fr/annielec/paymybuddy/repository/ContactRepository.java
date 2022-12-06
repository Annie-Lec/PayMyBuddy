package fr.annielec.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	
	public BuddyUser findBuddyUserByPseudoContact(String pseudoContact);
	public Contact findContactByPseudoContact(String pseudoContact);


}
