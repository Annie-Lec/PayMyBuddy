package fr.annielec.paymybuddy.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity

@Data @NoArgsConstructor @AllArgsConstructor
public class BuddyUser {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	//private String email;
	@Column(unique = true)
	private String pseudo;
	private String lastName;
	private String firstName;
	
	@Temporal(TemporalType.DATE)
	private Date dateDenaissance;
	
	@OneToOne(mappedBy = "buddyUser")
	private AppUser appUser;
		
	@OneToOne
	@JoinColumn(name = "buddyAccount_id", referencedColumnName = "id")
	private BuddyAccount buddyAccount;
		
	@ManyToMany(mappedBy = "buddyUserContacts", fetch=FetchType.LAZY)
	private Collection<Contact> contacts;
	
	@OneToMany(fetch = FetchType.LAZY)
	private Collection<Transaction> transactions;
	
	
	

}
