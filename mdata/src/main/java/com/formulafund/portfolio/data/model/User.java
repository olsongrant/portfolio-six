package com.formulafund.portfolio.data.model;

import java.util.Set;
import java.util.HashSet;

public class User extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fullName;
	private String handle;
	private Set<Account> accounts = new HashSet<>();
	
	public Set<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	
	public static User with(String nameString, String handle) {
		User u = new User();
		u.setFullName(nameString);
		u.setHandle(handle);
		return u;
	}
	@Override
	public String toString() {
		return "User [fullName=" + fullName + ", handle=" + handle + ", accounts=" + accounts + ", id=" + id + "]";
	}
	
	
}
