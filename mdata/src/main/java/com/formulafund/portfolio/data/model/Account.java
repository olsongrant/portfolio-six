package com.formulafund.portfolio.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Setter
@Slf4j
public class Account extends BaseEntity {
	@Override
	public String toString() {
		return "Account [name=" + name + ", user=" + user + ", id=" + id + "]";
	}
	private String name;
	
    @ManyToOne
    @JoinColumn(name = "user_id")	
	private User user;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
	private Set<Portfolio> portfolios = new HashSet<>();
	

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
