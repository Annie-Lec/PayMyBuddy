package fr.annielec.paymybuddy.service;


import fr.annielec.paymybuddy.entities.BuddyAccount;


public interface BuddyAccountService {

	public BuddyAccount saveBuddyAccount(BuddyAccount buddyAccount);

	public BuddyAccount findBuddyAccountById(Long id);

}
