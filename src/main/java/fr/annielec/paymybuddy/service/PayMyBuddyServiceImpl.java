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
import fr.annielec.paymybuddy.entities.TypeContact;
import fr.annielec.paymybuddy.entities.TypeTransaction;

import fr.annielec.paymybuddy.repository.BuddyAccountRepository;

import fr.annielec.paymybuddy.repository.ContactRepository;
import fr.annielec.paymybuddy.repository.TransactionRepository;
import fr.annielec.paymybuddy.util.CheckTheBalance;
import fr.annielec.paymybuddy.util.Operations;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PayMyBuddyServiceImpl implements PayMyBuddyService {

	private final BuddyUserService buddyUserService;
	private final BuddyAccountRepository buddyAccountRepository;
	private final TransactionRepository transactionRepository;
	private final ContactRepository contactRepository;
	private Operations ope;
	private CheckTheBalance checkTheBalance;

//****** CONTACT***** CONTACT***** CONTACT***** CONTACT***** CONTACT 
	@Override
	public List<Contact> findContactByPseudo(String pseudo) {
		List<Contact> contacts;

		BuddyUser buddyUser = buddyUserService.findBuddyUserByEmail(pseudo);

		contacts = contactRepository.findByBuddyUser(buddyUser);
		return contacts;
	}

	@Override
	public Page<Contact> findContactByPseudo(String pseudo, Pageable pageable) {
		Page<Contact> contacts;

		BuddyUser buddyUser = buddyUserService.findBuddyUserByEmail(pseudo);

		contacts = contactRepository.findByBuddyUser(buddyUser, pageable);
		return contacts;
	}

	@Override
	public List<BuddyUser> findBuddyUserContactForAPseudo(String pseudo) {
		List<Contact> contacts = new ArrayList<>();
		List<BuddyUser> buddyUserContacts = new ArrayList<>();

		contacts = findContactByPseudo(pseudo);
		contacts.forEach(c -> {

			BuddyUser b = buddyUserService.findBuddyUserById(c.getIdContact());
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

		Page<Contact> contacts = null;
		List<BuddyUser> buddyUserContacts = new ArrayList<BuddyUser>();
		Page<BuddyUser> buddyUserContactsPage = null;

		contacts = findContactByPseudo(pseudo, pageable);
		
		if (contacts == null) {
			throw new NullPointerException("Aucun contact trouvé dans notre base de données!");
		} else {
			contacts.forEach(c -> {

				BuddyUser b = buddyUserService.findBuddyUserById(c.getIdContact());
				buddyUserContacts.add(b);

			});
			buddyUserContactsPage = new PageImpl<BuddyUser>(buddyUserContacts, pageable, buddyUserContacts.size());

		}

		return buddyUserContactsPage;

	}

	@Override
	public String addContactsToBuddyUserByEMail(String pseudoBuddyUser, String emailContact)
			throws NullPointerException {
		BuddyUser buddyUser = buddyUserService.findBuddyUserByEmail(pseudoBuddyUser);
		// on récupère la liste des contacts du BU
		List<Contact> actualContacts = findContactByPseudo(pseudoBuddyUser);
		// on initialise de la variable (newContact, alreadyExist, notInBDD) à new par
		// défaut
		String exists = TypeContact.NEW_CONTACT.toString();

		try {
			BuddyUser buddyUser2 = buddyUserService.findBuddyUserByEmail(emailContact);

			for (int i = 0; i < actualContacts.size(); i++) {
				// en balayant la liste des actuelcontacts si on trouve qui matche alors already
				// exists !
				if (actualContacts.get(i).getIdContact() == buddyUser2.getId()) {
					exists = TypeContact.ALREADY_EXISTS.toString();
				}
			}
			if (exists == TypeContact.NEW_CONTACT.toString()) {
				Contact contact = new Contact();
				contact.setBuddyUser(buddyUser);

				contact.setIdContact(buddyUser2.getId());
				contact.setActiveStatusContact(true);
				contactRepository.save(contact);

				buddyUser.getContacts().add(contact);
				buddyUserService.saveBuddyUser(buddyUser);
			}

		} catch (NullPointerException e) {

			exists = TypeContact.NOT_IN_BDD.toString();
			System.out.println(e.getMessage());

		}
		return exists;

	}

	// ************ ACCOUNT ******************* ACCOUNT ********** ACCOUNT
	// **********
	@Override
	public void updateBalance(Long id, double amount, TypeTransaction typeOperation) {
		BuddyAccount buddyAccount = buddyAccountRepository.findBuddyAccountById(id);
		System.out.println(21);

		double balance = buddyAccount.getBalance();
		System.out.println(22);
		if (typeOperation == TypeTransaction.DEBIT) {
			System.out.println(23);
			balance = ope.debit(balance, amount);
		} else {
			balance = ope.credit(balance, amount);
			System.out.println(24);
		}
		buddyAccount.setBalance(balance);
		System.out.println(25);
		buddyAccountRepository.save(buddyAccount);
		System.out.println(26);

	}

	// **** TRANSACTION ******** TRANSACTION ******** TRANSACTION ********
	// TRANSACTION ****

	@Override
	public List<Transaction> findBuddyUserTransactionForAPseudo(String pseudo) {

		List<Transaction> transactionsT = new ArrayList<>();
		List<Transaction> transactionsB = new ArrayList<>();
		List<Transaction> transactions = new ArrayList<>();

		BuddyUser b = buddyUserService.findBuddyUserByEmail(pseudo);

		transactionsT = transactionRepository.findTransactionByTransmitter(b);
		transactionsT.forEach(t -> {
			if (t.getType() == null) {
				t.setType(TypeTransaction.DEBIT);
			}

		});
		transactions.addAll(transactionsT);

		transactionsB = transactionRepository.findTransactionByBeneficiary(b);
		transactionsB.forEach(t -> {
			if (t.getType() == null)
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

		final int start = (int) pageable.getOffset();
		final int end = Math.min((start + pageable.getPageSize()), buddyUserTransactions.size());

		buddyUserTransactionsPage = new PageImpl<Transaction>(buddyUserTransactions.subList(start, end), pageable,
				buddyUserTransactions.size());

		return buddyUserTransactionsPage;
	}

	@Override
	public boolean addTransfersToBuddyUser(String pseudoBuddyUser, String pseudoContact, double amount,
			String description) {
		BuddyUser transmitter = buddyUserService.findBuddyUserByEmail(pseudoBuddyUser);
		BuddyUser beneficiary = buddyUserService.findBuddyUserByEmail(pseudoContact);
		//BuddyAccount baBeneficiary = buddyAccountRepository.findByBuddyUser(beneficiary);
		double balance;
		double fees;

		balance = buddyAccountRepository.findByBuddyUser(transmitter).getBalance();
		fees = ope.calculFees(amount);

		boolean balanceIsEnough = false;

		if (checkTheBalance.ItsGoodEnough(balance, amount, fees)) {
			// if (balance >= amount) {
			balanceIsEnough = true;
			Transaction transaction = new Transaction();
			System.out.println(0);

			transaction.setAmount(amount);
			transaction.setBeneficiary(beneficiary);
			transaction.setTransmitter(transmitter);
			transaction.setFees(fees);
			transaction.setDate(new Date());
			System.out.println(31);
			transaction.setDescription(description);
			transactionRepository.save(transaction);
			System.out.println(32);
			transmitter.getTransactions().add(transaction);
			beneficiary.getTransactions().add(transaction);
			System.out.println(33);
			buddyUserService.saveBuddyUser(transmitter);
			System.out.println(34);
			buddyUserService.saveBuddyUser(beneficiary);
			System.out.println(35);
			
			transactionRepository.save(transaction);
			System.out.println(1);

			updateBalance(beneficiary.getId(), amount, TypeTransaction.CREDIT);
			System.out.println(2);
		
			updateBalance(transmitter.getId(), amount + fees, TypeTransaction.DEBIT);
			System.out.println(3);

		}
		return balanceIsEnough;
	}

	@Override
	public void addTransfersToBuddyUserFromAccount(Long idBuddyUser, double amount, String description,
			TypeTransaction typeTransaction) {
		BuddyUser transmitter = buddyUserService.findBuddyUserByEmail("bank@bank.com");
		BuddyUser beneficiary = buddyUserService.findBuddyUserById(idBuddyUser);

		double fees = ope.calculFees(amount);

		Transaction transaction = new Transaction();

		transaction.setAmount(amount);
		transaction.setFees(fees);
		transaction.setBeneficiary(beneficiary);
		transaction.setTransmitter(transmitter);
		transaction.setDate(new Date());
		transaction.setDescription(description);
		transaction.setType(typeTransaction);
		transactionRepository.save(transaction);
		beneficiary.getTransactions().add(transaction);
		buddyUserService.saveBuddyUser(beneficiary);
		transactionRepository.save(transaction);

		updateBalance(beneficiary.getId(), amount, TypeTransaction.CREDIT);

	}

	@Override
	public boolean addTransfersForBuddyUserToBankAccount(Long idBuddyUser, double amount, String description,
			TypeTransaction typeTransaction) {
		BuddyUser transmitter = buddyUserService.findBuddyUserById(idBuddyUser);
		BuddyUser beneficiary = buddyUserService.findBuddyUserByEmail("bank@bank.com");
		double balance;

		balance = buddyAccountRepository.findByBuddyUser(transmitter).getBalance();
		double fees = ope.calculFees(amount);
		boolean transferOK = checkTheBalance.ItsGoodEnough(balance, amount, fees);

		if (transferOK == true) {

			Transaction transaction = new Transaction();

			transaction.setAmount(amount);
			transaction.setFees(fees);
			transaction.setBeneficiary(beneficiary);
			transaction.setTransmitter(transmitter);
			transaction.setDate(new Date());
			transaction.setDescription(description);
			transaction.setType(typeTransaction);
			transactionRepository.save(transaction);
			transmitter.getTransactions().add(transaction);
			buddyUserService.saveBuddyUser(transmitter);

			updateBalance(transmitter.getId(), amount + fees, TypeTransaction.DEBIT);

		}
		return transferOK;

	}

}
