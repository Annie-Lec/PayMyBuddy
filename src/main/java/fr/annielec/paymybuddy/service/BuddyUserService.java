package fr.annielec.paymybuddy.service;


import fr.annielec.paymybuddy.entities.BuddyUser;


public interface BuddyUserService {

	public BuddyUser saveBuddyUser(BuddyUser buddyUser);

	public BuddyUser findBuddyUserByEmail(String email) throws NullPointerException;

	public BuddyUser findBuddyUserById(Long id);

	public String retrievePseudoWithIdUser(Long id);
}

