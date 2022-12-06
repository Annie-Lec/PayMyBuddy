package fr.annielec.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.annielec.paymybuddy.entities.BuddyAccount;

public interface BuddyAccountRepository extends JpaRepository<BuddyAccount, Long> {

}
