package fr.annielec.paymybuddy.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity

@Data @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"Contacts"})
public class BuddyUser {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


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
	
//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "myContactId", referencedColumnName = "id")
//	private Contact monContact;
	
//	@ManyToMany //(mappedBy = "buddyUserContacts", fetch=FetchType.LAZY)
//	@JoinTable( name = "T_BuddyUsers_Contacts_Associations",
//    	joinColumns = @JoinColumn( name = "idBuddyUser" ),
//    	inverseJoinColumns = @JoinColumn( name = "idContact" ) )
	@OneToMany(fetch = FetchType.LAZY)
	private List<Contact> Contacts = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY)
	private Collection<Transaction> transactions;
	
	
	

}
