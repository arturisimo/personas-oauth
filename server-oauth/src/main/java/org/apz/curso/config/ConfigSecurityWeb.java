package org.apz.curso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class ConfigSecurityWeb extends WebSecurityConfigurerAdapter {
		
		@Autowired
		PasswordEncoder passwordEncoder;
	    
		/** repositorio de usuarios en memoria */
		@Bean
	    @Override
	    public UserDetailsService userDetailsService() {
	    	
	    	UserDetails user = User.builder().username("user1")
	    								   .password(passwordEncoder.encode("user1"))
	    								   .roles("USER").build();
	    	UserDetails userAdmin = User.builder()
	    									.username("admin")
	    									.password(passwordEncoder.encode("admin"))
	    									.roles("ADMIN").build();
	        return new InMemoryUserDetailsManager(user,userAdmin);
	    }
	    
		@Bean
		public AuthenticationManager authMg() throws Exception {
			return super.authenticationManagerBean();
		}
	    
}
