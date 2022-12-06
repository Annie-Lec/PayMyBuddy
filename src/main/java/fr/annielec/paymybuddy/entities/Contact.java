package fr.annielec.paymybuddy.entities;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Contact {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idContact;
	private String pseudoContact;
	private String lastNameContact;
	private String firstNameContact;

	
	@ManyToMany
	//pour un buddyuser tu me donnes ses contacts mais pour les contacts tu ne me donnes pas ses buddyusers
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Collection<BuddyUser> buddyUserContacts;
	
	private boolean activeStatusContact;
	
	@OneToMany(fetch = FetchType.LAZY)
	private Collection<Transaction> transaction;

}
