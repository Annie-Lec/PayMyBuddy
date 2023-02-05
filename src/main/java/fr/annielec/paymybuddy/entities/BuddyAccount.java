package fr.annielec.paymybuddy.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OneToOne;
import javax.validation.constraints.Min;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class BuddyAccount {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Min(value = 0)
	private double balance;
	
	@Size(max=27)
	private String iban;
	
	@Size(max =  40)
	private String bankName;
	
	@OneToOne(mappedBy = "buddyAccount")
	private BuddyUser buddyUser;
	
	private double amountToCharge;
	

}
