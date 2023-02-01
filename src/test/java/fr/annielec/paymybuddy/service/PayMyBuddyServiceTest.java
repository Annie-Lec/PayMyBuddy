package fr.annielec.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import fr.annielec.paymybuddy.entities.BuddyAccount;
import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.entities.Contact;
import fr.annielec.paymybuddy.entities.Transaction;
import fr.annielec.paymybuddy.entities.TypeTransaction;
import fr.annielec.paymybuddy.repository.BuddyAccountRepository;
import fr.annielec.paymybuddy.repository.ContactRepository;
import fr.annielec.paymybuddy.repository.TransactionRepository;
import fr.annielec.paymybuddy.util.CheckTheBalance;
import fr.annielec.paymybuddy.util.Operations;

@ExtendWith(MockitoExtension.class)
class PayMyBuddyServiceTest {
	
	private static PayMyBuddyService payMyBuddyService;
	
	@Mock
	private static BuddyUserService buddyUserServiceMock;
	@Mock
	private static BuddyAccountRepository buddyAccountRepositoryMock;
	@Mock
	private static TransactionRepository transactionRepositoryMock;
	@Mock
	private static ContactRepository contactRepositoryMock;
	

	private static Operations ope = new Operations();
	private static CheckTheBalance checkTheBalance = new CheckTheBalance();
	
	private static List<Contact> contacts = new ArrayList<Contact>();
	private static List<BuddyUser> buS = new ArrayList<BuddyUser>();
	private static Page<Contact> contactsPage ;
//	private static Page<BuddyUser> buSPage;

	private static BuddyUser bank = new BuddyUser();
	private static BuddyUser bu1 = new BuddyUser();
	private static BuddyUser bu2 = new BuddyUser();
	private static BuddyUser bu3 = new BuddyUser();
	private static BuddyUser buNew = new BuddyUser();

	private static Contact c1;
	private static Contact c2;
	
