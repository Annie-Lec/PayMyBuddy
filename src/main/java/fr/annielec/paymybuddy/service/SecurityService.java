package fr.annielec.paymybuddy.service;

import java.util.List;

import fr.annielec.paymybuddy.entities.AppRole;
import fr.annielec.paymybuddy.entities.AppUser;


public interface SecurityService {
	
	public AppUser saveNewUser(String username, String password, String verifyPwd);
	AppUser addNewUser(AppUser appUser);
	AppRole addNewRole(AppRole appRole);
	void addRoleToUser(String username, String roleName);
	AppUser loadUserByUsername(String username);
	//pour des futurs admin
	List<AppUser> listUsers();
	void removeUser(AppUser appUser);
	public Long getId(String username);
	//pour lier au groupe buddy
		void AddBuddyUserToUser(String username, String pseudo);
		public void AddBuddyUserToUser(String username);

}
