package com.formulafund.portfolio.data.services.springdatajpa;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.repositories.IssuingCompanyRepository;
import com.formulafund.portfolio.data.services.IssuingCompanyService;

@Service
@Profile({"mysqldev", "h2dev", "mysqlprod", "mysqlaws"})
public class SDJPAIssuingCompanyService implements IssuingCompanyService {
	
	@Override
	public Optional<IssuingCompany> findByName(String aName) {
		List<IssuingCompany> companyList = this.issuingCompanyRepository.findByFullName(aName);
		if (companyList.isEmpty()) return Optional.empty();
		return Optional.ofNullable(companyList.get(0));
	}

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