	private static BuddyAccount ba ;
	private static BuddyAccount ba2 ;
//	private static Transaction tTransmitter2;
//	private static Transaction tTransmitter3;
//	private static Transaction tbeneficiary1 ;
//	
//	private static List<Transaction> transactionsT ;
//	private static List<Transaction> transactionsB;
	
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		bu1.setId(1L);
		bu2.setId(2L);
		bu3.setId(3L);
		buNew.setId(4L);
		bank.setId(5L);
		bu1.setPseudo("toto1@gmail.com");
		bu2.setPseudo("toto2@gmail.com");
		bu3.setPseudo("toto3@gmail.com");
		buNew.setPseudo("totoNew@gmail.com");
		bank.setPseudo("bank@bank.com");
		bu1.setFirstName("firstNameBU1");
		bu1.setLastName("lastNameBU1");
		c1 = new Contact(1L, 2L, bu1, false);
		c2 = new Contact(2L, 3L, bu1, true);
		contacts.add(c1);
		contacts.add(c2);
		contactsPage = new PageImpl<Contact>(contacts);
		buS.add(bu2);
		buS.add(bu3);
//		buSPage =  new PageImpl<BuddyUser>(buS);
		

	}

	@BeforeEach
	void setUp() throws Exception {
//		tTransmitter2 = new Transaction(1L, 10, 0, null, bu1, bu2, "bu1 vers bu2", null);
//		tTransmitter3 = new Transaction(2L, 20, 0, null, bu1, bu3, "bu1 vers bu3", null);
//		tbeneficiary1 = new Transaction(3L, 5, 0, null, bu2, bu1, "bu2 vers bu1", null);
//		// bu2 commence avec 24.75
//		//bu1 commence avec 73.5
//		 transactionsT = new ArrayList<Transaction>();
//		 transactionsB = new ArrayList<Transaction>();
//		
//		transactionsT.add(tTransmitter2);
//		transactionsT.add(tTransmitter3);
//		transactionsB.add(tbeneficiary1);
//		
		payMyBuddyService = new PayMyBuddyServiceImpl(buddyUserServiceMock, buddyAccountRepositoryMock, transactionRepositoryMock, contactRepositoryMock, ope, checkTheBalance);
	}

	@Test
	void testFindContactByPseudoList() {
		//GIVEN
		when(buddyUserServiceMock.findBuddyUserByEmail(anyString())).thenReturn(bu1);
		when(contactRepositoryMock.findByBuddyUser(bu1)).thenReturn(contacts);
		
		//WHEN
		List<Contact> contactsTest = payMyBuddyService.findContactByPseudo("toto1@gmail.com");
		
		//THEN
		assertThat(contactsTest.size()).isEqualTo(2);
		
		
	}

	@Test
	void testFindContactByPseudoPageable() {
		//GIVEN
		int page=0;
		int size=3;
		Pageable paging = PageRequest.of(page, size);
		when(buddyUserServiceMock.findBuddyUserByEmail(anyString())).thenReturn(bu1);
		when(contactRepositoryMock.findByBuddyUser(any(BuddyUser.class), any(Pageable.class))).thenReturn(contactsPage);
	
		//WHEN
		Page<Contact> contactsTest = payMyBuddyService.findContactByPseudo("toto1@gmail.com", paging);
		
		//THEN
		assertThat(contactsTest.getContent().get(0).getIdContact()).isEqualTo(2L);
		assertThat(contactsTest.getContent().get(1).getIdContact()).isEqualTo(3L);
	
	}

	@Test
	void testFindBuddyUserContactForAPseudoListBU() {
		//GIVEN
		when(payMyBuddyService.findContactByPseudo(anyString())).thenReturn(contacts);
		//WHEN
		List<BuddyUser> buSTest = payMyBuddyService.findBuddyUserContactForAPseudo("toto1@gmail.com");
		//THEN
		assertThat(buSTest.size()).isEqualTo(2);
	}

	@Test
	void testFindPseudoBuddyUserContactForAPseudoListStringPseudo() {
		//GIVEN
		when(payMyBuddyService.findContactByPseudo(anyString())).thenReturn(contacts);
		when(buddyUserServiceMock.findBuddyUserById(3L)).thenReturn(bu3);
		when(buddyUserServiceMock.findBuddyUserById(2L)).thenReturn(bu2);

		//WHEN
		List<String> pseudoContactsTest = payMyBuddyService.findPseudoBuddyUserContactForAPseudo("toto1@gmail.com");
		
		//THEN
		assertThat(pseudoContactsTest.get(0)).isEqualTo("toto2@gmail.com");
				
	}

	@Test
	void testFindBuddyUserContactForAPseudoStringPageable() {
		//GIVEN
		int page=0;
		int size=3;
		Pageable paging = PageRequest.of(page, size);
		when(buddyUserServiceMock.findBuddyUserByEmail(anyString())).thenReturn(bu1);
		when(contactRepositoryMock.findByBuddyUser(any(BuddyUser.class), any(Pageable.class))).thenReturn(contactsPage);
		when(buddyUserServiceMock.findBuddyUserById(3L)).thenReturn(bu3);
		when(buddyUserServiceMock.findBuddyUserById(2L)).thenReturn(bu2);

		//WHEN
		Page<BuddyUser> buContactsTest = payMyBuddyService.findBuddyUserContactForAPseudo("toto1@gmail.com", paging);
		
		//THEN
		assertThat(buContactsTest.getContent().get(0).getPseudo()).isEqualTo("toto2@gmail.com");
		assertThat(buContactsTest.getContent().get(1).getPseudo()).isEqualTo("toto3@gmail.com");

	}
	@Test
	void testFindBuddyUserContactForAPseudoStringPageable_givenException() {
		//GIVEN
		String messageErreur = "Aucun contact trouvé dans notre base de données!";
		int page=0;
		int size=3;
		Pageable paging = PageRequest.of(page, size);
		Page<BuddyUser> buContactsTest = null;

		when(payMyBuddyService.findContactByPseudo(anyString(), paging)).thenReturn(null);
		
		// WHEN
		try {
			buContactsTest = payMyBuddyService.findBuddyUserContactForAPseudo("toto1@gmail.com", paging);
		} catch (Exception e) {
		// THEN
		verify(buddyUserServiceMock, times(0)).findBuddyUserById(anyLong());
		assertNull(buContactsTest);
		assertEquals(messageErreur, e.getMessage());

		}

	}

	@Test
	void testAddContactsToBuddyUserByEMail_newContact() {
		//GIVEN
		when(contactRepositoryMock.findByBuddyUser(any(BuddyUser.class))).thenReturn(contacts);
		when(buddyUserServiceMock.findBuddyUserByEmail("toto1@gmail.com")).thenReturn(bu1);
		when(buddyUserServiceMock.findBuddyUserByEmail("totoNew@gmail.com")).thenReturn(buNew);
		
		//WHEN
		String result = payMyBuddyService.addContactsToBuddyUserByEMail("toto1@gmail.com", "totoNew@gmail.com");
		//THEN
		
		assertThat(result.toString()).isEqualTo("NEW_CONTACT");

	}
	
	
	@Test
	void testAddContactsToBuddyUserByEMail_oldContact() {
		//GIVEN
		when(contactRepositoryMock.findByBuddyUser(any(BuddyUser.class))).thenReturn(contacts);
		when(buddyUserServiceMock.findBuddyUserByEmail("toto1@gmail.com")).thenReturn(bu1);
		when(buddyUserServiceMock.findBuddyUserByEmail("toto2@gmail.com")).thenReturn(bu2);
		
		//WHEN
		String result = payMyBuddyService.addContactsToBuddyUserByEMail("toto1@gmail.com", "toto2@gmail.com");
		//THEN
		
		assertThat(result.toString()).isEqualTo("ALREADY_EXISTS");

	}
	
	@Test
	void testAddContactsToBuddyUserByEMail_contactNotInBDD() {
		//GIVEN
		when(contactRepositoryMock.findByBuddyUser(any(BuddyUser.class))).thenReturn(contacts);
		when(buddyUserServiceMock.findBuddyUserByEmail("toto1@gmail.com")).thenReturn(bu1);
		when(buddyUserServiceMock.findBuddyUserByEmail("totoPasLa@gmail.com")).thenReturn(null);
		
		//WHEN
		String result = payMyBuddyService.addContactsToBuddyUserByEMail("toto1@gmail.com", "totoPasLa@gmail.com");
		//THEN
		
		assertThat(result.toString()).isEqualTo("NOT_IN_BDD");

	}


	@Test
	void testUpdateBalance() {
		//GIVEN
		ba = new BuddyAccount(1L, 100, null, null, bu1, 0);
		ba2 = new BuddyAccount(2L, 20, null, null, bu2, 0);
		when(buddyAccountRepositoryMock.findBuddyAccountById(anyLong())).thenReturn(ba);
		when(buddyAccountRepositoryMock.save(any(BuddyAccount.class))).thenReturn(ba);
	
		//WHEN
		payMyBuddyService.updateBalance(1L, 20.0, TypeTransaction.CREDIT);
		
		//THEN
		assertThat(ba.getBalance()).isEqualTo(120);

	}

	/******************************** *************/
