package fr.annielec.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.annielec.paymybuddy.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
