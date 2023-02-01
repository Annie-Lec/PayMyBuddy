/**
 * 
 */
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

import fr.annielec.paymybuddy.entities.AppRole;
import fr.annielec.paymybuddy.entities.AppUser;
import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.repository.AppRoleRepository;
import fr.annielec.paymybuddy.repository.AppUserRepository;
import fr.annielec.paymybuddy.repository.BuddyUserRepository;

/**
 * @author aNewL
 *
 */
@ExtendWith(MockitoExtension.class)
class BuddyUserServiceTest {

	
	private static BuddyUserService buddyUserService;
	
	@Mock
	private static BuddyUserRepository buddyUserRepositoryMock;
	
	@Mock
	private static AppUserRepository appUserRepositoryMock;
	@Mock
	private static AppRoleRepository appRoleRepositoryMock;

	private static BuddyUser bu1 = new BuddyUser();
	private static List<AppRole> appRoles = new ArrayList<AppRole>(); 
	private static AppUser appUser;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		appRoles.add(new AppRole(1L, "USER", "descr User"));
		appUser = new AppUser(1L, "hello@gmail.com", "$2a$10$b0GhrNhc01ryv/KbKM6lNe8L/t2B0sYZAJT20p8BxvainvtB1IkkS" , true, appRoles, bu1);
	}
	
	@BeforeEach
	private void setUpPerTest() {
		buddyUserService = new BuddyUserServiceImpl(buddyUserRepositoryMock, appUserRepositoryMock );
	}

	/**
	 * Test method for
	 * {@link fr.annielec.paymybuddy.service.BuddyUserServiceImpl#saveBuddyUser(fr.annielec.paymybuddy.entities.BuddyUser)}.
	 */
	@Test
	void testSaveBuddyUser() {
		// GIVEN
		bu1.setId(1L);
		bu1.setFirstName("firstNameBU1");
		bu1.setLastName("lastNameBU1");
		bu1.setAppUser(appUser);
		when(buddyUserRepositoryMock.save(any(BuddyUser.class))).thenReturn(bu1);

		// WHEN
		BuddyUser buddyUser = buddyUserService.saveBuddyUser(bu1);
		
		// THEN
		assertThat(buddyUser.getFirstName()).isEqualTo("firstNameBU1");
		verify(buddyUserRepositoryMock, times(1)).save(any(BuddyUser.class));

	}

	/**
	 * Test method for
	 * {@link fr.annielec.paymybuddy.service.BuddyUserServiceImpl#retrievePseudoWithIdUser(java.lang.Long)}.
	 */
	@Test
	void testRetrievePseudoWithIdUser() {
		// GIVEN
		String pseudo = "pseudo";
		bu1.setId(1L);
		bu1.setPseudo(pseudo);
		bu1.setFirstName("firstNameBU1");
		bu1.setLastName("lastNameBU1");
		bu1.setAppUser(appUser);
		when(buddyUserRepositoryMock.findBuddyUserById(anyLong())).thenReturn(bu1);

		// WHEN
		String pseudoLookFor = buddyUserService.retrievePseudoWithIdUser(1L);
		
		// THEN
		assertThat(pseudoLookFor).isEqualTo("pseudo");
		verify(buddyUserRepositoryMock, times(1)).findBuddyUserById(anyLong());

		
	}

	/**
	 * Test method for
	 * {@link fr.annielec.paymybuddy.service.BuddyUserServiceImpl#findBuddyUserById(java.lang.Long)}.
	 */
	@Test
	void testFindBuddyUserById() {
		// GIVEN
		String pseudo = "pseudo";
		bu1.setId(1L);
		bu1.setPseudo(pseudo);
		bu1.setFirstName("firstNameBU1");
		bu1.setLastName("lastNameBU1");
		bu1.setAppUser(appUser);
		when(buddyUserRepositoryMock.findBuddyUserById(anyLong())).thenReturn(bu1);

		// WHEN
		BuddyUser buddyUser = buddyUserService.findBuddyUserById(1L);
		
		// THEN
		assertThat(buddyUser.getPseudo()).isEqualTo("pseudo");
		verify(buddyUserRepositoryMock, times(1)).findBuddyUserById(anyLong());

	}

	/**
	 * Test method for
	 * {@link fr.annielec.paymybuddy.service.BuddyUserServiceImpl#findBuddyUserByEmail(java.lang.String)}.
	 */
	@Test
	void testFindBuddyUserByEmail() {
		
		// GIVEN
		bu1.setId(1L);
		bu1.setFirstName("firstNameBU1");
		bu1.setLastName("lastNameBU1");
		bu1.setAppUser(appUser);
		bu1.setPseudo(appUser.getUsername());
		when(appUserRepositoryMock.findUserByUsername(anyString())).thenReturn(appUser);
		when(buddyUserRepositoryMock.findBuddyUserById(anyLong())).thenReturn(bu1);

		// WHEN
		BuddyUser buddyUser = buddyUserService.findBuddyUserByEmail("hello@gmail.com");
		
		// THEN
		assertThat(buddyUser.getPseudo()).isEqualTo("hello@gmail.com");
		verify(appUserRepositoryMock, times(2)).findUserByUsername(anyString());

		
	}
	/**
	 * Test method for
	 * {@link fr.annielec.paymybuddy.service.BuddyUserServiceImpl#findBuddyUserByEmail(java.lang.String)}.
	 */
	@Test
	void testFindBuddyUserByEmail_givenException() {
		
		// GIVEN
		bu1.setId(1L);
		bu1.setFirstName("firstNameBU1");
		bu1.setLastName("lastNameBU1");
		String messageErreur = "Email non trouvé dans notre base de données!";
		when(appUserRepositoryMock.findUserByUsername(anyString())).thenReturn(null);

		// WHEN
		try {
			BuddyUser buddyUser = buddyUserService.findBuddyUserByEmail("absent@gmail.com");
		} catch (Exception e) {
		// THEN
		assertEquals(messageErreur, e.getMessage());
		verify(appUserRepositoryMock, times(1)).findUserByUsername(anyString());
		}
		
	}

}
