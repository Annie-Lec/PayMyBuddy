package fr.annielec.paymybuddy.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.ManyToOne;

import javax.validation.constraints.Min;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Transaction {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Min(value = 0)
	private double amount;
	private double fees;
	@Enumerated(EnumType.STRING)
	private TypeTransaction type;
	@ManyToOne
	private BuddyUser transmitter;
	@ManyToOne
	private BuddyUser beneficiary;
	private String description;
	//@Past
	private Date date;
	

	
}
