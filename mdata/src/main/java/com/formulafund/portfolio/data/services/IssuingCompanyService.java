package com.formulafund.portfolio.data.services;

import java.util.Optional;

import com.formulafund.portfolio.data.model.IssuingCompany;

public interface IssuingCompanyService extends CrudService<IssuingCompany> {
	public IssuingCompany getInstanceFor(String coName);
	
	public default Optional<IssuingCompany> findByName(String aName) {
		if (aName == null) throw new RuntimeException("sent in null to IssuingCompanyService::findByName");
		return this.findAll()
				.stream()
				.filter(c -> aName.equalsIgnoreCase(c.getFullName()))
				.findAny();
	}
}
