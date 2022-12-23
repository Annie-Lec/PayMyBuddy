package fr.annielec.paymybuddy.service;

import java.util.ArrayList;
import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.data.domain.Pageable;

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
public class PayMyBuddyServiceImpl implements PayMyBuddyService {

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
	public BuddyUser saveBuddyUserById(Long id) {
		BuddyUser buddyUser = buddyUserRepository.findBuddyUserById(id);
		buddyUserRepository.save(buddyUser);
		return buddyUser;
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
		BuddyUser buddyUser = buddyUserRepository.findBuddyUserByPseudo(pseudoBuddyUser);
		BuddyUser buddyUser2 = buddyUserRepository.findBuddyUserByPseudo(pseudoContact);
		Contact contact = new Contact();

		contact.setBuddyUser(buddyUser);
		contact.setIdContact(buddyUser2.getId());
		contact.setActiveStatusContact(true);
		contactRepository.save(contact);

		buddyUser.getContacts().add(contact);
		buddyUserRepository.save(buddyUser);

	}

	@Override
	public BuddyUser findBuddyUserByPseudo(String pseudo) {

		return buddyUserRepository.findBuddyUserByPseudo(pseudo);
	}

	@Override
	public List<Contact> findContactByPseudo(String pseudo) {
		List<Contact> contacts;

		BuddyUser buddyUser = buddyUserRepository.findBuddyUserByPseudo(pseudo);

		contacts = contactRepository.findByBuddyUser(buddyUser);
		return contacts;
	}



	@Override
	public Page<Contact> findContactByPseudo(String pseudo, Pageable pageable) {
		Page<Contact> contacts;

		BuddyUser buddyUser = buddyUserRepository.findBuddyUserByPseudo(pseudo);

		contacts = contactRepository.findByBuddyUser(buddyUser, pageable);
		return contacts;
	}

	@Override
	public List<BuddyUser> findBuddyUserContactForAPseudo(String pseudo) {
		List<Contact> contacts = new ArrayList<>();
		List<BuddyUser> buddyUserContacts = new ArrayList<>();

		contacts = findContactByPseudo(pseudo);
		contacts.forEach(c -> {

			BuddyUser b = buddyUserRepository.findBuddyUserById(c.getIdContact());
			buddyUserContacts.add(b);
		});

		return buddyUserContacts;
	}

	@Override
	public Page<BuddyUser> findBuddyUserContactForAPseudo(String pseudo, Pageable pageable) {

		Page<Contact> contacts;
		List<BuddyUser> buddyUserContacts = new ArrayList<BuddyUser>();
		Page<BuddyUser> buddyUserContactsPage = null; ;

		try {
			contacts = findContactByPseudo(pseudo, pageable);

			contacts.forEach(c -> {

				BuddyUser b = buddyUserRepository.findBuddyUserById(c.getIdContact());
				buddyUserContacts.add(b);
				
			});
			buddyUserContactsPage = new PageImpl<BuddyUser>(buddyUserContacts, pageable, buddyUserContacts.size());

		} catch (Exception e) {
			System.out.println("");
		}
		
		return buddyUserContactsPage;

	}

//	@Override
//	public Page<BuddyUser> findByPseudoContains(String pseudo, Pageable pageable, List<BuddyUser> buContacts) {
//		
//		
//		Page<BuddyUser> page = new PageImpl<BuddyUser>(buContacts, pageable, buContacts.size());
//
//		return page;
//	}
//	

	@Override
	public Page<BuddyUser> findContactsForAPseudo(Pageable pageable, List<BuddyUser> buContacts) {

		Page<BuddyUser> page = new PageImpl<BuddyUser>(buContacts, pageable, buContacts.size());

		return page;
	}

	@Override
	public Long retrieveIdUserWithPseudo(String pseudo) {
		Long idUser;
		idUser = buddyUserRepository.findBuddyUserByPseudo(pseudo).getId();
		return idUser;
	}

	@Override
	public String retrievePseudoWithIdUser(Long id) {
		String pseudoUser;
		pseudoUser = buddyUserRepository.findBuddyUserById(id).getPseudo();
		return pseudoUser;
	}

	@Override
	public BuddyUser findBuddyUserById(Long id) {

		return buddyUserRepository.findBuddyUserById(id);
	}
	
	@Override
	public BuddyAccount findAccountByPseudo(String pseudo) {
		BuddyAccount buddyAccount;
		BuddyUser buddyUser = buddyUserRepository.findBuddyUserByPseudo(pseudo);
		buddyAccount = buddyAccountRepository.findByBuddyUser(buddyUser);
		return buddyAccount;
	}

	@Override
	public void addBuddyAccountToBuddyUser(Long id) {
		BuddyUser buddyUser = buddyUserRepository.findBuddyUserById(id);
		BuddyAccount buddyAccount = buddyAccountRepository.findBuddyAccountById(id);
		
		buddyAccount.setBuddyUser(buddyUser);
		buddyAccountRepository.save(buddyAccount);
		
		
	}

	@Override
	public BuddyAccount findBuddyAccountById(Long id) {
		
		return buddyAccountRepository.findBuddyAccountById(id);
	}

	@Override
	public void creditBalance(Long id,  double amount) {
		BuddyAccount buddyAccount = buddyAccountRepository.findBuddyAccountById(id);
		double balance =  buddyAccount.getBalance();
		balance += amount;
		buddyAccount.setBalance(balance);
		buddyAccountRepository.save(buddyAccount);
		
	}


}
