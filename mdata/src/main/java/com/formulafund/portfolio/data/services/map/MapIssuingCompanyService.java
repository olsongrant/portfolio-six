package com.formulafund.portfolio.data.services.map;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.services.IssuingCompanyService;

@Service
@Profile("map")
public class MapIssuingCompanyService extends BaseMapService<IssuingCompany> implements IssuingCompanyService {

	@Override
	public IssuingCompany getInstanceFor(String coName) {
		Optional<IssuingCompany> potentialCompany = this.map.values()
				.stream()
				.filter(c -> coName.equalsIgnoreCase(c.getFullName()))
				.findAny();
		if (potentialCompany.isPresent()) {
			return potentialCompany.get();
		} else {
			IssuingCompany co = new IssuingCompany();
			co.setFullName(coName);
			this.save(co);
			return co;
		}
	}



}
