package fr.annielec.paymybuddy.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AppUser {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
    @Email
	private String username;
	@JsonIgnore
	private String password;
	private boolean active;
	//quand charge un user, on charge tous ses roles donc on choisit EAGER pas LAZY
	@ManyToMany(fetch = FetchType.EAGER)
	//qd on utilise eager, il est préférable d initialise la collection avec new arrayList : un utilsateur par defaut une liste vide, pas null !
	private Collection<AppRole> appRoles = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn( referencedColumnName = "id")
	private BuddyUser buddyUser;

}
