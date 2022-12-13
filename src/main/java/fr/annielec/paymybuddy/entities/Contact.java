package fr.annielec.paymybuddy.entities;


import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//	private String pseudo;
//	private String lastName;
//	private String firstName;

//	@OneToOne(mappedBy = "monContact")
//	private BuddyUser myContactId;

	private Long idContact;

//	@ManyToMany
//	@JoinTable( name = "T_BuddyUsers_Contacts_Associations",
//		joinColumns = @JoinColumn( name = "idContact" ),
//		inverseJoinColumns = @JoinColumn( name = "idBuddyUser" ) )
	@ManyToOne
	private BuddyUser buddyUser;

	private boolean activeStatusContact;

//	@OneToMany(fetch = FetchType.LAZY)
//	private Collection<Transaction> transaction;

}
