package fr.annielec.paymybuddy.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.entities.Contact;
import fr.annielec.paymybuddy.entities.Transaction;
import fr.annielec.paymybuddy.entities.TypeTransaction;

public interface PayMyBuddyService {

	public List<Contact> findContactByPseudo(String pseudo);

	public Page<Contact> findContactByPseudo(String pseudo, Pageable pageable);

	public List<BuddyUser> findBuddyUserContactForAPseudo(String pseudo);

	public Page<BuddyUser> findBuddyUserContactForAPseudo(String pseudo, Pageable pageable);

	public List<String> findPseudoBuddyUserContactForAPseudo(String pseudo);

	public String addContactsToBuddyUserByEMail(String pseudoBuddyUser, String emailContact);

	public void updateBalance(Long id, double amount, TypeTransaction typeOperation);

	public List<Transaction> findBuddyUserTransactionForAPseudo(String pseudo);

	public Page<Transaction> findBuddyUserTransactionForAPseudoPage(String pseudo, Pageable pageable);

	public boolean addTransfersToBuddyUser(String pseudoBuddyUser, String pseudoContact, double amount,
			String description);

	public void addTransfersToBuddyUserFromAccount(Long idBuddyUser, double amount, String description,
			TypeTransaction typeTransaction);

	public boolean addTransfersForBuddyUserToBankAccount(Long idBuddyUser, double amount, String description,
			TypeTransaction typeTransaction);
}
