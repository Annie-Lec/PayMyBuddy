package fr.annielec.paymybuddy.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import fr.annielec.paymybuddy.entities.BuddyAccount;

import fr.annielec.paymybuddy.repository.BuddyAccountRepository;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class BuddyAccountServiceImpl implements BuddyAccountService {

	private BuddyAccountRepository buddyAccountRepository;

	@Override
	public BuddyAccount saveBuddyAccount(BuddyAccount buddyAccount) {

		return buddyAccountRepository.save(buddyAccount);
	}

	@Override
	public BuddyAccount findBuddyAccountById(Long id) {

		return buddyAccountRepository.findBuddyAccountById(id);
	}

}
