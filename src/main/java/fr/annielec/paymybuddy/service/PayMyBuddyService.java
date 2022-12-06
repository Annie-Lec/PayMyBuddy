package fr.annielec.paymybuddy.service;

import java.util.List;

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
	public Contact  findContactByPseudo(String pseudo);
	
	public void addContactsToBuddyUser(String pseudoBuddyUser, String pseudoContact);
	
	//public List<Transaction> findTransactionByUserPseudo(String pseudo);

}
