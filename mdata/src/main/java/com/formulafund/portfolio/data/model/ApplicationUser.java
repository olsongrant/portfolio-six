package com.formulafund.portfolio.data.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;

@Entity
@Getter
@Setter
@Slf4j
public class ApplicationUser extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String handle;
	private String password;
	private String socialPlatformId;
	private boolean enabled;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Account> accounts = new HashSet<>();
	
	public static ApplicationUser with(String fName, String surname, String handle) {
		log.debug("in User::of");
		ApplicationUser u = new ApplicationUser();
		u.setFirstName(fName);
		u.setLastName(surname);
		u.setHandle(handle);
		return u;
	}
	
	public String getFullName() {
		return (this.getFirstName() + " " + this.getLastName());
	}
	
	@Override
	public String toString() {
		log.debug("in User::toString");
		return this.getEmailAddress();
	}

	public Object getCredentials() {
		HashMap<String, String> map = new HashMap<>();
		map.put("username", this.getEmailAddress());
		map.put("password", this.getPassword());
		return map;
	}
	
	public String getPassword() {
		if (this.password == null) {
			return this.socialPlatformId;
		}
		return this.password;
	}
}
