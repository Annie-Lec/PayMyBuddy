package fr.annielec.paymybuddy.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import fr.annielec.paymybuddy.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	
	public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
		
		this.userDetailsService = userDetailsService;
	}

	UserDetailsServiceImpl userDetailsService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
		//page Home accessible à tous même sans connexion
		http.authorizeRequests().antMatchers("/", "/home/**", "/register/**", "/login/**").permitAll();
		// attention aux accès aux répertoires static qui contiennent les css!! Si on
		// donne l accès au menu home à tt le monde par ex...
		http.authorizeRequests().antMatchers("/webjars/**","/media/**","/css/**" ).permitAll();
		
		//autres pages : utilisateur doit être authenticated();
		http.authorizeRequests()
        .antMatchers("/anonymous*")
        .anonymous()
        .antMatchers("/login*")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
        .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
        .defaultSuccessUrl("/")
        //TODO validation page
 //       .failureUrl("/login.html?error=true")
        .and()
        .logout()
        .deleteCookies("JSESSIONID")
        .and()
        .rememberMe()
        .key("uniqueAndSecret");
		
		
		http.authenticationProvider(authenticationProvider());
		return http.build();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

//	//BCrypt pour cryptage du mot de passe : attention ref circulaire avec Brcyp : à masser dans main class
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}

}
