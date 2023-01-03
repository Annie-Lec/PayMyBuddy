package fr.annielec.paymybuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import fr.annielec.paymybuddy.entities.BuddyUser;
import fr.annielec.paymybuddy.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	public List<Transaction> findTransactionByTransmitter(BuddyUser transmitter);

	public List<Transaction> findTransactionByBeneficiary(BuddyUser beneficiary);

}
