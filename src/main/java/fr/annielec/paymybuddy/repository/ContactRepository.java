package fr.annielec.paymybuddy.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	
	public List<Contact> findByBuddyUser(BuddyUser buddyUser);
	public List<Contact> findByIdContact(Long idContact);
	
	public Page<Contact> findByBuddyUser(BuddyUser buddyUser, Pageable pageable);
	
}
