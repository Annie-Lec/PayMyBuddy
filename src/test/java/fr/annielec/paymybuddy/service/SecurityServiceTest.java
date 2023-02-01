package fr.annielec.paymybuddy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import fr.annielec.paymybuddy.entities.AppRole;
import fr.annielec.paymybuddy.entities.AppUser;
import fr.annielec.paymybuddy.entities.BuddyAccount;
import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.repository.AppRoleRepository;
import fr.annielec.paymybuddy.repository.AppUserRepository;
import fr.annielec.paymybuddy.repository.BuddyAccountRepository;
import fr.annielec.paymybuddy.repository.BuddyUserRepository;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

	private static SecurityService securityService;

	@Mock
	private static AppUserRepository appUserRepositoryMock;
	@Mock
	private static AppRoleRepository appRoleRepositoryMock;
	@Mock
	private static BuddyUserRepository buddyUserRepositoryMock;
	@Mock
	private static BuddyAccountRepository buddyAccountRepositoryMock;
	@Mock
	private static PasswordEncoder passwordEncoder;
	private static AppUser appUser;
	private static AppRole appRole1;
	private static AppRole appRole2;
	private static BuddyUser bu1 = new BuddyUser();
	private static BuddyAccount ba1 = new BuddyAccount();
	private static List<AppRole> appRoles = new ArrayList<AppRole>();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		appRole1 = new AppRole(1L, "USER1", "descr User1");
		appRole2 = new AppRole(2L, "USER2", "descr User2");
		appRoles.add(appRole1);
		bu1.setId(1L);
		bu1.setFirstName("firstNameBU1");
		bu1.setLastName("lastNameBU1");
		ba1.setId(1L);
		ba1.setBalance(10);

		appUser = new AppUser(1L, "hello@gmail.com", "$2a$10$b0GhrNhc01ryv/KbKM6lNe8L/t2B0sYZAJT20p8BxvainvtB1IkkS",
				true, appRoles, bu1);
	}

	@BeforeEach
	void setUp() throws Exception {
		securityService = new SecurityServiceImpl(appUserRepositoryMock, appRoleRepositoryMock, buddyUserRepositoryMock,
				buddyAccountRepositoryMock, passwordEncoder);
	}

	@Test
	void testAddRoleToUser() {
		// Given
		when(appUserRepositoryMock.findUserByUsername(anyString())).thenReturn(appUser);
		when(appRoleRepositoryMock.findRoleByRoleName(anyString())).thenReturn(appRole2);
		// When
		securityService.addRoleToUser("hello@gmail.com", "USER2");
		// Then
		assertThat(appUser.getAppRoles()).contains(appRole2);
	}

	@Test
	void testLoadUserByUsername() {
		// Given
		when(appUserRepositoryMock.findUserByUsername(anyString())).thenReturn(appUser);
		// When
		AppUser appUserTest = securityService.loadUserByUsername("hello@gmail.com");
		// Then
		assertThat(appUserTest.getUsername()).isEqualTo("hello@gmail.com");
	}

	@Test
	void testGetId() {
		// Given
		when(appUserRepositoryMock.findUserByUsername(anyString())).thenReturn(appUser);
		// When
		Long idappUserTest = securityService.loadUserByUsername("hello@gmail.com").getId();
		// Then
		assertThat(idappUserTest).isEqualTo(1L);

	}

	@Test
	void testAddBuddyUserToUser_withUsernameAndPseudo() {
		// Given
		when(appUserRepositoryMock.findUserByUsername(anyString())).thenReturn(appUser);
		when(buddyUserRepositoryMock.findBuddyUserByPseudo(anyString())).thenReturn(bu1);
		when(appUserRepositoryMock.save(any(AppUser.class))).thenReturn(appUser);

		// When
		// appUser.setBuddyUser(bu1);
		securityService.AddBuddyUserToUser("hello@gmail.com", "hello@gmail.com");
		// Then
		assertThat(appUser.getBuddyUser().getFirstName()).isEqualTo("firstNameBU1");

	}

	@Test
	void testAddBuddyUserToUserOnlyWithUsername() {
		// Given
		when(appUserRepositoryMock.findUserByUsername(anyString())).thenReturn(appUser);
		when(buddyUserRepositoryMock.findBuddyUserById(anyLong())).thenReturn(bu1);
		when(buddyAccountRepositoryMock.findBuddyAccountById(anyLong())).thenReturn(ba1);

		// When
		securityService.AddBuddyUserToUser("hello@gmail.com");
		// Then
		assertThat(appUser.getBuddyUser().getFirstName()).isEqualTo("firstNameBU1");
		assertThat(appUser.getBuddyUser().getBuddyAccount().getBalance()).isEqualTo(10);

	}

	@Test
	void testSaveNewUser_withAgoodPwd() {
		// Given

		when(appUserRepositoryMock.save(any(AppUser.class))).thenReturn(appUser);
		when(passwordEncoder.encode("1234")).thenReturn("$2a$10$b0GhrNhc01ryv/KbKM6lNe8L/t2B0sYZAJT20p8BxvainvtB1IkkS");
		// When
		AppUser appUserTest = securityService.saveNewUser("hello@gmail.com", "1234", "1234");
		// then
		assertThat(appUserTest.getUsername()).isEqualTo("hello@gmail.com");
		assertThat(appUserTest.getPassword()).isEqualTo("$2a$10$b0GhrNhc01ryv/KbKM6lNe8L/t2B0sYZAJT20p8BxvainvtB1IkkS");
		verify(appUserRepositoryMock, times(1)).save(any(AppUser.class));

	}

	@Test
	void testSaveNewUser_generatingException() {
		// given
		String messageErreur = "Bad credentials";

		// WHEN
		try {
			AppUser appUserTest = securityService.saveNewUser("toto@gmail.com", "1234", "5678");
		} catch (Exception e) {
			// THEN
			assertEquals(messageErreur, e.getMessage());
			verify(appUserRepositoryMock, times(0)).save(any(AppUser.class));
		}

	}

	@Test
	void testSaveNewRole_withGoodRole() {

		// GIVEN
		when(appRoleRepositoryMock.findRoleByRoleName(anyString())).thenReturn(null);

		// WHEN
		securityService.saveNewRole("USER3", "type de user qui n existe pas encore");
		// THEN
		verify(appRoleRepositoryMock, times(1)).save(any(AppRole.class));

	}

	@Test
	void testSaveNewRole_generatingException() {
		// given
		String messageErreur = "Role name already exists";
		when(appRoleRepositoryMock.findRoleByRoleName(anyString())).thenReturn(appRole1);

		// WHEN
		try {
			securityService.saveNewRole("USER1", "type de user qui a deja ete defini en base");
		} catch (Exception e) {
			// THEN
			assertEquals(messageErreur, e.getMessage());
			verify(appRoleRepositoryMock, times(0)).save(any(AppRole.class));
		}

	}

}
