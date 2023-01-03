package fr.annielec.paymybuddy.service;

import java.util.ArrayList;
import java.util.Date;
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
import fr.annielec.paymybuddy.entities.TypeTransaction;
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
	public boolean addContactsToBuddyUser(String pseudoBuddyUser, String pseudoContact) {
		BuddyUser buddyUser = buddyUserRepository.findBuddyUserByPseudo(pseudoBuddyUser);
		BuddyUser buddyUser2 = buddyUserRepository.findBuddyUserByPseudo(pseudoContact);
		List<Contact> actualContacts = findContactByPseudo(pseudoBuddyUser);

		boolean exists = false;

		for (int i = 0; i < actualContacts.size(); i++) {

			if (actualContacts.get(i).getIdContact() == buddyUser2.getId()) {
				exists = true;
			}
		}
		if (exists == false) {
			Contact contact = new Contact();
			contact.setBuddyUser(buddyUser);

			contact.setIdContact(buddyUser2.getId());
			contact.setActiveStatusContact(true);
			contactRepository.save(contact);

			buddyUser.getContacts().add(contact);
			buddyUserRepository.save(buddyUser);
		}
		return exists;

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

////************************************************
	@Override
	public Page<Contact> findContactByPseudo(String pseudo, Pageable pageable) {
		Page<Contact> contacts;

		BuddyUser buddyUser = buddyUserRepository.findBuddyUserByPseudo(pseudo);

		contacts = contactRepository.findByBuddyUser(buddyUser, pageable);
		return contacts;
	}

/////////////////////////////////////////////////////////
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
	public List<String> findPseudoBuddyUserContactForAPseudo(String pseudo) {
		List<String> pseudoContacts = new ArrayList<>();
		List<BuddyUser> buddyUserContacts = new ArrayList<>();

		buddyUserContacts = findBuddyUserContactForAPseudo(pseudo);
		buddyUserContacts.forEach(c -> {

			String pseudoC = c.getPseudo();
			pseudoContacts.add(pseudoC);
		});

		return pseudoContacts;
	}

	@Override
	public Page<BuddyUser> findBuddyUserContactForAPseudo(String pseudo, Pageable pageable) {

		Page<Contact> contacts;
		List<BuddyUser> buddyUserContacts = new ArrayList<BuddyUser>();
		Page<BuddyUser> buddyUserContactsPage = null;
		;

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

	
	@Override
	public List<Transaction> findBuddyUserTransactionForAPseudo(String pseudo) {

		List<Transaction> transactionsT = new ArrayList<>();
		List<Transaction> transactionsB = new ArrayList<>();
		List<Transaction> transactions = new ArrayList<>();

		BuddyUser b = buddyUserRepository.findBuddyUserByPseudo(pseudo);

		transactionsT = transactionRepository.findTransactionByTransmitter(b);
		transactionsT.forEach(t -> {
			t.setType(TypeTransaction.DEBIT);
		});
		transactions.addAll(transactionsT);

		transactionsB = transactionRepository.findTransactionByBeneficiary(b);
		transactionsB.forEach(t -> {
			t.setType(TypeTransaction.CREDIT);
		});
		transactions.addAll(transactionsB);

		return transactions;
	}


	@Override
	public Page<Transaction> findBuddyUserTransactionForAPseudoPage(String pseudo, Pageable pageable) {
		List<Transaction> buddyUserTransactions = new ArrayList<Transaction>();
		Page<Transaction> buddyUserTransactionsPage = null;

		buddyUserTransactions = findBuddyUserTransactionForAPseudo(pseudo);
		
		final int start = (int)pageable.getOffset();
		final int end = Math.min((start + pageable.getPageSize()), buddyUserTransactions.size());
		
		buddyUserTransactionsPage = new PageImpl<Transaction>(buddyUserTransactions.subList(start, end), pageable,
				buddyUserTransactions.size());

		return buddyUserTransactionsPage;
	}

//	@Override
//	public Page<BuddyUser> findContactsForAPseudo(Pageable pageable, List<BuddyUser> buContacts) {
//
//		Page<BuddyUser> page = new PageImpl<BuddyUser>(buContacts, pageable, buContacts.size());
//
//		return page;
//	}

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
	public void creditBalance(Long id, double amount) {
		BuddyAccount buddyAccount = buddyAccountRepository.findBuddyAccountById(id);
		double balance = buddyAccount.getBalance();
		balance += amount;
		buddyAccount.setBalance(balance);
		buddyAccountRepository.save(buddyAccount);
	}

	@Override
	public void debitBalance(Long id, double amount) {
		BuddyAccount buddyAccount = buddyAccountRepository.findBuddyAccountById(id);
		double balance = buddyAccount.getBalance();
		balance -= amount;
		buddyAccount.setBalance(balance);
		buddyAccountRepository.save(buddyAccount);

	}

	@Override
	public boolean addTransfersToBuddyUser(String pseudoBuddyUser, String pseudoContact, double amount,
			String description) {
		BuddyUser transmitter = buddyUserRepository.findBuddyUserByPseudo(pseudoBuddyUser);
		BuddyUser beneficiary = buddyUserRepository.findBuddyUserByPseudo(pseudoContact);
		// List<Contact> actualContacts = findContactByPseudo(pseudoBuddyUser);
		double balance;

		balance = buddyAccountRepository.findByBuddyUser(transmitter).getBalance();

		boolean balanceIsEnough = false;

		if (balance >= amount) {

			balanceIsEnough = true;
			Transaction transaction = new Transaction();

			transaction.setAmount(amount);
			transaction.setBeneficiary(beneficiary);
			transaction.setTransmitter(transmitter);
			transaction.setDate(new Date());
			transaction.setDescription(description);
			saveTransaction(transaction);
			transmitter.getTransactions().add(transaction);
			beneficiary.getTransactions().add(transaction);
			buddyUserRepository.save(transmitter);
			buddyUserRepository.save(beneficiary);
			saveTransaction(transaction);

			creditBalance(beneficiary.getId(), amount);
			debitBalance(transmitter.getId(), amount);

		}
		return balanceIsEnough;
	}

}
