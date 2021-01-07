package com.formulafund.portfolio.data.repositories;

import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.ApplicationUser;

public interface UserRepository extends CrudRepository<ApplicationUser, Long> {

}
