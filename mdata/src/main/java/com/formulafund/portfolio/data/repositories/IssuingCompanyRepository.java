package com.formulafund.portfolio.data.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.IssuingCompany;

public interface IssuingCompanyRepository extends CrudRepository<IssuingCompany, Long> {
	
	List<IssuingCompany> findByFullName(String aName);

}