//	@Test
//	void testFindBuddyUserTransactionForAPseudo() {
//
//		//GIVEN
//		when(buddyUserServiceMock.findBuddyUserByEmail("toto1@gmail.com")).thenReturn(bu1);
//		when(transactionRepositoryMock.findTransactionByTransmitter(any(BuddyUser.class))).thenReturn(transactionsT);
//		when(transactionRepositoryMock.findTransactionByBeneficiary(any(BuddyUser.class))).thenReturn(transactionsB);
//
//		//WHEN
//		List<Transaction> transactionBu1 = payMyBuddyService.findBuddyUserTransactionForAPseudo("toto1@gmail.com");
//		
//		//THEN
//		assertThat(transactionBu1).contains(tTransmitter2);
//		assertThat(transactionBu1).contains(tbeneficiary1);
//	
//	}

//	@Test
//	void testFindBuddyUserTransactionForAPseudoPage() {
//		//GIVEN
//		
//		int page=0;
//		int size=3;
//		Pageable paging = PageRequest.of(page, size);
//
//		when(buddyUserServiceMock.findBuddyUserByEmail("toto1@gmail.com")).thenReturn(bu1);
//		when(transactionRepositoryMock.findTransactionByTransmitter(any(BuddyUser.class))).thenReturn(transactionsT);
//		when(transactionRepositoryMock.findTransactionByBeneficiary(any(BuddyUser.class))).thenReturn(transactionsB);
//
//		//WHEN
//		Page<Transaction> transactionBu1 = payMyBuddyService.findBuddyUserTransactionForAPseudoPage("toto1@gmail.com", paging);
//		
//		//THEN
//		assertThat(transactionBu1).contains(tTransmitter3);
//		assertThat(transactionBu1).contains(tbeneficiary1);
//	
//
//
//	}

	@Test
	void testAddTransfersToBuddyUser_balanceIsEnough() {
		//GIVEN
		ba = new BuddyAccount(1L, 100, null, null, bu1, 0);
		ba2 = new BuddyAccount(2L, 20, null, null, bu2, 0);

		when(buddyUserServiceMock.findBuddyUserByEmail("toto1@gmail.com")).thenReturn(bu1);
		when(buddyUserServiceMock.findBuddyUserByEmail("toto2@gmail.com")).thenReturn(bu2);
		when(buddyAccountRepositoryMock.findByBuddyUser(bu1)).thenReturn(ba);	
		when(buddyAccountRepositoryMock.findBuddyAccountById(1L)).thenReturn(ba);
		when(buddyAccountRepositoryMock.findBuddyAccountById(2L)).thenReturn(ba2);
		//WHEN
		boolean doTransaction = payMyBuddyService.addTransfersToBuddyUser("toto1@gmail.com", "toto2@gmail.com", 5d, "nouvelle transaction 5 vers bu2");
		
		//THEN
		assertThat(doTransaction).isTrue();
		
	}
	
	@Test
	void testAddTransfersToBuddyUser_balanceIsInsufficient() {
		//GIVEN
		ba = new BuddyAccount(1L, 100, null, null, bu1, 0);
		//ba2 = new BuddyAccount(2L, 20, null, null, bu2, 0);

		when(buddyUserServiceMock.findBuddyUserByEmail("toto1@gmail.com")).thenReturn(bu1);
		when(buddyUserServiceMock.findBuddyUserByEmail("toto2@gmail.com")).thenReturn(bu2);
		when(buddyAccountRepositoryMock.findByBuddyUser(bu1)).thenReturn(ba);	
//		when(buddyAccountRepositoryMock.findByBuddyUser(bu2)).thenReturn(ba2);	
//		when(buddyAccountRepositoryMock.findBuddyAccountById(1L)).thenReturn(ba);
//		when(buddyAccountRepositoryMock.findBuddyAccountById(2L)).thenReturn(ba2);
	
		//WHEN
		boolean doTransaction = payMyBuddyService.addTransfersToBuddyUser("toto1@gmail.com", "toto2@gmail.com", 2000d, "nouvelle transaction 2000 vers bu2");
		
		//THEN
		assertThat(doTransaction).isFalse();
	}


	@Test
	void testAddTransfersToBuddyUserFromAccount() {
		//GIVEN
		ba = new BuddyAccount(1L, 100, null, null, bu1, 0);
		ba2 = new BuddyAccount(2L, 20, null, null, bu2, 0);

		when(buddyUserServiceMock.findBuddyUserByEmail("bank@bank.com")).thenReturn(bank);
		when(buddyUserServiceMock.findBuddyUserById(2L)).thenReturn(bu2);
		when(buddyAccountRepositoryMock.findBuddyAccountById(2L)).thenReturn(ba2);
		//WHEN
		payMyBuddyService.addTransfersToBuddyUserFromAccount(2L, 50.0,"depuis banque", TypeTransaction.CREDIT);
		
		//THEN
		assertThat(ba2.getBalance()).isEqualTo(70);

	}

	@Test
	void testAddTransfersForBuddyUserToBankAccount() {
		//GIVEN
		ba = new BuddyAccount(1L, 100, null, null, bu1, 0);
		ba2 = new BuddyAccount(2L, 20, null, null, bu2, 0);

		when(buddyUserServiceMock.findBuddyUserByEmail("bank@bank.com")).thenReturn(bank);
		when(buddyUserServiceMock.findBuddyUserById(2L)).thenReturn(bu2);
		when(buddyAccountRepositoryMock.findBuddyAccountById(2L)).thenReturn(ba2);
		when(buddyAccountRepositoryMock.findByBuddyUser(bu2)).thenReturn(ba2);
		//WHEN
		payMyBuddyService.addTransfersForBuddyUserToBankAccount(2L, 10,"vers banque", TypeTransaction.DEBIT);
		
		//THEN
		assertThat(ba2.getBalance()).isEqualTo(9.5);

	}

}
