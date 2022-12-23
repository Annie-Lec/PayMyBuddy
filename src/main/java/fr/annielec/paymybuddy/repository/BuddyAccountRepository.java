package fr.annielec.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.annielec.paymybuddy.entities.BuddyAccount;
import fr.annielec.paymybuddy.entities.BuddyUser;


public interface BuddyAccountRepository extends JpaRepository<BuddyAccount, Long> {
	public BuddyAccount findByBuddyUser(BuddyUser buddyUser);
	public BuddyAccount findBuddyAccountById(Long id);
}
