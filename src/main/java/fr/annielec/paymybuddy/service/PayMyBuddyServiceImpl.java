package fr.annielec.paymybuddy.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.annielec.paymybuddy.entities.BuddyAccount;
import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.entities.Contact;
import fr.annielec.paymybuddy.entities.Transaction;
import fr.annielec.paymybuddy.repository.BuddyAccountRepository;
import fr.annielec.paymybuddy.repository.BuddyUserRepository;
import fr.annielec.paymybuddy.repository.ContactRepository;
import fr.annielec.paymybuddy.repository.TransactionRepository;
import lombok.AllArgsConstructor;


@Service
@Transactional
@AllArgsConstructor
public class PayMyBuddyServiceImpl implements PayMyBuddyService{
	
	private BuddyUserRepository buddyUserRepository;
	private BuddyAccountRepository buddyAccountRepository;
	private TransactionRepository transactionRepository;
	private ContactRepository contactRepository;
	

	@Override
	public Transaction saveTransaction(Transaction transaction) {
		
		return transactionRepository.save(transaction);
	}

	@Override
	public BuddyUser saveBuddyUser(BuddyUser buddyUser) {
		
		return buddyUserRepository.save(buddyUser);
	}

	@Override
	public Contact saveContacts(Contact contact) {
		
		return contactRepository.save(contact);
	}

	@Override
	public BuddyAccount saveBuddyAccount(BuddyAccount buddyAccount) {
		
		return buddyAccountRepository.save(buddyAccount);
	}


	@Override
	public void addContactsToBuddyUser(String pseudoBuddyUser, String pseudoContact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BuddyUser findBuddyUserByPseudo(String pseudo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contact findContactByPseudo(String pseudo) {
		// TODO Auto-generated method stub
		return null;
	}



	

}
