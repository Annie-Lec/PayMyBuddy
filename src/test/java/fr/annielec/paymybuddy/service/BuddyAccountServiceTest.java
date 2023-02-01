package fr.annielec.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.annielec.paymybuddy.entities.BuddyAccount;
import fr.annielec.paymybuddy.repository.BuddyAccountRepository;


@ExtendWith(MockitoExtension.class)
class BuddyAccountServiceTest {
	
	private static BuddyAccountService buddyAccountService;
	
	@Mock
	private static BuddyAccountRepository buddyAccountRepositoryMock;
	
	private static BuddyAccount ba1 = new BuddyAccount();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		
		buddyAccountService = new BuddyAccountServiceImpl(buddyAccountRepositoryMock);
	}

	@Test
	void testSaveBuddyAccount() {
		//Given
		ba1.setBalance(10);
		ba1.setId(1L);
		when(buddyAccountRepositoryMock.save(any(BuddyAccount.class))).thenReturn(ba1);
		
		//When
		BuddyAccount buddyAccount = buddyAccountService.saveBuddyAccount(ba1);
		
		//Then
		assertThat(buddyAccount.getBalance()==10);
		verify(buddyAccountRepositoryMock, times(1)).save(any(BuddyAccount.class));
		
	}

	@Test
	void testFindBuddyAccountById() {
		//Given
		ba1.setBalance(10);
		ba1.setId(1L);
		when(buddyAccountRepositoryMock.findBuddyAccountById(anyLong())).thenReturn(ba1);
		
		//When
		BuddyAccount buddyAccount = buddyAccountService.findBuddyAccountById(1L);
		
		//Then
		assertThat(buddyAccount.getBalance()==10);
		verify(buddyAccountRepositoryMock, times(1)).findBuddyAccountById(anyLong());

		
	}

}
