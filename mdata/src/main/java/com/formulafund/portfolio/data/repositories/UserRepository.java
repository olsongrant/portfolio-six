package com.formulafund.portfolio.data.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.ApplicationUser;

public interface UserRepository extends CrudRepository<ApplicationUser, Long> {
	List<ApplicationUser> findByEmailAddress(String anEmailAddress);
	List<ApplicationUser> findBySocialPlatformId(String aPlatformId);


}
