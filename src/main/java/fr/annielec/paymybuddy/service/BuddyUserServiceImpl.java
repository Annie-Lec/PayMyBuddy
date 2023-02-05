package fr.annielec.paymybuddy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fr.annielec.paymybuddy.entities.AppUser;
import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.repository.AppUserRepository;
import fr.annielec.paymybuddy.repository.BuddyUserRepository;
import lombok.AllArgsConstructor;

@Service
@Transactional(noRollbackFor = {NullPointerException.class})
@AllArgsConstructor
public class BuddyUserServiceImpl implements BuddyUserService {

	private BuddyUserRepository buddyUserRepository;
	private AppUserRepository appUserRepository;

	@Override
	public BuddyUser saveBuddyUser(BuddyUser buddyUser) {
		return buddyUserRepository.save(buddyUser);

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
	public BuddyUser findBuddyUserByEmail(String email) throws NullPointerException {
		AppUser appUser = appUserRepository.findUserByUsername(email);
		BuddyUser buddyUser = null;
		Long idUser;
		if (appUser == null) {
			throw new NullPointerException("Email non trouvé dans notre base de données!");
		} else {
			idUser = appUserRepository.findUserByUsername(email).getId();
			buddyUser = buddyUserRepository.findBuddyUserById(idUser);
		}

		return buddyUser;

	}

}
