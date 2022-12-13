package fr.annielec.paymybuddy.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.annielec.paymybuddy.entities.BuddyAccount;
import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.entities.Contact;
import fr.annielec.paymybuddy.entities.Transaction;

public interface PayMyBuddyService {
	
	public Transaction saveTransaction(Transaction transaction);
	public BuddyUser saveBuddyUser(BuddyUser buddyUser);
	public Contact saveContacts(Contact contact);
	public BuddyAccount saveBuddyAccount(BuddyAccount buddyAccount);
	
	public BuddyUser findBuddyUserByPseudo(String pseudo);
	public BuddyUser findBuddyUserById(Long id);
	public BuddyUser saveBuddyUserById(Long id);
	public List<Contact> findContactByPseudo(String pseudo);
	public List<BuddyUser> findBuddyUserContactByPseudo(String pseudo);
	public Long retrieveIdUserWithPseudo(String pseudo);
	public String retrievePseudoWithIdUser(Long id);
	
	Page<BuddyUser> findBuddyUserContactByPseudoContains(String pseudo, String keyword, Pageable pageable);
	
	public void addContactsToBuddyUser(String pseudoBuddyUser, String pseudoContact);

	
	//public List<Transaction> findTransactionByUserPseudo(String pseudo);

}
