package com.formulafund.portfolio.data.services.springdatajpa;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.repositories.IssuingCompanyRepository;
import com.formulafund.portfolio.data.services.IssuingCompanyService;

@Service
@Profile("springdatajpa")
public class SDJPAIssuingCompanyService implements IssuingCompanyService {
	
	private IssuingCompanyRepository issuingCompanyRepository;
	
	public SDJPAIssuingCompanyService(IssuingCompanyRepository icRepository) {
		this.issuingCompanyRepository = icRepository;
	}

	@Override
	public Set<IssuingCompany> findAll() {
		Set<IssuingCompany> issuingCompanies = new HashSet<>();
		this.issuingCompanyRepository.findAll().forEach(issuingCompanies::add);
		return issuingCompanies;
	}

	@Override
	public IssuingCompany findById(Long id) {
		return this.issuingCompanyRepository.findById(id).orElse(null);
	}

	@Override
	public IssuingCompany save(IssuingCompany object) {
		return this.issuingCompanyRepository.save(object);
	}

	@Override
	public void delete(IssuingCompany object) {
		this.issuingCompanyRepository.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		this.issuingCompanyRepository.deleteById(id);
	}

	@Override
	public IssuingCompany getInstanceFor(String coName) {
		Optional<IssuingCompany> potentialCompany = this.findByName(coName);
		if (potentialCompany.isPresent()) return potentialCompany.get();
		IssuingCompany aCompany = new IssuingCompany();
		aCompany.setFullName(coName);
		this.save(aCompany);
		return aCompany;
	}

}
