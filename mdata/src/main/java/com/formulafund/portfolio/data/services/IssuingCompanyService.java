package com.formulafund.portfolio.data.services;

import com.formulafund.portfolio.data.model.IssuingCompany;

public interface IssuingCompanyService extends CrudService<IssuingCompany> {
	public IssuingCompany getInstanceFor(String coName);
}
