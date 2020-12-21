package com.formulafund.portfolio.data.repositories;

import org.springframework.data.repository.CrudRepository;

import com.formulafund.portfolio.data.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
