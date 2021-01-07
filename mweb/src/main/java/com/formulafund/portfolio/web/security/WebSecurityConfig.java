package com.formulafund.portfolio.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		UserDetails user1 =
				 User.withDefaultPasswordEncoder()
					.username("grantcine")
					.password("grantcine")
					.roles("USER")
					.build();
		
		UserDetails user2 = User.withDefaultPasswordEncoder()
				.username("daffy")
				.password("ducksrule")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user1, user2);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/", 
					"/index", 
					"/user", 
					"/user/index", 
					"/resources/**",
					"/webjars/**",
					"/user/registration",
					"/login",
					"/holdings/h",
					"/oups").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login")
			.permitAll()
			.and()
		.logout()
			.permitAll();
	}
	
	/*
	 * This was necessary to allow the H2 console's security to work
	 */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers("/h2-console/**");
    }

}

