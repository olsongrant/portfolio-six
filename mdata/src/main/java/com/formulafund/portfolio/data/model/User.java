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

import java.util.HashSet;

@Entity
@Getter
@Setter
@Slf4j
public class User extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fullName;
	private String handle;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Account> accounts = new HashSet<>();
	
	public static User with(String nameString, String handle) {
		log.debug("in User::of");
		User u = new User();
		u.setFullName(nameString);
		u.setHandle(handle);
		return u;
	}
	@Override
	public String toString() {
		log.debug("in User::toString");
		return "User [fullName=" + fullName + ", handle=" + handle + ", id=" + id + "]";
	}
	
	
}
