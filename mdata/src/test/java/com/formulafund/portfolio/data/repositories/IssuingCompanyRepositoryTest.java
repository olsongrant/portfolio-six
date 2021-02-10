package com.formulafund.portfolio.data.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import com.formulafund.portfolio.data.model.IssuingCompany;


@DataJpaTest
class IssuingCompanyRepositoryTest {
	
	@Autowired
	IssuingCompanyRepository icRepository;
	
	Long issuingCompanyId;

	@BeforeEach
	void setUp() throws Exception {
		IssuingCompany issuingCompany = new IssuingCompany();
		issuingCompany.setFullName("Acme Rocket Launchers");
		issuingCompany = this.icRepository.save(issuingCompany);
		this.issuingCompanyId = issuingCompany.getId();
	}

	@AfterEach
	void tearDown() throws Exception {
		this.icRepository.deleteById(issuingCompanyId);
	}

	@Test
	void testConfiguration() {
		Optional<IssuingCompany> shouldBeAcme = this.icRepository.findById(issuingCompanyId);
		assert(shouldBeAcme.isPresent());
	}

	@Test
	void testNameFinder() {
		List<IssuingCompany> companies = this.icRepository.findByFullName("Acme Rocket Launchers");
		boolean nonZero = companies.size() > 0;
		assert(nonZero);
		List<IssuingCompany> expectingEmptyList = this.icRepository.findByFullName("0 Companies With This Name");
		assertNotNull(expectingEmptyList);
		assert(expectingEmptyList.isEmpty());
	}
}
