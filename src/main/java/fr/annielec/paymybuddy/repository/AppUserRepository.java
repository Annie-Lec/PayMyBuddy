package fr.annielec.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.annielec.paymybuddy.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long>{
	
	AppUser findUserByUsername(String username);

}
