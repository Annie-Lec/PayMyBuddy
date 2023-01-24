package fr.annielec.paymybuddy.configuration;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import fr.annielec.paymybuddy.service.UserDetailsServiceImpl;

@Component

public class UtilityConfig {
	 

	public UtilityConfig() {
		// TODO Auto-generated constructor stub
	}
	public void refreshContextWithNewRole(String username, Authentication authentication, UserDetailsServiceImpl userDetailsService) {
		UserDetails ud = userDetailsService.loadUserByUsername(username);
		Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
				authentication.getCredentials(), ud.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

}
