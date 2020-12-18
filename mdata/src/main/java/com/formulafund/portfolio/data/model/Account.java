package com.formulafund.portfolio.data.model;

import java.util.HashSet;
import java.util.Set;

public class Account extends BaseEntity {
	@Override
	public String toString() {
		return "Account [name=" + name + ", user=" + user + ", id=" + id + "]";
	}
	private String name;
	private User user;
	private Set<Portfolio> portfolios = new HashSet<>();
	
	public Set<Portfolio> getPortfolios() {
		return portfolios;
	}
	public void setPortfolios(Set<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Account with(String aName, User aUser) {
		Account a = new Account();
		a.setName(aName);
		a.setUser(aUser);
		return a;
	}

}
