package fr.annielec.paymybuddy.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.annielec.paymybuddy.entities.AppRole;
import fr.annielec.paymybuddy.entities.AppUser;
import fr.annielec.paymybuddy.entities.BuddyAccount;
import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.repository.AppRoleRepository;
import fr.annielec.paymybuddy.repository.AppUserRepository;
import fr.annielec.paymybuddy.repository.BuddyAccountRepository;
import fr.annielec.paymybuddy.repository.BuddyUserRepository;
import groovy.util.logging.Slf4j;

@Service
@Transactional
@Slf4j
public class SecurityServiceImpl implements SecurityService {

	private AppUserRepository appUserRepository;
	private AppRoleRepository appRoleRepository;
	private BuddyUserRepository buddyUserRepository;
	private BuddyAccountRepository buddyAccountRepository;

	private PasswordEncoder passwordEncoder;

	public SecurityServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,
			BuddyUserRepository buddyUserRepository, BuddyAccountRepository buddyAccountRepository,
			PasswordEncoder passwordEncoder) {
		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
		this.buddyUserRepository = buddyUserRepository;
		this.buddyAccountRepository = buddyAccountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AppUser addNewUser(AppUser appUser) {
		return appUserRepository.save(appUser);
	}

	@Override
	public AppRole addNewRole(AppRole appRole) {
		return appRoleRepository.save(appRole);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		AppUser appUser = appUserRepository.findUserByUsername(username);
		AppRole appRole = appRoleRepository.findRoleByRoleName(roleName);

		appUser.getAppRoles().add(appRole);
	}

	@Override
	public AppUser loadUserByUsername(String username) {
		AppUser appUser = appUserRepository.findUserByUsername(username);
		return appUser;
	}

	@Override
	public Long getId(String username) {
		AppUser appUser = appUserRepository.findUserByUsername(username);
		return appUser.getId();
	}

	@Override
	public List<AppUser> listUsers() {
		return appUserRepository.findAll();
	}

	@Override
	public void removeUser(AppUser appUser) {
		appUserRepository.delete(appUser);
	}

	@Override
	public void AddBuddyUserToUser(String username, String pseudo) {
		AppUser appUser = appUserRepository.findUserByUsername(username);
		BuddyUser buddyUser = buddyUserRepository.findBuddyUserByPseudo(pseudo);

		appUser.setBuddyUser(buddyUser);
		appUserRepository.save(appUser);
	}

	@Override
	public void AddBuddyUserToUser(String username) {
		AppUser appUser = appUserRepository.findUserByUsername(username);
		BuddyUser buddyUser = new BuddyUser();
		BuddyAccount buddyAccount = new BuddyAccount();

		buddyUserRepository.save(buddyUser);
		buddyUser = buddyUserRepository.findBuddyUserById(appUser.getId());
		buddyUser.setPseudo("pseudo" + buddyUser.getId());
		buddyUser.setBuddyAccount(buddyAccount);
		buddyUserRepository.save(buddyUser);

		buddyAccountRepository.save(buddyAccount);
		buddyAccount = buddyAccountRepository.findBuddyAccountById(appUser.getId());
		buddyAccount.setBuddyUser(buddyUser);
		buddyAccountRepository.save(buddyAccount);

		appUser.setBuddyUser(buddyUser);
		addRoleToUser(username, "USER");
		appUserRepository.save(appUser);
	}

	@Override
	public AppUser saveNewUser(String username, String password, String verifyPwd) {
		if (!password.equals(verifyPwd))
			throw new RuntimeException("Bad credentials");

		String hachedPwd = passwordEncoder.encode(password);
		AppUser appUser = new AppUser();

		appUser.setUsername(username);
		appUser.setPassword(hachedPwd);
		appUser.setActive(true);

		AppUser saveAppUser = appUserRepository.save(appUser);

		return saveAppUser;
	}

}
