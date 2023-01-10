package fr.annielec.paymybuddy.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.annielec.paymybuddy.entities.BuddyAccount;
import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.entities.Contact;
import fr.annielec.paymybuddy.entities.Transaction;
import fr.annielec.paymybuddy.entities.TypeTransaction;




public interface PayMyBuddyService {
	
	public Transaction saveTransaction(Transaction transaction);
	public BuddyUser saveBuddyUser(BuddyUser buddyUser);
	public Contact saveContacts(Contact contact);
	public BuddyAccount saveBuddyAccount(BuddyAccount buddyAccount);
	
	public BuddyUser findBuddyUserByPseudo(String pseudo);
	//en cours -------------------------------------------------
	public BuddyUser findBuddyUserByEmail(String email) throws NullPointerException;
	public BuddyUser findBuddyUserById(Long id);
	public BuddyUser saveBuddyUserById(Long id);
	public List<Contact> findContactByPseudo(String pseudo);
	public List<BuddyUser> findBuddyUserContactForAPseudo(String pseudo);
	public Long retrieveIdUserWithPseudo(String pseudo);
	public String retrievePseudoWithIdUser(Long id);
	public BuddyAccount findAccountByPseudo(String pseudo);
	public BuddyAccount findBuddyAccountById(Long id);
	public void addBuddyAccountToBuddyUser(Long id);
	
	public Page<Contact> findContactByPseudo(String pseudo, Pageable pageable);
	public Page<BuddyUser> findBuddyUserContactForAPseudo(String pseudo,  Pageable pageable);
	
	public String addContactsToBuddyUser(String pseudoBuddyUser, String pseudoContact);
	
	public List<Transaction> findBuddyUserTransactionForAPseudo(String pseudo);
	public Page<Transaction> findBuddyUserTransactionForAPseudoPage(String pseudo, Pageable pageable);

	
	public void creditBalance(Long id, double amount);
	public void debitBalance(Long id, double amount);
	public boolean addTransfersToBuddyUser(String pseudoBuddyUser, String pseudoContact, double amount, String description);
	public List<String> findPseudoBuddyUserContactForAPseudo(String pseudo);
	public String addContactsToBuddyUserByEMail(String pseudoBuddyUser, String emailContact);

	
	public void addTransfersToBuddyUserFromAccount(Long idBuddyUser, double amount,
			String description, TypeTransaction typeTransaction);
	public void addTransfersToBuddyUserForBankAccount(Long idBuddyUser, double amount,
			String description, TypeTransaction typeTransaction);
}
