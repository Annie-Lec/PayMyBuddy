package fr.annielec.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.annielec.paymybuddy.entities.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole, Long>{
	
	AppRole findRoleByRoleName(String roleName);

}
