package com.formulafund.portfolio.data.model;

public class IssuingCompany extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fullName;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "IssuingCompany [fullName=" + fullName + ", id=" + id + "]";
	}
	
}
