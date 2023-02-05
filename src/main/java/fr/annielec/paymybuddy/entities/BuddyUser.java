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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity

@Data @NoArgsConstructor @AllArgsConstructor
public class BuddyUser {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String pseudo;
	private String lastName;
	private String firstName;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dateDenaissance;
	
	@OneToOne(mappedBy = "buddyUser")
	private AppUser appUser;
		
	@OneToOne
	@JoinColumn(name = "buddyAccount_id", referencedColumnName = "id")
	private BuddyAccount buddyAccount;
	
	@OneToMany(fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<Contact> contacts = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<Transaction> transactions = new ArrayList<>();
	
	
	

}
