package fr.annielec.paymybuddy.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import fr.annielec.paymybuddy.entities.BuddyUser;


public interface BuddyUserRepository extends JpaRepository<BuddyUser, Long> {
	// public BuddyUser findBuddyUserByLastName(String lastName);
	public BuddyUser findBuddyUserByPseudo(String pseudo);
	public BuddyUser findBuddyUserById(Long id);
	//public List<BuddyUser> findByPseudoContains(String pseudo);
	
	
	


}
